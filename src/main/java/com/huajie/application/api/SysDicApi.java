package com.huajie.application.api;

import com.github.pagehelper.Page;
import com.huajie.application.api.common.ApiPage;
import com.huajie.application.api.common.ApiResult;
import com.huajie.application.api.request.DicAddRequestVO;
import com.huajie.application.api.request.DicValueAddRequestVO;
import com.huajie.application.api.response.DicResponseVO;
import com.huajie.application.api.response.DicValueResponseVO;
import com.huajie.application.service.SysDicAppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author zhuxiaofeng
 * @date 2023/8/8
 */
@Api(tags = "字典相关")
@RestController
@RequestMapping(value = "sys/dic")
public class SysDicApi {

    @Autowired
    private SysDicAppService sysDicAppService;

    @ApiOperation("字典列表")
    @GetMapping("list")
    public ApiResult<ApiPage<DicResponseVO>> getPageDicList(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                            @ApiParam("字典code") @RequestParam String dicCode,
                                                        @ApiParam("描述") @RequestParam String description){
        Page<DicResponseVO> dicResponseVOS = sysDicAppService.getPageDicList(dicCode, description, pageNum, pageSize);
        return ApiResult.ok(ApiPage.restPage(dicResponseVOS));
    }

    @ApiOperation("新增字典")
    @PostMapping("add")
    public ApiResult<Void> addDic(@RequestBody @Validated DicAddRequestVO requestVO){
        sysDicAppService.addDic(requestVO);
        return ApiResult.ok();
    }

    @ApiOperation("字典值列表")
    @GetMapping("value/list")
    public ApiResult<ApiPage<DicValueResponseVO>> getPageDicValueList(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                                   @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                                   @Valid @ApiParam("字典code") @RequestParam
                                                                   @NotBlank(message = "字典code不能为空")
                                                                   @NotNull(message = "字典code不能为空") String dicCode){
        Page<DicValueResponseVO> dicValueList = sysDicAppService.getPageDicValueList(dicCode, pageNum, pageSize);
        return ApiResult.ok(ApiPage.restPage(dicValueList));
    }

    @ApiOperation("新增字典值列表")
    @PostMapping("value/add")
    public ApiResult<Void> addDicValue(@Validated @RequestBody DicValueAddRequestVO requestVO){
        sysDicAppService.addDicValue(requestVO);
        return ApiResult.ok();
    }

}
