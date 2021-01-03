package com.biubiu.controller;

import com.biubiu.core.annotation.SystemLog;
import com.biubiu.dto.PictureWaterDeLogoDTO;
import com.biubiu.utils.FfmpegUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Map;


@RestController
@RequestMapping(value = "/api/picture/water", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PictureWaterController {

    private final static Logger logger = LoggerFactory.getLogger(PictureWaterController.class);

    @Value("${ffmpeg.picture.folder}")
    private String targetFolder;

    @SystemLog(description = "图片去除水印")
    @PostMapping(value = "delogo")
    public ResponseEntity<?> delogo(@RequestBody PictureWaterDeLogoDTO pictureWaterDeLogoDTO) {
        try {
            Map<String, Object> map = FfmpegUtils.deLogoPictureWater(pictureWaterDeLogoDTO.getBase64(), pictureWaterDeLogoDTO.getType(), targetFolder, pictureWaterDeLogoDTO.getList());
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch(Exception e) {
            logger.error("", e);
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
