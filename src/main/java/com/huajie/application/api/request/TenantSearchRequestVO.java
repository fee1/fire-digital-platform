package com.huajie.application.api.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author zhuxiaofeng
 * @date 2023/12/5
 */
@Data
public class TenantSearchRequestVO {

    @ApiModelProperty("搜索  1: 企业  2: 政府")
    @NotNull
    private Integer searchType;

    @ApiModelProperty("区/县地址")
    private Integer regionId;

    @ApiModelProperty("街道地址")
    private Integer streetId;

    @ApiModelProperty("企业/政府名称（模糊搜索）")
    private String tenantName;

    @ApiModelProperty("企业性质 字典value code")
    private String enterpriseType;

}
