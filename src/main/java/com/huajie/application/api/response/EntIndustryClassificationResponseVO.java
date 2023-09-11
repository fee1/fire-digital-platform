package com.huajie.application.api.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhuxiaofeng
 * @date 2023/9/10
 */
@Data
public class EntIndustryClassificationResponseVO {

    @ApiModelProperty("名称")
    private String valueName;

    @ApiModelProperty("code")
    private String valueCode;

}
