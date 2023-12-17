package com.huajie.application.api;

import com.huajie.application.api.common.ApiResult;
import com.huajie.application.api.response.statistic.*;
import com.huajie.application.service.EnterpriseIndexStatisticService;
import com.huajie.application.service.GovernmentIndexStatisticService;
import com.huajie.application.service.NoticeAppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Api(tags = "首页统计")
@RestController
@RequestMapping("/index/statistic")
public class IndexStatisticApi {

    @Autowired
    private GovernmentIndexStatisticService governmentIndexStatisticService;

    @Autowired
    private EnterpriseIndexStatisticService enterpriseIndexStatisticService;

    @Autowired
    private NoticeAppService noticeAppService;

    @ApiOperation("企业设备类型数量统计")
    @GetMapping(value = "/ent/deviceTypeList")
    public ApiResult<List<DeviceStateCountResponseVO>> deviceTypeList(){
        return ApiResult.ok(enterpriseIndexStatisticService.getDeviceStateCount());
    }


    @ApiOperation("企业基本数据信息")
    @GetMapping(value = "/ent/baseInfo")
    public ApiResult<EntIndexResponseVO> enterpriseInfo(){
        return ApiResult.ok(enterpriseIndexStatisticService.getEntBaseInfo());
    }


    @ApiOperation("企业云体检")
    @GetMapping(value = "/ent/cloudCheck")
    public ApiResult<EntCloudCheckResponseVO> enterpriseCloudCheck(){
        return ApiResult.ok(enterpriseIndexStatisticService.getEntCloudCheck());
    }


    @ApiOperation("政府基本数据信息")
    @GetMapping(value = "/gov/baseInfo")
    public ApiResult<GovIndexResponseVO> governmentInfo(){
        return ApiResult.ok(governmentIndexStatisticService.getGovBaseInfo());
    }

    @ApiOperation("新增企业类型数量")
    @GetMapping(value = "/gov/newEnterpriseTypeCount")
    public ApiResult<GovIndexEntCountByTypeResponseVO> govNewEnterpriseList(){
        return ApiResult.ok(governmentIndexStatisticService.getNewEnterpriseListInLastWeek());
    }

    @ApiOperation("新增设备数量统计")
    @GetMapping(value = "/gov/newDeviceCount")
    public ApiResult<List<StatisticResponseVO>> govNewDeviceCount(){
        return ApiResult.ok(governmentIndexStatisticService.getNewDeviceCount());
    }

    @ApiOperation("根据安全等级统计企业数量")
    @GetMapping(value = "/gov/getEnterpriseCountBySecurityLevel")
    public ApiResult<GovIndexEntCountBySecurityLevelResponseVO> getEnterpriseCountBySafeLevel(@RequestParam(value = "city",required = false)Integer city,
                                                   @RequestParam(value = "region",required = false)Integer region,
                                                   @RequestParam(value = "street",required = false)Integer street
    ){
        return ApiResult.ok(governmentIndexStatisticService.getEnterpriseCountBySecurityLevel(city,region,street));
    }

    @ApiOperation("用户工作日历")
    @GetMapping(value = "/user/workCalender")
    public ApiResult<List<WorkMessageResponseVO>> getWorkCalender(@RequestParam(required = false)@ApiParam("结束时间")@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date date){
        return ApiResult.ok(noticeAppService.getWorkCalender(date));
    }


}
