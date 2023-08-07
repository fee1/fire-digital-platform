package com.huajie.application.api;

import com.huajie.application.api.common.ApiResult;
import com.huajie.application.api.request.UserAddRequestVO;
import com.huajie.application.api.request.UserUpdateRequestVO;
import com.huajie.application.api.response.UserDetailResponseVO;
import com.huajie.application.service.UserAppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
public class UserApi {

    @Autowired
    private ConsumerTokenServices consumerTokenServices;

    @Autowired
    private UserAppService userAppService;

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
    public ApiResult<Object> getCurrentUser(Authentication authentication) {
        return ApiResult.ok(authentication.getPrincipal());
    }

    @ApiOperation(value = "获取当前租户下的用户")
    @GetMapping("/tenant/users")
    public ApiResult<List<UserDetailResponseVO>> getTenantUsers(){
        List<UserDetailResponseVO> responseVOS = userAppService.getTenantUsers();
        return ApiResult.ok(responseVOS);
    }

    @ApiOperation(value = "为当前租户新增用户")
    @PostMapping("/tenant/user/add")
    public ApiResult<Void> addUser(@RequestBody @Validated UserAddRequestVO requestVO){
        userAppService.addUser(requestVO);
        return ApiResult.ok();
    }

    @ApiOperation(value = "修改选中用户信息")
    @PostMapping("/tenant/user/update")
    public ApiResult<Void> updateUser(@RequestBody @Validated UserUpdateRequestVO requestVO){
        userAppService.updateUser(requestVO);
        return ApiResult.ok();
    }

}
