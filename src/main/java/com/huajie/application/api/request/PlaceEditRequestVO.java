package com.huajie.application.api.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PlaceEditRequestVO {

    @ApiModelProperty("点位id")
    @NotNull(message = "点位id不能为空")
    Integer id;

    @ApiModelProperty("点位名称")
    String placeName;

    @ApiModelProperty("点位地址")
    String placeAddress;

    @ApiModelProperty("备注")
    String remark;

    @ApiModelProperty("版本号")
    @NotNull(message = "版本号不能为空")
    Integer version;

}
