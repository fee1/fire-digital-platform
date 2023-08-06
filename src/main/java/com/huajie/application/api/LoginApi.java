package com.huajie.application.api;

import com.huajie.application.service.LoginAppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @ApiOperation(value = "登出")
    @PostMapping(value = "logout")
    public void logout(){
    }

}
