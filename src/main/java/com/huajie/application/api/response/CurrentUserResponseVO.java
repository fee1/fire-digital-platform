package com.huajie.application.api.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author zhuxiaofeng
 * @date 2023/8/7
 */
@Data
public class CurrentUserResponseVO {

    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("用户权限、租户、菜单、方法等信息")
    private Collection<GrantedAuthority> authorities;
    @ApiModelProperty("账户是否过期")
    private boolean accountNonExpired;
    @ApiModelProperty("账户是否锁定")
    private boolean accountNonLocked;
    @ApiModelProperty("凭证是否过期")
    private boolean credentialsNonExpired;
    @ApiModelProperty("账户是否可用")
    private boolean enabled;
}
