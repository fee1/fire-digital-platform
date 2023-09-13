package com.huajie.domain.common.enums;

public enum ExtinguisherTypeEnum {

    WaterBase("waterBase","水基型"),
    DryPowder("dryPowder","干粉"),
    Foam("foam","泡沫"),
    Gas("gas","气体");

    String code;

    String name;

    ExtinguisherTypeEnum(String code, String name){
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
