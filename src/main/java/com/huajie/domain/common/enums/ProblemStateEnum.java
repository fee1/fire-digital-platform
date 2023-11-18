package com.huajie.domain.common.enums;

import lombok.Getter;

@Getter
public enum ProblemStateEnum {

    SUBMIT("SUBMIT","已提交"),
    TODO("TODO","待整改"),
    TIMEOUT("TIMEOUT","超时未整改"),
    DELAY_APPROVE("DELAY_APPROVE","延迟整改审批中"),
    REFORM_APPROVE("REFORM_APPROVE","整改审批中"),
    FINISH("FINISH","已解决");

    ProblemStateEnum(String stateCode, String stateName) {
        this.stateCode = stateCode;
        this.stateName = stateName;
    }

    private String stateCode;

    private String stateName;



}
