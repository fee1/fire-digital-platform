package com.huajie.application.api;

import com.huajie.application.api.common.ApiResult;
import com.huajie.application.api.request.DicAddRequestVO;
import com.huajie.application.api.request.DicRequestVO;
import com.huajie.application.api.response.DicResponseVO;
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

import java.util.List;

/**
 * @author zhuxiaofeng
 * @date 2023/8/8
 */
@Api("字典表相关")
@RestController
@RequestMapping(value = "sys/dic")
public class SysDicApi {

    @Autowired
    private SysDicAppService sysDicAppService;

    @ApiOperation("字典列表")
    @GetMapping("list")
    public ApiResult<List<DicResponseVO>> getDicList(@ApiParam("字典code") @RequestParam String dicCode,
                                                     @ApiParam("描述") @RequestParam String description){
        List<DicResponseVO> dicResponseVOS = sysDicAppService.getDicList(dicCode, description);
        return ApiResult.ok(dicResponseVOS);
    }

    @ApiOperation("新增字典")
    @PostMapping("add")
    public ApiResult<Void> addDic(@RequestBody @Validated DicAddRequestVO requestVO){
        sysDicAppService.addDic(requestVO);
        return ApiResult.ok();
    }

}
