package com.huajie.application.api.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhuxiaofeng
 * @date 2023/9/3
 */
@Data
public class EnterpriseResponseVO {

    private Integer id;

    @ApiModelProperty("企业名称")
    private String enterpriseName;

    @ApiModelProperty("省")
    private String province;

    @ApiModelProperty("市")
    private String city;

    @ApiModelProperty("区/县")
    private String region;

    @ApiModelProperty("街道")
    private String street;

    @ApiModelProperty("企业性质")
    private String enterpriseType;

    @ApiModelProperty("行业类别")
    private String entIndustryClassification;

    @ApiModelProperty("消防安全管理类别")
    private String entFireType;

}
