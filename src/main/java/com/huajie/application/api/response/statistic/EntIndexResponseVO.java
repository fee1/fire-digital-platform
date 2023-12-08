package com.huajie.application.api.response.statistic;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class EntIndexResponseVO {

    @ApiModelProperty("企业名称")
    private String enterpriseName;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("已解决隐患数量")
    private int finishProblemCount;

    @ApiModelProperty("待整改隐患数量")
    private int todoProblemCount;

    @ApiModelProperty("新增待整改隐患数量")
    private int newTodoProblemCount;

    @ApiModelProperty("超时隐患数量")
    private int timeoutProblemCount;

    @ApiModelProperty("新增超时隐患数量")
    private int newTimeoutProblemCount;

    @ApiModelProperty("已检查点位数")
    private int inspectPlaceCount;

    @ApiModelProperty("今日检查点位数")
    private int todayInspectPlaceCount;

    @ApiModelProperty("总采集点位数")
    private int totalPlaceCount;

    @ApiModelProperty("新增点位数")
    private int newPlaceCount;

    @ApiModelProperty("总采集设备数")
    private int totalDeviceCount;

    @ApiModelProperty("新增设备数")
    private int newDeviceCount;

    @ApiModelProperty("设备有效率")
    private BigDecimal deviceEffectiveRate;

}
