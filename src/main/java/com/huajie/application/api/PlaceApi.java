package com.huajie.application.api;

import com.huajie.application.api.common.ApiPage;
import com.huajie.application.api.common.ApiResult;
import com.huajie.application.api.request.PlaceAddRequestVO;
import com.huajie.application.api.request.PlaceEditRequestVO;
import com.huajie.application.api.request.PlaceQueryRequestVO;
import com.huajie.application.api.response.PlaceResponseVO;
import com.huajie.application.api.response.RegionResponseVO;
import com.huajie.application.service.PlaceAppService;
import com.huajie.domain.entity.Place;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "点位相关")
@RestController
@RequestMapping("/place")
public class PlaceApi {

    @Autowired
    private PlaceAppService placeAppService;

    @ApiOperation("分页获取点位列表")
    @GetMapping("/page")
    public ApiResult<ApiPage<PlaceResponseVO>> pagePlaceList(@RequestParam(required = false, defaultValue = "1")Integer pageNum,
                                                                              @RequestParam(required = false, defaultValue = "10")Integer pageSize,
                                                                              PlaceQueryRequestVO requestVO){
        ApiPage<PlaceResponseVO> result = ApiPage.restPage(placeAppService.pagePlaceListWithDevices(pageNum, pageSize, requestVO));
        return ApiResult.ok(result);
    }


    @ApiOperation("新增点位")
    @PostMapping("/addPlace")
    public ApiResult<PlaceResponseVO> addPlace(@RequestBody @Valid PlaceAddRequestVO requestVO){
        return ApiResult.ok(placeAppService.addPlace(requestVO));
    }

    @ApiOperation("编辑点位")
    @PostMapping("/editPlace")
    public ApiResult editPlace(@RequestBody @Valid PlaceEditRequestVO requestVO){
        placeAppService.editPlace(requestVO);
        return ApiResult.ok();
    }

    @ApiOperation("删除点位")
    @PostMapping("/deletePlace")
    public ApiResult deletePlace(@RequestParam Integer placeId){
        placeAppService.deletePlace(placeId);
        return ApiResult.ok();
    }

}
