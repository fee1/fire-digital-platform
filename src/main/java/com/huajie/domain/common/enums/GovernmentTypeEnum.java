package com.huajie.domain.common.enums;

/**
 * 企业消防安全类型
 */
public enum GovernmentTypeEnum {

    SubLeader("SubLeader","分管领导"),
    IndustrySector("IndustrySector","行业部门"),
    FireDepartment("FireDepartment","消防救援机构"),
    FireBrigade("FireBrigade","安全生产管理、消防救援队"),
    PoliceStation("PoliceStation","公安派出所"),
    MarketAdmin("MarketAdmin","市场管理部门"),
    IndustrialAdmin("IndustrialAdmin","工业企业管理部门"),
    Other("Other","其他");

    String code;

    String name;

    GovernmentTypeEnum(String code, String name){
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
