package com.huajie.application.api;

import com.huajie.application.api.common.ApiPage;
import com.huajie.application.api.common.ApiResult;
import com.huajie.application.api.request.ChangePasswordRequestVO;
import com.huajie.application.api.request.LoginRequestVO;
import com.huajie.application.api.request.TenantUsersRequestVO;
import com.huajie.application.api.request.UserAddRequestVO;
import com.huajie.application.api.request.UserUpdateRequestVO;
import com.huajie.application.api.response.CurrentUserResponseVO;
import com.huajie.application.api.response.LoginResponseVO;
import com.huajie.application.api.response.UserDetailResponseVO;
import com.huajie.application.service.UserAppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.security.Principal;
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
    private UserAppService userAppService;

    @ApiOperation(value = "获取用户信息")
    @GetMapping("/getCurrentUserInfo")
    public ApiResult<CurrentUserResponseVO> getCurrentUser() {
        CurrentUserResponseVO currentUser = userAppService.getCurrentUser();
        return ApiResult.ok(currentUser);
    }

    @ApiOperation(value = "用户列表")
    @GetMapping("/tenant/users")
    public ApiResult<ApiPage<UserDetailResponseVO>> getTenantUsers(@ApiParam("当前页码")@RequestParam(required = false, defaultValue = "0") Integer pageNum,
                                                                   @ApiParam("每页数量")@RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                                   @ApiParam("用户账号")@RequestParam(required = false) String userNo,
                                                                   @ApiParam("手机号")@RequestParam(required = false) String phone,
                                                                   @ApiParam("用户名")@RequestParam(required = false) String userName){
        TenantUsersRequestVO tenantUsersRequestVO = new TenantUsersRequestVO();
        tenantUsersRequestVO.setPageNum(pageNum);
        tenantUsersRequestVO.setPageSize(pageSize);
        tenantUsersRequestVO.setPhone(phone);
        tenantUsersRequestVO.setUserName(userName);
        tenantUsersRequestVO.setUserNo(userNo);
        List<UserDetailResponseVO> responseVOS = userAppService.getTenantUsers(tenantUsersRequestVO);
        return ApiResult.ok(ApiPage.restPage(responseVOS));
    }

    @ApiOperation(value = "新增用户")
    @PostMapping("/tenant/user/add")
    public ApiResult<Void> addUser(@RequestBody @Validated UserAddRequestVO requestVO){
        userAppService.addUser(requestVO);
        return ApiResult.ok();
    }

    @ApiOperation(value = "用户信息修改")
    @PostMapping("/tenant/user/update")
    public ApiResult<Void> updateUser(@RequestBody @Validated UserUpdateRequestVO requestVO){
        userAppService.updateUser(requestVO);
        return ApiResult.ok();
    }

    @ApiOperation(value = "查询用户详情")
    @GetMapping("/tenant/user/detail")
    public ApiResult<UserDetailResponseVO> getUserDetail(@Valid @ApiParam("用户id")
                                                             @NotNull(message = "用户id不能为空")
                                                             @RequestParam Integer userId){
        UserDetailResponseVO userDetailResponseVO = userAppService.getUserDetail(userId);
        return ApiResult.ok(userDetailResponseVO);
    }

    @ApiOperation(value = "修改密码")
    @PostMapping("/tenant/user/change/password")
    public ApiResult<Void> changePassword(@RequestBody @Validated ChangePasswordRequestVO requestVO){
        userAppService.changePassword(requestVO);
        return ApiResult.ok();
    }


}
