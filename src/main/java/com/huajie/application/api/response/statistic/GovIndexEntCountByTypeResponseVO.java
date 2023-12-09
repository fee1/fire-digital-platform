package com.huajie.application.api.response.statistic;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GovIndexEntCountByTypeResponseVO {

    @ApiModelProperty("总用户")
    private int totalCount;

    @ApiModelProperty("机关团体事业单位数量")
    private int companyCount;

    @ApiModelProperty("企业、场所数量")
    private int enterpriseCount;

    @ApiModelProperty("商户")
    private int merchantCount;

    @ApiModelProperty("出租屋")
    private int rentalHouseCount;

}
