package com.huajie.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huajie.application.api.common.exception.ApiException;
import com.huajie.domain.common.exception.ServerException;
import com.huajie.domain.common.oauth2.model.CustomizeGrantedAuthority;
import com.huajie.domain.common.utils.UserContext;
import com.huajie.domain.entity.Device;
import com.huajie.infrastructure.mapper.DeviceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DeviceService {

    @Autowired
    private DeviceMapper deviceMapper;

    public List<Device> getDeviceListByPlaceId(Integer placeId){
        if(placeId == null){
            return Collections.emptyList();
        }
        QueryWrapper<Device> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Device::getPlaceId,placeId);
        queryWrapper.lambda().eq(Device::getDeleted,"0");
        queryWrapper.lambda().eq(Device::getTenantId,UserContext.getCurrentTenant().getId());
        return deviceMapper.selectList(queryWrapper);
    }

    public Map<Integer,List<Device>> getPlaceDeviceMap(List<Integer> placeIds){
        if(CollectionUtils.isEmpty(placeIds)){
            return new HashMap<>();
        }

        QueryWrapper<Device> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(Device::getPlaceId,placeIds);
        queryWrapper.lambda().eq(Device::getDeleted,"0");
        queryWrapper.lambda().eq(Device::getTenantId,UserContext.getCurrentTenant().getId());

        List<Device> devices = deviceMapper.selectList(queryWrapper);
        if(CollectionUtils.isEmpty(devices)){
            return new HashMap<>();
        }

        Map<Integer, List<Device>> placeDeviceMap = devices.stream().collect(Collectors.groupingBy(Device::getPlaceId));
        return placeDeviceMap;
    }



    public Device addDevice(Device device){
        CustomizeGrantedAuthority customizeGrantedAuthority = UserContext.getCustomizeGrantedAuthority();
        device.setOperatorId(customizeGrantedAuthority.getUserId());
        device.setTenantId(customizeGrantedAuthority.getTenant().getId());

        deviceMapper.insert(device);
        return device;
    }

    public void editDevice(Device device){
        Device updateDevice = new Device();
        updateDevice.setVersion(device.getVersion());
        updateDevice.setId(device.getId());
        updateDevice.setDeviceName(device.getDeviceName());
        updateDevice.setDeviceNo(device.getDeviceNo());
        updateDevice.setLastReplaceDate(device.getLastReplaceDate());
        updateDevice.setLastUseDate(device.getLastUseDate());
        updateDevice.setRemark(device.getRemark());
        deviceMapper.updateById(updateDevice);
    }

    public void deleteDevice(Integer deviceId){
        deviceMapper.deleteById(deviceId);
    }

    public Device getDeviceById(Integer deviceId){
        return deviceMapper.selectById(deviceId);
    }


}
