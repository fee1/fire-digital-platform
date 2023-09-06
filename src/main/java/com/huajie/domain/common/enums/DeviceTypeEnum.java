package com.huajie.domain.common.enums;

public enum DeviceTypeEnum {

    DeviceType01("DeviceType01","灭火器"),
    DeviceType02("DeviceType02","消火栓"),
    DeviceType03("DeviceType03","消防水带"),
    DeviceType04("DeviceType04","消防用电供配电及电气线路"),
    DeviceType05("DeviceType05","消防应急照明"),
    DeviceType06("DeviceType06","安全疏散及避难设施"),
    DeviceType07("DeviceType07","火灾自动报警器"),
    DeviceType08("DeviceType08","防火卷帘门窗"),
    DeviceType09("DeviceType09","防烟排烟系统"),
    DeviceType10("DeviceType10","其他");

    String code;

    String name;

    DeviceTypeEnum(String code,String name){
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
