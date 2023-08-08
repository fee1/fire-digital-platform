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
public class LoginRequestVO {

    @ApiModelProperty("用户名")
    @NotBlank(message = "用户名不能为空")
    @NotNull(message = "用户名不能为空")
    private String username;

    @ApiModelProperty("密码")
    @NotBlank(message = "密码不能为空")
    @NotNull(message = "密码不能为空")
    private String password;

}
