package com.huajie.domain.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class TenantPayRecord implements Serializable {
    private Integer id;

    private String outTradeNo;

    private String tradeNo;

    private String status;

    private BigDecimal totalAmount;

    private Integer tenantId;

    private String payAmount;

    private String payChannel;

    private Date date;

    private String remark;

    private Date createTime;

    private String createUser;

    private Date updateTime;

    private String updateUser;

    private static final long serialVersionUID = 1L;


}