package com.huajie.domain.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Tenant implements Serializable {
    private Integer id;

    private String tenantName;

    private String tenantType;

    private Short status;

    private Date effectiveEndDate;

    private String province;

    private String city;

    private String region;

    private String street;

    private String address;

    private String enterpriseType;

    private String entIndustryClassification;

    private String entFireType;

    private String governmentType;

    private String govIndustrySector;

    private Short approveStatus;

    private Integer approveUser;

    private Date approveDate;

    private Date createTime;

    private String createUser;

    private Date updateTime;

    private String updateUser;

    private static final long serialVersionUID = 1L;

}