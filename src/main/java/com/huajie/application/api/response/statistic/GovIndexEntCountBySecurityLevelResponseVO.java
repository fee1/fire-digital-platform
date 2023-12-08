package com.huajie.application.api.response.statistic;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GovIndexEntCountBySecurityLevelResponseVO {


    @ApiModelProperty("安全等级低-企业数量统计")
    private GovIndexEntCountByTypeResponseVO lowLevelEnterpriseStatistic;

    @ApiModelProperty("安全等级中-企业数量统计")
    private GovIndexEntCountByTypeResponseVO middleLevelEnterpriseStatistic;

    @ApiModelProperty("安全等级高-企业数量统计")
    private GovIndexEntCountByTypeResponseVO highLevelEnterpriseStatistic;

}
