package com.huajie.domain.common.constants;

/**
 * @author zhuxiaofeng
 * @date 2023/9/7
 */
public class PayRecordStatusConstants {

    /**
     * 未付款交易超时关闭，或支付完成后全额退款
     */
    public static final String ALIPAY_WAIT_BUYER_PAY = "WAIT_BUYER_PAY";

    /**
     * 未付款交易超时关闭，或支付完成后全额退款
     */
    public static final String ALIPAY_TRADE_CLOSED = "TRADE_CLOSED";

    /**
     * 交易支付成功
     */
    public static final String ALIPAY_TRADE_SUCCESS = "TRADE_SUCCESS";

    /**
     * 交易结束，不可退款
     */
    public static final String ALIPAY_TRADE_FINISHED = "TRADE_FINISHED";

    /**
     * 支付成功
     */
    public static final String WECHAT_PAY_SUCCESS = "SUCCESS";

    /**
     * 转入退款
     */
    public static final String WECHAT_PAY_REFUND = "REFUND";

    /**
     * 未支付
     */
    public static final String WECHAT_PAY_NOTPAY = "NOTPAY";

    /**
     * 已关闭
     */
    public static final String WECHAT_PAY_CLOSED = "CLOSED";

}
