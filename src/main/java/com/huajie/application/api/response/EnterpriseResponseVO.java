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
    private Integer provinceId;

    @ApiModelProperty("省名称")
    private String provinceName;

    @ApiModelProperty("市")
    private Integer cityId;

    @ApiModelProperty("市名称")
    private String cityName;

    @ApiModelProperty("区/县")
    private Integer regionId;

    @ApiModelProperty("区/县名称")
    private String regionName;

    @ApiModelProperty("街道")
    private Integer streetId;

    @ApiModelProperty("街道名称")
    private String streetName;


    @ApiModelProperty("企业性质")
    private String enterpriseType;

    @ApiModelProperty("行业类别")
    private String entIndustryClassification;

    @ApiModelProperty("消防安全管理类别")
    private String entFireType;

}
