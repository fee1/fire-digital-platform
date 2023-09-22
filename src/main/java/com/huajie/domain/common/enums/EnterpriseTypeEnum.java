package com.huajie.domain.common.enums;

/**
 * 企业消防安全类型
 */
public enum EnterpriseTypeEnum {

    Company("Company","机关团体事业单位"),
    Enterprise("Enterprise","企业、场所"),
    Merchant("Merchant","一般商户"),
    RentalHouse("RentalHouse","出租房");

    String code;

    String name;

    EnterpriseTypeEnum(String code, String name){
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
