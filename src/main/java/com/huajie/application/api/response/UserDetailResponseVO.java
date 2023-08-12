package com.huajie.application.api.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhuxiaofeng
 * @date 2023/8/7
 */
@Data
public class UserDetailResponseVO {

    private Integer id;

    @ApiModelProperty("用户编号")
    private String userNo;

    @ApiModelProperty("用户名称")
    private String userName;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("用户邮箱")
    private String email;

    @ApiModelProperty("角色id")
    private Integer roleId;

    @ApiModelProperty("角色名称")
    private String roleName;

}
