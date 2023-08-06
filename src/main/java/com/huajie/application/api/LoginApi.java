package com.huajie.application.api;

import com.huajie.application.api.common.ApiResult;
import com.huajie.application.service.LoginAppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhuxiaofeng
 * @date 2023/8/6
 */
@Api(description = "登录相关")
@RestController
@RequestMapping("user")
public class LoginApi {

    @Autowired
    private LoginAppService loginAppService;

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

}
