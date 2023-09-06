package com.huajie.domain.common.enums;

public enum PowerTypeEnum {

    DeviceType01("battery","蓄电池"),
    DeviceType02("lamp","照明电");

    String code;

    String name;

    PowerTypeEnum(String code, String name){
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
