package com.huajie.application.api;

import com.huajie.application.api.common.ApiResult;
import com.huajie.application.api.request.RoleDetailRequestVO;
import com.huajie.application.api.response.RoleDetailResponseVO;
import com.huajie.application.api.response.RoleResponseVO;
import com.huajie.application.service.RoleAppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zhuxiaofeng
 * @date 2023/8/7
 */
@Api("角色相关")
@RestController
@RequestMapping("role")
public class RoleApi {

    @Autowired
    private RoleAppService roleAppService;

    @ApiOperation("角色列表")
    @GetMapping("list")
    public ApiResult<List<RoleResponseVO>> getRoleList(){
        List<RoleResponseVO> responseVOList = roleAppService.getRoleList();
        return ApiResult.ok(responseVOList);
    }

    @ApiOperation("新增角色")
    @PostMapping("add")
    public ApiResult<Void> addRole(){
//        roleAppService.add();
        return ApiResult.ok();
    }

    @ApiOperation("查询角色详情")
    @GetMapping("detail")
    public ApiResult<RoleResponseVO> getRoleDetail(@RequestBody @Validated RoleDetailRequestVO requestVO){
        RoleResponseVO responseVO = roleAppService.getRoleDetail(requestVO.getId());
        return ApiResult.ok(responseVO);
    }

}
