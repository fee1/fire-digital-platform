package com.huajie.application.api;

import com.github.pagehelper.Page;
import com.huajie.application.api.common.ApiPage;
import com.huajie.application.api.common.ApiResult;
import com.huajie.application.api.response.MenuResponseVO;
import com.huajie.application.service.MenuAppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhuxiaofeng
 * @date 2023/8/9
 */
@Api(tags = "菜单相关")
@RestController
@RequestMapping("menu")
public class MenuApi {

    @Autowired
    private MenuAppService menuAppService;

    @ApiOperation("菜单列表")
    @GetMapping("list")
    public ApiResult<ApiPage<MenuResponseVO>> getPageMenuList(@RequestParam(required = false, defaultValue = "1")Integer pageNum,
                                                              @RequestParam(required = false, defaultValue = "10")Integer pageSize,
                                                              @RequestParam(required = false)@ApiParam("菜单编码")String menuCode,
                                                              @RequestParam(required = false)@ApiParam("菜单名称")String menuName){
        Page<MenuResponseVO> page = menuAppService.getPageMenuList(pageNum, pageSize, menuCode, menuName);
        return ApiResult.ok(ApiPage.restPage(page));
    }



}
