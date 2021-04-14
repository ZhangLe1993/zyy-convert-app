package com.biubiu.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author ：张音乐
 * @date ：Created in 2021/4/14 下午2:05
 * @description：解析抖音产生的模型
 * @email: zhangyule1993@sina.com
 * @version: 1.0
 */
@Getter
@Setter
public class DouYinModel {

    /**
     * 视频地址
     */
    private String videoAddr;

    /**
     * 封面地址
     */
    private String coverAddr;

    public DouYinModel() {
    }

    public DouYinModel(String videoAddr, String coverAddr) {
        this.videoAddr = videoAddr;
        this.coverAddr = coverAddr;
    }
}