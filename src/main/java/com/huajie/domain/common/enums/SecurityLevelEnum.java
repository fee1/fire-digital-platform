package com.huajie.domain.common.enums;

public enum SecurityLevelEnum {

    /**
     * 安全等级  LOW MIDDLE HIGH
     */

    LOW("LOW","安全等级-低"),
    MIDDLE("MIDDLE","安全等级-中"),
    HIGH("HIGH","安全等级-高"),
    ;

    String code;

    String name;

    SecurityLevelEnum(String code, String name){
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
