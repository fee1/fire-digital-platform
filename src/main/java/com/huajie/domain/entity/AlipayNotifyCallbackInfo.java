package com.huajie.domain.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class AlipayNotifyCallbackInfo implements Serializable {
    private Integer id;

    private String outTradeNo;

    private String subject;

    private BigDecimal totalAmount;

    private String sellerEmail;

    private String sellerId;

    private String buyerLogonId;

    private String buyerId;

    private String tradeNo;

    private BigDecimal invoiceAmount;

    private BigDecimal buyerPayAmount;

    private BigDecimal receiptAmount;

    private String tradeStatus;

    private String fundBillList;

    private Date gmtCreate;

    private Date gmtPayment;

    private Date notifyTime;

    private String notifyId;

    private String notifyType;

    private String appId;

    private String authAppId;

    private BigDecimal pointAmount;

    private String sign;

    private String signType;

    private String charset;

    private String version;

    private Date createTime;

    private Date updateTime;

    private String requestOriginal;

    private static final long serialVersionUID = 1L;

}