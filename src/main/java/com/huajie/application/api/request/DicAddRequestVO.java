package com.huajie.application.api.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author zhuxiaofeng
 * @date 2023/8/8
 */
@Data
public class DicAddRequestVO {

    @ApiModelProperty("字典code")
    @NotBlank(message = "字典code不能为空")
    @NotNull(message = "字典code不能为空")
    private String dicCode;

    @ApiModelProperty("字典名称")
    @NotBlank(message = "字典名称不能为空")
    @NotNull(message = "字典名称不能为空")
    private String dicName;

    @ApiModelProperty("描述")
    private String description;


}
