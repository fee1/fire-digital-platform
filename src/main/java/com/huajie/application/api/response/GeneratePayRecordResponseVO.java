package com.huajie.application.api.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GeneratePayRecordResponseVO {

    @ApiModelProperty("订单号")
    private String orderId;

}
