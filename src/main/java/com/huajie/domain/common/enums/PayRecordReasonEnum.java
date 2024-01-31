package com.huajie.domain.common.enums;

import lombok.Getter;

@Getter
public enum  PayRecordReasonEnum {

    ENTERPRISE_USER_REGIST("企业用户注册"),
    ENTERPRISE_USER_RENEWAL("企业用户续费"),
    ENTERPRISE_USER_FAILED_TO_PAY_OVER_TIME("企业用户超时未支付");

    private String description;

    PayRecordReasonEnum(String description){
        this.description = description;
    }

}
