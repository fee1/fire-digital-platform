package com.huajie.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Tenant implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String tenantName;

    private String tenantType;

    private Short status;

    private Date effectiveEndDate;

    private Integer province;

    private Integer city;

    private Integer region;

    private Integer street;

    private String address;

    private String enterpriseType;

    private String entIndustryClassification;

    private String entFireType;

    private String governmentType;

    private String govIndustrySector;

    private Short approveStatus;

    private Integer approveUser;

    private Date approveDate;

    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "create_user",fill = FieldFill.INSERT)
    private String createUser;

    @TableField(value = "update_time",fill = FieldFill.UPDATE)
    private Date updateTime;;

    @TableField(value = "update_user",fill = FieldFill.UPDATE)
    private String updateUser;

    private String securityLevel;

    private static final long serialVersionUID = 1L;

}