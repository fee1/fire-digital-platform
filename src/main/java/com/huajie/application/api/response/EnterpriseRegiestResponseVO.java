package com.huajie.application.api.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zhuxiaofeng
 * @date 2023/9/5
 */
@Data
public class EnterpriseRegiestResponseVO {

    @ApiModelProperty("订单号")
    private String orderId;


}
