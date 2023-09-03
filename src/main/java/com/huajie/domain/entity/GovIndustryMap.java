package com.huajie.domain.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class GovIndustryMap implements Serializable {
    private Integer id;

    private Integer tenantId;

    private String industryClassification;

    private static final long serialVersionUID = 1L;

}