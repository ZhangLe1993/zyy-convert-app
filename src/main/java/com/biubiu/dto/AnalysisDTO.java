package com.biubiu.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author ：张音乐
 * @date ：Created in 2021/4/13 上午9:16
 * @description：二次解析接口参数
 * @email: zhangyule1993@sina.com
 * @version: 1.0
 */
@Getter
@Setter
public class AnalysisDTO {

    /**
     * 提取码或者链接
     */
    private String text;

    /**
     * 类型, 哪个频道的, 例如 抖音, 网易云, 等等
     */
    private String code;


}