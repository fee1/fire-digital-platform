package com.huajie.application.api;


import com.github.pagehelper.Page;
import com.huajie.application.api.common.ApiPage;
import com.huajie.application.api.common.ApiResult;
import com.huajie.application.api.request.*;
import com.huajie.application.api.response.*;
import com.huajie.application.service.InspectAppService;
import com.huajie.domain.common.utils.Base64Util;
import com.huajie.domain.entity.DeviceInspectInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@Api(tags = "巡查检查")
public class InspectApi {

    @Autowired
    private InspectAppService inspectAppService;

    @ApiOperation("登录限制-保存巡查检查记录")
    @PostMapping(value = "/save")
    public ApiResult save(@RequestBody @Validated AddInspectRequestVO requestVO){
        inspectAppService.save(requestVO);
        return ApiResult.ok();
    }

    @ApiOperation("根据设备类型获取检查项")
    @GetMapping(value = "/getDeviceInspectInfos")
    public ApiResult<List<DeviceInspectInfo>> save(@RequestParam @ApiParam("设备类型") String deviceType){
        return ApiResult.ok(inspectAppService.getDeviceInspectInfos(deviceType));
    }


    @ApiOperation("wx-根据点位Code获取设备列表及检查信息")
    @GetMapping("/getDeviceListWithInspectByNFC")
    public ApiResult<List<DeviceResponseVO>> getDeviceListWithInspectByNFC(
            @RequestParam @ApiParam("点位nfcCode") String nfcCode,
            @RequestParam @ApiParam("检查类型, patrol：巡查；inspect：检查") String inspectType){
        return ApiResult.ok(inspectAppService.getDeviceListWithInspectByNFC(nfcCode,inspectType));
    }

    @ApiOperation("根据企业id查询指定季度/月度检查记录(按点位分页),政府企业通用")
    @GetMapping("/getInspectRecord")
    public ApiResult<InspectRecordResponseVO> getInspectRecord(
            @RequestParam(required = false, defaultValue = "1")Integer pageNum,
            @RequestParam(required = false, defaultValue = "10")Integer pageSize,
            @Validated InspectQueryRequestVO requestVO){
        return ApiResult.ok(inspectAppService.getInspectRecord(pageNum,pageSize,requestVO));
    }

    @ApiOperation("企业PC-分页查询企业巡查记录")
    @GetMapping("/getPatrolList")
    public ApiResult<ApiPage<InspectDetailResponseVO>> getPagePatrolList(
            @RequestParam(required = false, defaultValue = "1")Integer pageNum,
            @RequestParam(required = false, defaultValue = "10")Integer pageSize,
            PatrolQueryRequestVO requestVO){

        return ApiResult.ok(ApiPage.restPage(inspectAppService.getPagePatrolList(pageNum,pageSize,requestVO)));
    }


    @ApiOperation("企业PC-分页查询企业自查记录(企业)")
    @GetMapping("/getSelfCheckList")
    public ApiResult<ApiPage<InspectDetailResponseVO>> getPageSelfCheckList(
            @RequestParam(required = false, defaultValue = "1")Integer pageNum,
            @RequestParam(required = false, defaultValue = "10")Integer pageSize,
            SelfCheckQueryRequestVO requestVO){

        return ApiResult.ok(ApiPage.restPage(inspectAppService.getPageSelfCheckList(pageNum,pageSize,requestVO)));
    }

}
