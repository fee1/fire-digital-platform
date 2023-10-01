package com.huajie.application.api;

import com.huajie.application.api.common.ApiResult;
import com.huajie.application.api.request.DeviceAddRequestVO;
import com.huajie.application.api.request.DeviceEditRequestVO;
import com.huajie.application.api.response.DeviceResponseVO;
import com.huajie.application.service.DeviceAppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "设备相关")
@RestController
@RequestMapping("/device")
public class DeviceApi {

    @Autowired
    private DeviceAppService deviceAppService;

    @ApiOperation("通过点位id新增设备")
    @PostMapping("/addDeviceByPlaceId")
    public ApiResult<DeviceResponseVO> addDeviceByPlaceId(
            @RequestParam Integer placeId,
            @RequestBody @Valid DeviceAddRequestVO requestVO){
        return ApiResult.ok(deviceAppService.addDeviceByPlaceId(placeId,requestVO));
    }

    @ApiOperation("通过NFC代码新增设备")
    @PostMapping("/addDeviceByNFCCode")
    public ApiResult<DeviceResponseVO> addDeviceByNFCCode(
            @RequestParam  String NFCCode,
            @RequestBody @Valid DeviceAddRequestVO requestVO){
        return ApiResult.ok(deviceAppService.addDeviceByNFCCode(NFCCode,requestVO));
    }

    @ApiOperation("编辑设备")
    @PostMapping("/editDevice")
    public ApiResult<DeviceResponseVO> editDevice(
            @RequestBody @Valid DeviceEditRequestVO requestVO){
            deviceAppService.editDevice(requestVO);
        return ApiResult.ok();
    }

    @ApiOperation("删除设备")
    @PostMapping("/deleteDevice")
    public ApiResult<DeviceResponseVO> deleteDevice(
             Integer deviceId){
        deviceAppService.deleteDevice(deviceId);
        return ApiResult.ok();
    }

    @ApiOperation("根据点位id获取设备列表")
    @GetMapping("/getDeviceListByPlaceId")
    public ApiResult<List<DeviceResponseVO>> getDeviceListByPlaceId(
            Integer placeId){
        return ApiResult.ok(deviceAppService.getDeviceListByPlaceId(placeId));
    }

    @ApiOperation("根据设备id获取设备信息")
    @GetMapping("/getDeviceById")
    public ApiResult<DeviceResponseVO> getDeviceById(
            Integer deviceId){
        return ApiResult.ok(deviceAppService.getDeviceById(deviceId));
    }


}
