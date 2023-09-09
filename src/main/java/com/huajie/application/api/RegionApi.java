package com.huajie.application.api;

import com.huajie.application.api.common.ApiResult;
import com.huajie.application.api.response.RegionResponseVO;
import com.huajie.application.service.RegionAppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zhuxiaofeng
 * @date 2023/8/22
 */
@Api(tags = "区域相关")
@RestController
@RequestMapping("region")
public class RegionApi {

    @Autowired
    private RegionAppService regionAppService;

    @ApiOperation("获取省")
    @GetMapping("province/list")
    public ApiResult<List<RegionResponseVO>> getProvinces(){
        List<RegionResponseVO> responseVOList = regionAppService.getProvinces();
        return ApiResult.ok(responseVOList);
    }

    @ApiOperation("获取市")
    @GetMapping("city/list")
    public ApiResult<List<RegionResponseVO>> getCities(@ApiParam("省Id") @RequestParam Integer provinceId){
        List<RegionResponseVO> responseVOList = regionAppService.getRegionListByParentId(provinceId);
        return ApiResult.ok(responseVOList);
    }

    @ApiOperation("获取区县")
    @GetMapping("district/list")
    public ApiResult<List<RegionResponseVO>> getDistricts(@ApiParam("市Id") @RequestParam Integer cityId){
        List<RegionResponseVO> responseVOList = regionAppService.getRegionListByParentId(cityId);
        return ApiResult.ok(responseVOList);
    }

    @ApiOperation("获取街道")
    @GetMapping("street/list")
    public ApiResult<List<RegionResponseVO>> getStreets(@ApiParam("区县Id") @RequestParam Integer regionId){
        List<RegionResponseVO> responseVOList = regionAppService.getRegionListByParentId(regionId);
        return ApiResult.ok(responseVOList);
    }

}
