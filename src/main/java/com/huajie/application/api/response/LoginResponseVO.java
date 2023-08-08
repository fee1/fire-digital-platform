package com.huajie.application.api.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Set;

/**
 * @author zhuxiaofeng
 * @date 2023/8/8
 */
@Data
public class LoginResponseVO {

    @ApiModelProperty("认证令牌")
    private String accessToken;

    private String tokenType;

    @ApiModelProperty("有效时间")
    private Integer expiresIn;

    private Set<String> scope;

}
