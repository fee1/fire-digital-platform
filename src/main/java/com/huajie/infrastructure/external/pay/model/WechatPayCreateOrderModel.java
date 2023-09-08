package com.huajie.infrastructure.external.pay.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author zhuxiaofeng
 * @date 2023/9/8
 */
@Data
public class WechatPayCreateOrderModel {

    /**
     * 应用ID
     */
    @JSONField(name = "appid")
    private String appId;

    /**
     * 直连商户号
     */
    @JSONField(name = "mchid")
    private String mchId;

    /**
     * 商品描述
     */
    @JSONField(name = "description")
    private String description;

    /**
     * 商户订单号
     */
    @JSONField(name = "out_trade_no")
    private String outTradeNo;

    /**
     * 通知地址
     */
    @JSONField(name = "notify_url")
    private String notifyUrl;

    /**
     * 订单金额信息
     */
    @JSONField(name = "amount")
    private AmountModel amount;

}
