package com.huajie.application.api.response.statistic;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EntCloudCheckResponseVO {

    @ApiModelProperty("企业消防安全等级")
    private String securityLevel;

}
