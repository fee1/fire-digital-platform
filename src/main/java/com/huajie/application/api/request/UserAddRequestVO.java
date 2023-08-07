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
public class UserAddRequestVO {

    //todo 确认用户账号和编号
    @ApiModelProperty("用户编号")
    @NotNull(message = "用户编号不能为空")
    @NotBlank(message = "用户编号不能为空")
    private String userNo;

    @ApiModelProperty("用户名称")
    @NotNull(message = "用户名称不能为空")
    @NotBlank(message = "用户名称不能为空")
    private String userName;

    @ApiModelProperty("用户密码")
    @NotNull(message = "用户密码不能为空")
    @NotBlank(message = "用户密码不能为空")
    private String password;

    @ApiModelProperty("手机号")
    @NotNull(message = "手机号不能为空")
    @NotBlank(message = "手机号不能为空")
    private String phone;

    @ApiModelProperty("用户邮箱")
    @NotNull(message = "用户邮箱不能为空")
    @NotBlank(message = "用户邮箱不能为空")
    private String email;

    @ApiModelProperty("分配的角色id")
    @NotNull(message = "分配的角色id不能位空")
    private String roleId;

}
