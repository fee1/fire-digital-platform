package com.huajie.domain.common.enums;

public enum PowerTypeEnum {

    Battery("Battery","蓄电池"),
    Lamp("Lamp","照明电");

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
