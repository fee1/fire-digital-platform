package com.huajie.application.api.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhuxiaofeng
 * @date 2023/8/8
 */
@Data
public class DicRequestVO {

    @ApiModelProperty("字典code")
    private String dicCode;

    @ApiModelProperty("描述")
    private String description;

}
