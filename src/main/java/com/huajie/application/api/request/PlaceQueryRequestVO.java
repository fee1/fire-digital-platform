package com.huajie.application.api.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PlaceQueryRequestVO {

    @ApiModelProperty("点位id")
    Integer placeId;

    @ApiModelProperty("点位名称 模糊查询")
    String placeName;

    @ApiModelProperty("点位地址 模糊查询")
    String placeAddress;
}
