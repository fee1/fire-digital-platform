package com.huajie.domain.common.enums;

public enum ExtinguisherTypeEnum {

    WaterBase("WaterBase","水基型"),
    DryPowder("DryPowder","干粉"),
    Foam("Foam","泡沫"),
    Gas("Gas","气体");

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
