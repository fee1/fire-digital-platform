package com.huajie.domain.common.enums;

/**
 * 企业消防安全类型
 */
public enum EnterpriseFireTypeEnum {

    ImportantFirePlace("importantFirePlace","消防重点单位"),
    PublicPlace("publicPlace","公共聚集场所"),
    GeneralPlace("generalPlace","一般场所");

    String code;

    String name;

    EnterpriseFireTypeEnum(String code, String name){
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
