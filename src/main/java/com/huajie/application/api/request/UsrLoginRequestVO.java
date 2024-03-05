package com.huajie.application.api.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UsrLoginRequestVO {

    @ApiModelProperty("手机号")
    @NotBlank(message = "手机号不能为空")
    private String phone;

    @ApiModelProperty("密码")
    @NotBlank(message = "密码不能为空")
    private String password;

    @ApiModelProperty
    @NotBlank(message = "openId 不能为空")
    private String openId;

    @ApiModelProperty
    @NotBlank(message = "jscode 不能为空")
    private String jscode;

}
