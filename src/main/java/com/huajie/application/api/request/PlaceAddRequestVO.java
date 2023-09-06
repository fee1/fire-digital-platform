package com.huajie.application.api.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PlaceAddRequestVO {

    @ApiModelProperty("NFCCode")
    @NotNull(message = "NFC代码不能为空")
    @NotBlank(message = "NFC代码不能为空")
    String nfcCode;


    @ApiModelProperty("点位名称")
    @NotNull(message = "点位名称不能为空")
    @NotBlank(message = "点位名称不能为空")
    String placeName;

    @ApiModelProperty("点位地址")
    @NotNull(message = "点位地址不能为空")
    @NotBlank(message = "点位地址不能为空")
    String placeAddress;

    @ApiModelProperty("备注")
    String remark;


}
