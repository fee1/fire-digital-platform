package com.huajie.application.api.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author zhuxiaofeng
 * @date 2023/9/16
 */
@Data
public class AppRequestVO {

    @ApiModelProperty("前端自己获取的jsCode")
    @NotNull(message = "jsCode不能为空")
    @NotBlank(message = "jsCode不能为空")
    private String jsCode;

    @ApiModelProperty("openId")
    @NotNull(message = "openId不能为空")
    @NotBlank(message = "openId不能为空")
    private String openId;

}
