package com.huajie.domain.common.enums;

public enum DeviceStateEnum {

    /**
     * 设备状态  NORMAL ABNORMAL EXPIRED
     */

    NORMAL("NORMAL","正常"),
    ABNORMAL("ABNORMAL","异常"),
    EXPIRED("EXPIRED","已过期"),
    ;

    String code;

    String name;

    DeviceStateEnum(String code, String name){
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
