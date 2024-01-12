package com.huajie.application.api.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ProblemQueryRequestVO {


    @ApiModelProperty(value = "entTenantId", name = "企业id",notes = "")
    private Integer entTenantId ;

    @ApiModelProperty(value = "enterpriseName", name = "企业名称（模糊查询）")
    private String enterpriseName;

    /** 隐患状态 submit;wait_reform */
    @ApiModelProperty(value = "state",name = "隐患状态",notes = "SUBMIT, TODO, TIMEOUT, DELAY_APPROVE, REFORM_APPROVE, FINISH")
    private String state ;

    /** 隐患状态 submit;wait_reform */
    @ApiModelProperty(value = "state",name = "隐患状态",notes = "SUBMIT, TODO, TIMEOUT, DELAY_APPROVE, REFORM_APPROVE, FINISH")
    private List<String> stateList ;

    /** 隐患类型 patrol 巡查，inspect 检查，selfcheck 综合自查, system 系统定时巡查 */
    @ApiModelProperty(value = "problemType  ", name = "隐患类型 patrol 巡查，inspect 检查，selfcheck 综合自查, system 系统定时巡查",notes = "")
    private String problemType ;

    @ApiModelProperty("点位id")
    private Integer placeId;

    @ApiModelProperty("点位名称 模糊查询")
    private String placeName;

    @ApiModelProperty("设备id")
    private Integer deviceId;

    @ApiModelProperty("设备名称 模糊查询")
    private String deviceName;



}
