package com.huajie.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
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

    private String wechatPayQrcodeUrl;

    private String alipayQrcodeUrl;

    private Date date;

    private String remark;

    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "create_user",fill = FieldFill.INSERT)
    private String createUser;

    @TableField(value = "update_time",fill = FieldFill.UPDATE)
    private Date updateTime;

    @TableField(value = "update_user",fill = FieldFill.UPDATE)
    private String updateUser;

    private static final long serialVersionUID = 1L;


}