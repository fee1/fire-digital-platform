package com.huajie.application.api;

import com.huajie.application.api.common.ApiResult;
import com.huajie.application.api.request.LoginRequestVO;
import com.huajie.application.api.response.LoginResponseVO;
import com.huajie.application.service.LoginAppService;
import com.huajie.domain.common.utils.Base64Util;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhuxiaofeng
 * @date 2023/8/8
 */
@Api(tags = "登录相关")
@RestController
@RequestMapping("oauth")
public class LoginApi {

    @Autowired
    private LoginAppService loginAppService;

    @ApiOperation("登录")
    @PostMapping(value = "login")
    public ApiResult<LoginResponseVO> login(@RequestBody @Validated LoginRequestVO requestVO, HttpServletRequest request){
        String headerValue = request.getHeader("Authorization");
        if (StringUtils.isNotBlank(headerValue)) {
            String[] clientIdAndSecret = Base64Util.decrypt(headerValue.split(" ")[1]).split(":");
            String clientId = clientIdAndSecret[0];
            String secret = clientIdAndSecret[1];
            LoginResponseVO loginResponseVO = loginAppService.login(clientId, secret, requestVO);
            return ApiResult.ok(loginResponseVO);
        }
        return ApiResult.failed("Authorization failed");
    }

    @ApiOperation(value = "登出")
    @PostMapping(value = "logout")
    public ApiResult<Void> logout(){
        loginAppService.logout();
        return ApiResult.ok();
    }

}
