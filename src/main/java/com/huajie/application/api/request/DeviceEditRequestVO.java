package com.huajie.application.api.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class DeviceEditRequestVO {

    @NotNull(message = "id不能为空")
    private Integer id;

    @ApiModelProperty("设备名称")
    private String deviceName;

    @ApiModelProperty("设备编号")
    private String deviceNo;

    @ApiModelProperty("最近一次使用日期")
    private Date lastUseDate;

    @ApiModelProperty("最近一次充装日期")
    private Date lastReplaceDate;

    @ApiModelProperty("备注")
    String remark;

    @ApiModelProperty("版本号")
    @NotNull(message = "版本号不能为空")
    private Integer version;
}
