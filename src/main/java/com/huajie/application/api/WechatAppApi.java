package com.huajie.application.api;

import com.huajie.application.api.common.ApiResult;
import com.huajie.application.api.common.ResultCode;
import com.huajie.application.api.request.AppRequestVO;
import com.huajie.application.api.request.PhoneBindingRequestVO;
import com.huajie.application.api.request.UsrLoginRequestVO;
import com.huajie.application.api.request.WechatEditUserInfoRequestVO;
import com.huajie.application.api.response.WechatAppLoginResponseVO;
import com.huajie.application.api.response.WechatGetPhoneResponseVO;
import com.huajie.application.api.response.WechatUserManagementResponseVO;
import com.huajie.application.service.WechatAppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author zhuxiaofeng
 * @date 2023/9/16
 */
@Api(tags = "微信相关")
@RestController
@RequestMapping("wechat")
public class WechatAppApi {

    @Autowired
    private WechatAppService wechatAppService;

    @ApiOperation("微信用户登录")
    @PostMapping("app/login")
    public ApiResult<WechatAppLoginResponseVO> appLogin(@Valid @RequestBody AppRequestVO requestVO){
        WechatAppLoginResponseVO responseVO = wechatAppService.appLogin(requestVO.getJsCode());
        if (StringUtils.isBlank(responseVO.getAccessToken())){
            return ApiResult.failed("登录失败，请先绑定手机号或传递正确的code", ResultCode.WECHAT_FAIL_LOGIN, responseVO);
        }
        return ApiResult.ok(responseVO);
    }

    @ApiOperation("微信手机号绑定账号")
    @PostMapping("user/phone/binding")
    public ApiResult<WechatGetPhoneResponseVO> userPhoneBinding(@Valid @RequestBody PhoneBindingRequestVO requestVO){
        WechatGetPhoneResponseVO wechatGetPhoneResponseVO = wechatAppService.userPhoneBinding(requestVO.getJsCode(), requestVO.getOpenId());
        return ApiResult.ok(wechatGetPhoneResponseVO);
    }

    @ApiOperation("编辑资料")
    @PostMapping("edit/user/info")
    public ApiResult<Void> editUserInfo(@Valid@RequestBody WechatEditUserInfoRequestVO requestVO){
        wechatAppService.editUserInfo(requestVO);
        return ApiResult.ok();
    }

    @ApiOperation("用户管理")
    @PostMapping("user/management")
    public ApiResult<List<WechatUserManagementResponseVO>> userManagement(){
        List<WechatUserManagementResponseVO> responseVOList = this.wechatAppService.userManagement();
        return ApiResult.ok(responseVOList);
    }

    @ApiOperation("微信用户名密码登陆")
    @PostMapping("app/usr/login")
    public ApiResult<WechatAppLoginResponseVO> login(@Valid @RequestBody UsrLoginRequestVO requestVO){
        WechatAppLoginResponseVO responseVO = this.wechatAppService.login(requestVO);
        return ApiResult.ok(responseVO);
    }

    @ApiOperation("微信用户名密码登出")
    @PostMapping("app/usr/logout")
    public ApiResult<WechatAppLoginResponseVO> logout(){
        this.wechatAppService.logout();
        return ApiResult.ok();
    }

}
