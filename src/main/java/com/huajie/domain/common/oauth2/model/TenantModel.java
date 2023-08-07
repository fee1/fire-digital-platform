package com.huajie.domain.common.oauth2.model;

import lombok.Data;

import java.util.Date;

/**
 * @author zhuxiaofeng
 * @date 2023/8/6
 */
@Data
public class TenantModel {

    private Integer id;

    private String tenantName;

    private String tenantType;

    private Short status;

    private Date effectiveEndDate;

}
