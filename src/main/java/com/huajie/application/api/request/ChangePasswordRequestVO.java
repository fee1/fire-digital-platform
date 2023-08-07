package com.huajie.application.api.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author zhuxiaofeng
 * @date 2023/8/7
 */
@Data
public class ChangePasswordRequestVO {

    @NotNull(message = "用户id不能为空")
    @ApiModelProperty("用户id")
    private Integer userId;

    @NotNull(message = "用户密码不能为空")
    @NotBlank(message = "用户密码不能为空")
    @ApiModelProperty("密码")
    private String password;

}
