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
    private String appid;

    /**
     * 直连商户号
     */
    private String mchid;

    /**
     * 商户订单号
     */
    private String out_trade_no;

    /**
     * 微信支付订单号
     */
    private String transaction_id;

    /**
     * 交易状态
     */
    private String trade_state;

    /**
     * 交易状态描述
     */
    private String trade_state_desc;

    /**
     * 支付完成时间 string 2018-06-08T10:34:56+08:00
     */
    private Date success_time;

    /**
     * 支付者
     */
    private PayerModel payer;

}
