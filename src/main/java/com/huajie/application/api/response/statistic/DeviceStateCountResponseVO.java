package com.huajie.application.api.response.statistic;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DeviceStateCountResponseVO {

    @ApiModelProperty("设备类型代码")
    private String deviceType;

    @ApiModelProperty("设备类型名称")
    private String deviceTypeName;

    @ApiModelProperty("正常数量")
    private String normalCount;

    @ApiModelProperty("异常数量")
    private String abnormalCount;

    @ApiModelProperty("过期需报废数量")
    private String expiredCount;


}
