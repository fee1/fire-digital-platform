package com.huajie.application.api;

import com.huajie.application.api.common.ApiResult;
import com.huajie.application.service.EnterpriseIndexStatisticService;
import com.huajie.application.service.GovernmentIndexStatisticService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "首页统计")
@RestController
@RequestMapping("/index/statistic")
public class IndexStatisticApi {

    @Autowired
    private GovernmentIndexStatisticService governmentIndexStatisticService;

    @Autowired
    private EnterpriseIndexStatisticService enterpriseIndexStatisticService;

    @ApiOperation("企业设备类型数量统计")
    @GetMapping(value = "/ent/deviceTypeList")
    public ApiResult deviceTypeList(){
        return ApiResult.ok(enterpriseIndexStatisticService.getEntBaseInfo());
    }


    @ApiOperation("企业基本数据信息")
    @GetMapping(value = "/ent/baseInfo")
    public ApiResult enterpriseInfo(){
        return ApiResult.ok(enterpriseIndexStatisticService.getDeviceStateCount());
    }


    @ApiOperation("企业云体检")
    @GetMapping(value = "/ent/cloudCheck")
    public ApiResult enterpriseCloudCheck(){
        return ApiResult.ok(enterpriseIndexStatisticService.getEntCloudCheck());
    }


    @ApiOperation("政府基本数据信息")
    @GetMapping(value = "/gov/baseInfo")
    public ApiResult governmentInfo(){
        return ApiResult.ok(governmentIndexStatisticService.getGovBaseInfo());
    }

    @ApiOperation("新增企业类型数量")
    @GetMapping(value = "/gov/newEnterpriseTypeCount")
    public ApiResult govNewEnterpriseList(){
        return ApiResult.ok(governmentIndexStatisticService.getNewEnterpriseListInLastWeek());
    }

    @ApiOperation("新增设备数量统计")
    @GetMapping(value = "/gov/newDeviceCount")
    public ApiResult govNewDeviceCount(){
        return ApiResult.ok(governmentIndexStatisticService.getNewDeviceCount());
    }

    @ApiOperation("根据安全等级统计企业数量")
    @GetMapping(value = "/gov/getEnterpriseCountBySecurityLevel")
    public ApiResult getEnterpriseCountBySafeLevel(@RequestParam(value = "city",required = false)Integer city,
                                                   @RequestParam(value = "region",required = false)Integer region,
                                                   @RequestParam(value = "street",required = false)Integer street
    ){
        return ApiResult.ok(governmentIndexStatisticService.getEnterpriseCountBySecurityLevel(city,region,street));
    }


}
