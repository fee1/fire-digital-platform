package com.huajie.application.api.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class DeviceInspectRecordResponseVO {


    private Integer id;

    @ApiModelProperty("设备类型 代码")
    private String deviceType;

    @ApiModelProperty("设备类型描述")
    private String deviceTypeDesc;

    @ApiModelProperty("设备名称")
    private String deviceName;

    @ApiModelProperty("设备编号")
    private String deviceNo;

    @ApiModelProperty("出厂日期")
    private Date productionDate;

    @ApiModelProperty("检查次数")
    private long inspectCount ;

    @ApiModelProperty("设备检查记录明细")
    private List<InspectDetailResponseVO> inspectDetailResponseVOS;


}
