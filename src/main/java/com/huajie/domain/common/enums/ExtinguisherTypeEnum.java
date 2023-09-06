package com.huajie.domain.common.enums;

public enum ExtinguisherTypeEnum {

    DeviceType01("waterBase","水基型"),
    DeviceType02("dryPowder","干粉"),
    DeviceType03("foam","泡沫"),
    DeviceType04("gas","气体");

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
