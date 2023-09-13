package com.huajie.domain.common.enums;

/**
 * 企业消防安全类型
 */
public enum EnterpriseTypeEnum {

    Company("company","机关团体事业单位"),
    Enterprise("enterprise","企业、场所"),
    Merchant("merchant","一般商户"),
    RentalHouse("rentalHouse","出租房");

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
