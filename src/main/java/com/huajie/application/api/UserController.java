package com.huajie.application.api;

import com.huajie.application.api.common.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 获取用户信息
 *
 * @author zhuxiaofeng
 * @date 2023/7/11
 */
@Api("用户相关")
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private ConsumerTokenServices consumerTokenServices;

    @ApiOperation(value = "登出")
    @PostMapping(value = "logout")
    public ApiResult<Void> logout(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //清除认证
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
        String tokenValue = details.getTokenValue();
        consumerTokenServices.revokeToken(tokenValue);
        return ApiResult.ok();
    }

    @ApiOperation(value = "获取用户信息")
    @GetMapping("/getCurrentUserInfo")
    public Object getCurrentUser(Authentication authentication) {
        return authentication.getPrincipal();
    }

}
