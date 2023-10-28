package com.huajie.application.api.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author zhuxiaofeng
 * @date 2023/10/24
 */
@Data
public class EnterpriseUserInfoResponseVO {

    @ApiModelProperty("用户编号")
    private String userNo;

    @ApiModelProperty("用户名称")
    private String userName;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("用户邮箱")
    private String email;

    @ApiModelProperty("分配的角色id")
    private String roleName;

}
