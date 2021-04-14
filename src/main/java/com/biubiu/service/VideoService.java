package com.biubiu.service;

import com.biubiu.dto.AnalysisDTO;
import com.biubiu.enums.ChannelType;
import com.biubiu.model.DouYinModel;
import com.biubiu.utils.AnalysisUtils;
import com.biubiu.utils.VideoConvert;
import com.google.common.collect.ImmutableMap;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameRecorder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author ：张音乐
 * @date ：Created in 2021/4/13 上午9:26
 * @description：视频业务处理
 * @email: zhangyule1993@sina.com
 * @version: 1.0
 */
@Service
public class VideoService {

    @Value("${ffmpeg.video.folder}")
    private String videoFolder;

    /**
     * 二次解析后返回地址就好了, 是否需要下载让前端去选择
     * @param analysisDTO
     * @return
     * @throws FrameGrabber.Exception
     * @throws FrameRecorder.Exception
     */
    public Map<String, String> analysis(AnalysisDTO analysisDTO) throws FrameGrabber.Exception, FrameRecorder.Exception {
        String text = analysisDTO.getText();
        String code = analysisDTO.getCode();
        ChannelType channel = ChannelType.getByCode(code);
        if(channel == null) {
            return null;
        }
        DouYinModel addrModel = AnalysisUtils.douYin(text);
        if(addrModel == null) {
            return null;
        }
        String fileName = System.currentTimeMillis() + "." + VideoConvert.NORMAL_VIDEO_TYPE;
        String videoFile = videoFolder + fileName;
        VideoConvert.record(addrModel.getVideoAddr(), videoFile);
        return ImmutableMap.of("vid", fileName, "cover", addrModel.getCoverAddr());
    }


}