package com.huajie.application.api.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author zhuxiaofeng
 * @date 2023/9/10
 */
@Data
public class GeneratePayQrcodeImageRequestVO {

    @ApiModelProperty("订单号")
    @NotBlank(message = "订单号 不能为空")
    @NotNull(message = "订单号 不能为空")
    private String outTradeNo;

    @ApiModelProperty("渠道 alipay: 支付宝; wechat_pay: 微信 ")
    @NotBlank(message = "渠道 不能为空")
    @NotNull(message = "渠道 不能为空")
    private String channel;

}
