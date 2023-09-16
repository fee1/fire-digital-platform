package com.huajie.application.api.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class PatrolQueryRequestVO {

    @ApiModelProperty("点位id")
    Integer placeId;

    @ApiModelProperty("点位名称 模糊查询")
    String placeName;

    @ApiModelProperty("设备id")
    Integer deviceId;

    @ApiModelProperty("设备名称 模糊查询")
    String deviceName;

    @ApiModelProperty("开始日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDateTime startDate;

    @ApiModelProperty("截止日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDateTime endDate;


}
