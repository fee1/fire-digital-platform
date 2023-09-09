package com.huajie.infrastructure.external.pay.model;

import lombok.Data;

import java.util.Date;

/**
 * @author zhuxiaofeng
 * @date 2023/9/8
 */
@Data
public class WechatPayCheckRespModel {

    /**
     * 应用ID
     */
    private String appId;

    /**
     * 直连商户号
     */
    private String mchId;

    /**
     * 商户订单号
     */
    private String outTradeNo;

    /**
     * 微信支付订单号
     */
    private String transactionId;

    /**
     * 交易状态
     */
    private String tradeState;

    /**
     * 交易状态描述
     */
    private String tradeStateDesc;

    /**
     * 支付完成时间 string 2018-06-08T10:34:56+08:00
     */
    private Date successTime;

    /**
     * 支付者
     */
    private PayerModel payer;

}
