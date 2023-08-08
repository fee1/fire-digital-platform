package com.huajie.domain.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author zhuxiaofeng
 * @date 2023/8/8
 */
@Data
public class DicValueAddDTO {

    @ApiModelProperty("字典列值code")
    @NotNull(message = "字典列值code不能为空")
    @NotBlank(message = "字典列值code不能为空")
    private String dicCode;

    @ApiModelProperty("自定义序号，排序用")
    @NotNull(message = "自定义序号不能为空")
    private Integer valueNo;

    @ApiModelProperty("值列表代码全局唯一")
    @NotNull(message = "值列表代码不能为空")
    @NotBlank(message = "值列表代码不能为空")
    private String valueCode;

    @ApiModelProperty("值名称")
    @NotNull(message = "值名称不能为空")
    @NotBlank(message = "值名称不能为空")
    private String valueName;

}
