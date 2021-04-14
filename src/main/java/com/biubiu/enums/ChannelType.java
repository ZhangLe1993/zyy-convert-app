package com.biubiu.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * @author ：张音乐
 * @date ：Created in 2021/4/13 上午9:16
 * @description： 频道类型
 * @email: zhangyule1993@sina.com
 * @version: 1.0
 */
public enum ChannelType {

    DouYin("douYin", "抖音"),
    ;

    /* 编码值 */
    private String code;

    /* 描述 */
    private String desc;

    ChannelType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }


    public static boolean check(String code) {
        if(StringUtils.isBlank(code)) {
            return false;
        }
        for(ChannelType type : values()) {
            if(type.getCode().equals(code)) {
                return true;
            }
        }
        return false;
    }


    public static ChannelType getByCode(String code) {
        if(StringUtils.isBlank(code)) {
            return null;
        }
        for(ChannelType type : values()) {
            if(type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
