package com.huajie.domain.common.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum ProblemActionTypeEnum {

    SIGN("SIGN","签收",
            Arrays.asList(ProblemStateEnum.SUBMIT),
            ProblemStateEnum.TODO),
    REPLY("REPLY","回复整改意见",
            Arrays.asList(ProblemStateEnum.SUBMIT,ProblemStateEnum.TODO,ProblemStateEnum.TIMEOUT),
            ProblemStateEnum.TODO),
    TIMEOUT("TIMEOUT","超时",
            Arrays.asList(ProblemStateEnum.TODO),
            ProblemStateEnum.TIMEOUT),
    URGE("URGE","催促整改",
            Arrays.asList(ProblemStateEnum.TIMEOUT),
            ProblemStateEnum.TIMEOUT),
    REFORM("REFORM","整改",
            Arrays.asList(ProblemStateEnum.TODO,ProblemStateEnum.TIMEOUT),
            ProblemStateEnum.REFORM_APPROVE),
    DELAY("DELAY","延迟整改",
            Arrays.asList(ProblemStateEnum.TODO,ProblemStateEnum.TIMEOUT),
            ProblemStateEnum.DELAY_APPROVE),
    DELAY_APPROVE_PASS("DELAY_APPROVE_PASS","延迟整改审批通过",
            Arrays.asList(ProblemStateEnum.DELAY_APPROVE),
            ProblemStateEnum.TODO),
    DELAY_APPROVE_REJECT("DELAY_APPROVE_REJECT","延迟整改审批拒绝",
            Arrays.asList(ProblemStateEnum.DELAY_APPROVE),
            ProblemStateEnum.TODO),
    REFORM_APPROVE_PASS("DELAY_APPROVE_PASS","整改审批通过",
            Arrays.asList(ProblemStateEnum.REFORM_APPROVE),
            ProblemStateEnum.FINISH),
    REFORM_APPROVE_REJECT("DELAY_APPROVE_REJECT","整改审批拒绝",
            Arrays.asList(ProblemStateEnum.REFORM_APPROVE),
            ProblemStateEnum.TODO),
    ;

    ProblemActionTypeEnum(String actionType, String actionName, List<ProblemStateEnum> fromStates, ProblemStateEnum toStates) {
        this.actionType = actionType;
        this.actionName = actionName;
        this.fromStates = fromStates;
        this.toStates = toStates;


    }

    private String actionType;

    private String actionName;

    private List<ProblemStateEnum> fromStates;

    private ProblemStateEnum toStates;





}
