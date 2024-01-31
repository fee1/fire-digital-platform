package com.huajie.domain.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author zhuxiaofeng
 * @date 2023/9/6
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnterpriseRegiestDTO {

    @ApiModelProperty("alipay二维码图片地址")
    private String alipayQrcodeUrl;

    @ApiModelProperty("alipay订单号")
    private String alipayOrderId;

    @ApiModelProperty("wechatPay二维码图片地址")
    private String wechatPayQrcodeUrl;

    @ApiModelProperty("wechatPay订单号")
    private String wechatPayOrderId;

    @ApiModelProperty("金额")
    private BigDecimal amount;

}
