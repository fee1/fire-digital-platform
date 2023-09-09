package com.huajie.application.api.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zhuxiaofeng
 * @date 2023/9/8
 */
@Data
public class TenantPayRecordResponseVO {

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("商家订单号")
    private String outTradeNo;

    @ApiModelProperty("支付宝订单号")
    private String tradeNo;

    @ApiModelProperty("交易状态：" +
            "WAIT_BUYER_PAY（交易创建，等待买家付款）、" +
            "TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）、" +
            "TRADE_SUCCESS（交易支付成功）、" +
            "TRADE_FINISHED（交易结束，不可退款）" +
            "SUCCESS：支付成功 " +
            "REFUND：转入退款 " +
            "NOTPAY：未支付 " +
            "CLOSED：已关闭")
    private String status;

    @ApiModelProperty("订单金额")
    private BigDecimal totalAmount;

    @ApiModelProperty("租户id")
    private Integer tenantId;

    @ApiModelProperty("支付账号")
    private String payAmount;

    @ApiModelProperty("支付渠道")
    private String payChannel;

    @ApiModelProperty("支付时间")
    private Date date;

    @ApiModelProperty("备注")
    private String remark;

}
