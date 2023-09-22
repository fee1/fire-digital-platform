package com.huajie.application.api.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PlaceInspectRecordResponseVO {


    private Integer id;

    @ApiModelProperty("点位名称")
    private String placeName;

    @ApiModelProperty("点位地址")
    private String placeAddress;

    @ApiModelProperty(name = "设备数量")
    private long deviceCount ;

    @ApiModelProperty(name = "已检查设备数量")
    private long inspectDeviceCount ;

    @ApiModelProperty("设备列表")
    private List<DeviceInspectRecordResponseVO> deviceList ;


}
