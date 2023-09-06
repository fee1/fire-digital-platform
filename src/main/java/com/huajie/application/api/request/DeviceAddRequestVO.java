package com.huajie.application.api.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;


@Data
public class DeviceAddRequestVO {

    @ApiModelProperty("设备类型 词典")
    @NotNull(message = "设备类型不能为空")
    @NotBlank(message = "设备类型不能为空")
    private String deviceType;

    @ApiModelProperty("设备名称")
    @NotNull(message = "设备名称不能为空")
    @NotBlank(message = "设备名称不能为空")
    private String deviceName;

    @ApiModelProperty("设备编号")
    private String deviceNo;

    @ApiModelProperty("出厂日期")
    private Date productionDate;

    @ApiModelProperty("最近一次使用日期")
    private Date lastUseDate;

    @ApiModelProperty("最近一次充装日期")
    private Date lastReplaceDate;

    @ApiModelProperty("灭火器类型")
    private String extinguisherType;

    @ApiModelProperty("电源类型")
    private String powerType;

    @ApiModelProperty("备注")
    private String remark;



}
