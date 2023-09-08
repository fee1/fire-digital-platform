package com.huajie.domain.common.constants;

/**
 * @author zhuxiaofeng
 * @date 2023/9/7
 */
public class PayRecordStatusConstants {

    /**
     * 未付款交易超时关闭，或支付完成后全额退款
     */
    public static final String WAIT_BUYER_PAY = "WAIT_BUYER_PAY";

    /**
     * 未付款交易超时关闭，或支付完成后全额退款
     */
    public static final String TRADE_CLOSED = "TRADE_CLOSED";

    /**
     * 交易支付成功
     */
    public static final String TRADE_SUCCESS = "TRADE_SUCCESS";

    /**
     * 交易结束，不可退款
     */
    public static final String TRADE_FINISHED = "TRADE_FINISHED";
}
