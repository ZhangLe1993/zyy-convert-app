package com.biubiu.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.biubiu.model.DouYinModel;
import org.apache.commons.lang3.StringUtils;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameRecorder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ：张音乐
 * @date ：Created in 2021/4/12 上午10:25
 * @description：网络视频二次解析工具类
 * @email: zhangyule1993@sina.com
 * @version: 1.0
 */
public class AnalysisUtils {

    public final static String DOU_YIN_WEB_API = "https://www.iesdouyin.com/web/api/v2/aweme/iteminfo/?item_ids=";


    /**
     * 根据赋值的分享码下载抖音视频
     * @param text
     * @throws FrameGrabber.Exception
     * @throws FrameRecorder.Exception
     */
    public static DouYinModel douYin(String text) {
        //
        String url = pickURI(text);
        RestTemplate client = new RestTemplate();
        //
        HttpHeaders headers = client.headForHeaders(url);
        String location = headers.getLocation().toString();
        String vid = StringUtils.substringBetween(location, "/video/", "/?");

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders queryHeaders = new HttpHeaders();
        queryHeaders.set(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.114 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<>(queryHeaders);
        ResponseEntity<JSONObject> response = restTemplate.exchange(DOU_YIN_WEB_API + vid, HttpMethod.GET, entity, JSONObject.class);

        if(response.getStatusCodeValue() != 200) {
            return null;
        }
        JSONObject body = response.getBody();
        assert body != null;
        List<JSONObject> list = JSONArray.parseObject(body.getJSONArray("item_list").toJSONString(), new TypeReference<List<JSONObject>>(){});
        if(list.size() == 0) {
            return null;
        }
        JSONObject item = list.get(0);
        JSONObject video = item.getJSONObject("video");
        JSONObject playAddr = video.getJSONObject("play_addr");
        JSONArray urlList = playAddr.getJSONArray("url_list");
        List<String> urlListArr = JSONArray.parseObject(urlList.toJSONString(), new TypeReference<List<String>>(){});
        JSONObject coverAddr = video.getJSONObject("origin_cover");
        JSONArray coverUrlList = coverAddr.getJSONArray("url_list");
        List<String> coverUrlListArr = JSONArray.parseObject(coverUrlList.toJSONString(), new TypeReference<List<String>>(){});
        if(urlListArr.size() == 0 || coverUrlListArr.size() == 0) {
            return null;
        }

        return new DouYinModel(urlListArr.get(0), coverUrlListArr.get(0));
        // VideoConvert.record(finalAddr, "/home/yinyue/upload/红马.flv");
    }


    /**
     * 正则表达式提取 url
     * @param text
     * @return
     */
    public static String pickURI(String text) {
        // eg: text = "5.1 GV:/ 一出场就给人一种江南的感觉%刘亦菲 %精彩片段 %歌曲红马  https://v.douyin.com/e614JkV/ 腹制佌lian接，打开Dou音搜索，直接观kan视頻！";
        Pattern pattern = Pattern.compile("https?://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group();
        }
        return "";
    }

}