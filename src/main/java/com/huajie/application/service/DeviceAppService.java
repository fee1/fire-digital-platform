package com.huajie.application.service;

import com.huajie.application.api.common.exception.ApiException;
import com.huajie.application.api.request.DeviceAddRequestVO;
import com.huajie.application.api.request.DeviceEditRequestVO;
import com.huajie.application.api.response.DeviceResponseVO;
import com.huajie.domain.common.enums.DeviceTypeEnum;
import com.huajie.domain.common.enums.ExtinguisherTypeEnum;
import com.huajie.domain.common.enums.PowerTypeEnum;
import com.huajie.domain.common.exception.ServerException;
import com.huajie.domain.common.oauth2.model.CustomizeGrantedAuthority;
import com.huajie.domain.common.utils.UserContext;
import com.huajie.domain.entity.Device;
import com.huajie.domain.entity.Place;
import com.huajie.domain.entity.User;
import com.huajie.domain.service.DeviceService;
import com.huajie.domain.service.PlaceService;
import com.huajie.domain.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeviceAppService {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private PlaceService placeService;

    @Autowired
    private UserService userService;


    public DeviceResponseVO addDeviceByPlaceId(Integer placeId, DeviceAddRequestVO requestVO){
        // 校验点位信息是否存在
        Place place = placeService.getPlaceById(placeId);
        if(place == null){
            throw new ApiException("新增设备失败，点位信息不存在");
        }
        return this.addDevice(place.getId(),requestVO);
    }

    public DeviceResponseVO addDeviceByNFCCode(String NFCCode,DeviceAddRequestVO requestVO){
        // 校验点位信息是否存在
        Place place = placeService.getPlaceByNFCCode(NFCCode);
        if(place == null){
            throw new ApiException("新增设备失败，点位信息不存在");
        }
        return this.addDevice(place.getId(),requestVO);
    }


    private DeviceResponseVO addDevice(Integer placeId, DeviceAddRequestVO requestVO){
        // 校验点位下设备数是否超过上限 5
        List<Device> deviceList = deviceService.getDeviceListByPlaceId(placeId);
        if(deviceList.size() >= 5){
            throw new ApiException("新增设备失败，当前点位下设备数量已达上限");
        }
        // 3 插入
        Device device = new Device();
        device.setPlaceId(placeId);
        BeanUtils.copyProperties(requestVO,device);
        device = deviceService.addDevice(device);

        DeviceResponseVO deviceResponseVO = new DeviceResponseVO();
        BeanUtils.copyProperties(device,deviceResponseVO);
        return deviceResponseVO;
    }

    public void editDevice(DeviceEditRequestVO requestVO){
        Device device = new Device();
        BeanUtils.copyProperties(requestVO,device);
        if(deviceService.editDevice(device) < 1){
            throw new ApiException("编辑设备失败，请重试");
        }
    }

    public void deleteDevice(Integer deviceId){
        if(deviceService.deleteDevice(deviceId) < 1){
            throw new ApiException("删除设备失败，请重试");
        }
    }

    public DeviceResponseVO getDeviceById(Integer deviceId){
        Device device = deviceService.getDeviceById(deviceId);
        DeviceResponseVO deviceResponseVO = new DeviceResponseVO();
        BeanUtils.copyProperties(device,deviceResponseVO);
        return deviceResponseVO;
    }

    public List<DeviceResponseVO> getDeviceListByPlaceId(Integer placeId){
        List<Device> deviceList = deviceService.getDeviceListByPlaceId(placeId);
        List<DeviceResponseVO> deviceResponseVOS = new ArrayList<>(5);
        for (Device device: deviceList) {
            DeviceResponseVO deviceResponseVO = new DeviceResponseVO();
            BeanUtils.copyProperties(device,deviceResponseVO);
            deviceResponseVOS.add(deviceResponseVO);

            User user = userService.getUserById(device.getOperatorId());
            deviceResponseVO.setOperatorName(user.getUserName());
            deviceResponseVO.setOperatorPhone(user.getPhone());

            deviceResponseVO.setDeviceTypeDesc(DeviceTypeEnum.valueOf(device.getDeviceType()).getName());

            if(StringUtils.isNotBlank(device.getExtinguisherType())){
                deviceResponseVO.setExtinguisherTypeDesc(ExtinguisherTypeEnum.valueOf(device.getExtinguisherType()).getCode());
            }
            if(StringUtils.isNotBlank(device.getPowerType())){
                deviceResponseVO.setPowerTypeDesc(PowerTypeEnum.valueOf(device.getPowerType()).getCode());
            }

        }
        return deviceResponseVOS;
    }



}
