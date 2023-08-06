package com.huajie.application.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 获取用户信息
 *
 * @author zhuxiaofeng
 * @date 2023/7/11
 */
@Api(description = "用户相关")
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @ApiOperation(value = "获取用户信息")
    @GetMapping("/getCurrentUserInfo")
    public Object getCurrentUser(Authentication authentication) {
        return authentication.getPrincipal();
    }

}
