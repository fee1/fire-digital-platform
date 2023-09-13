package com.huajie.application.api;


import com.github.pagehelper.Page;
import com.huajie.application.api.common.ApiPage;
import com.huajie.application.api.common.ApiResult;
import com.huajie.application.api.request.AddInspectRequestVO;
import com.huajie.application.api.request.LoginRequestVO;
import com.huajie.application.api.request.PatrolQueryRequestVO;
import com.huajie.application.api.request.SelfCheckQueryRequestVO;
import com.huajie.application.api.response.DeviceResponseVO;
import com.huajie.application.api.response.InspectDetailResponseVO;
import com.huajie.application.api.response.LoginResponseVO;
import com.huajie.application.api.response.PlaceResponseVO;
import com.huajie.application.service.InspectAppService;
import com.huajie.domain.common.utils.Base64Util;
import com.huajie.domain.entity.DeviceInspectInfo;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("inspect")
public class InspectApi {

    @Autowired
    private InspectAppService inspectAppService;

    @ApiOperation("保存巡查检查记录")
    @PostMapping(value = "/save")
    public ApiResult save(@RequestBody @Validated AddInspectRequestVO requestVO){
        inspectAppService.save(requestVO);
        return ApiResult.ok();
    }

    @ApiOperation("根据设备类型获取检查项")
    @GetMapping(value = "/getDeviceInspectInfos")
    public ApiResult<List<DeviceInspectInfo>> save(@RequestParam String deviceType){
        inspectAppService.getDeviceInspectInfos(deviceType);
        return ApiResult.ok();
    }


    @ApiOperation("wx-根据点位Code获取设备列表及检查信息")
    @GetMapping("/getDeviceListWithInspectByNFC")
    public ApiResult<List<DeviceResponseVO>> getDeviceListWithInspectByNFC(
            @RequestParam String nfcCode,
            @RequestParam String inspectType){
        return ApiResult.ok(inspectAppService.getDeviceListWithInspectByNFC(nfcCode,inspectType));
    }

    @ApiOperation("分页查询企业巡查记录(企业)")
    @GetMapping("/getPatrolList")
    public ApiResult<ApiPage<InspectDetailResponseVO>> getPagePatrolList(
            @RequestParam(required = false, defaultValue = "1")Integer pageNum,
            @RequestParam(required = false, defaultValue = "10")Integer pageSize,
            PatrolQueryRequestVO requestVO){

        return ApiResult.ok(ApiPage.restPage(inspectAppService.getPagePatrolList(pageNum,pageSize,requestVO)));
    }


    @ApiOperation("分页查询企业自查记录(企业)")
    @GetMapping("/getSelfCheckList")
    public ApiResult<ApiPage<InspectDetailResponseVO>> getPageSelfCheckList(
            @RequestParam(required = false, defaultValue = "1")Integer pageNum,
            @RequestParam(required = false, defaultValue = "10")Integer pageSize,
            SelfCheckQueryRequestVO requestVO){

        return ApiResult.ok(ApiPage.restPage(inspectAppService.getPageSelfCheckList(pageNum,pageSize,requestVO)));
    }


















}
