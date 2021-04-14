package com.biubiu.controller;

import com.biubiu.core.annotation.SystemLog;
import com.biubiu.dto.AnalysisDTO;
import com.biubiu.service.VideoService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author ：张音乐
 * @date ：Created in 2021/4/13 上午9:12
 * @description：视频接口
 * @email: zhangyule1993@sina.com
 * @version: 1.0
 */

@RestController
@RequestMapping(value = "/api/video", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class VideoController {

    private final static Logger logger = LoggerFactory.getLogger(VideoController.class);

    @Resource
    private VideoService videoService;

    @SystemLog(description = "二次解析接口")
    @PostMapping(value = "analysis")
    public ResponseEntity<?> analysis(@RequestBody AnalysisDTO analysisDTO) {
        if(analysisDTO == null || StringUtils.isBlank(analysisDTO.getCode()) || StringUtils.isBlank(analysisDTO.getText())) {
            return new ResponseEntity<>("参数错误", HttpStatus.BAD_REQUEST);
        }
        try {
            Map<String, String> map = videoService.analysis(analysisDTO);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch(Exception e) {
            logger.error("", e);
        }
        return new ResponseEntity<>("服务异常", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}