package com.huajie.application.api;

import com.github.pagehelper.Page;
import com.huajie.application.api.common.ApiPage;
import com.huajie.application.api.common.ApiResult;
import com.huajie.application.api.response.FunctionResponseVO;
import com.huajie.application.service.FunctionAppService;
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
@Api(tags = "功能管理相关")
@RestController
@RequestMapping("function")
public class FunctionApi {

    @Autowired
    private FunctionAppService functionAppService;

    @ApiOperation("功能列表")
    @GetMapping("list")
    public ApiResult<ApiPage<FunctionResponseVO>> getPageFunctionList(@RequestParam(required = false, defaultValue = "1")Integer pageNum,
                                                                      @RequestParam(required = false, defaultValue = "10")Integer pageSize,
                                                                      @RequestParam(required = false)@ApiParam("功能code")String functionCode,
                                                                      @RequestParam(required = false)@ApiParam("功能名称")String functionName){
        Page<FunctionResponseVO> page = functionAppService.getPageFunctionList(pageNum, pageSize, functionCode, functionName);
        return ApiResult.ok(ApiPage.restPage(page));
    }

}
