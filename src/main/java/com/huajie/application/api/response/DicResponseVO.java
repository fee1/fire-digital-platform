package com.huajie.application.api.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhuxiaofeng
 * @date 2023/8/8
 */
@Data
public class DicResponseVO {

    private Integer id;

    @ApiModelProperty("字典code")
    private String dicCode;

    @ApiModelProperty("字典名称")
    private String dicName;

    @ApiModelProperty("描述")
    private String description;

}
