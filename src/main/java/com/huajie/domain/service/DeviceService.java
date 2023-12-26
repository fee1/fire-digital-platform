package com.huajie.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huajie.application.api.response.statistic.DeviceStateCountResponseVO;
import com.huajie.domain.common.enums.DeviceStateEnum;
import com.huajie.domain.common.enums.DeviceTypeEnum;
import com.huajie.domain.common.oauth2.model.CustomizeGrantedAuthority;
import com.huajie.domain.common.utils.UserContext;
import com.huajie.domain.entity.Device;
import com.huajie.infrastructure.mapper.DeviceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DeviceService {

    @Autowired
    private DeviceMapper deviceMapper;

    public Integer getDeviceCountByTenantId(Integer tenantId){
        QueryWrapper<Device> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Device::getTenantId,tenantId);
        return deviceMapper.selectCount(queryWrapper);
    }

    public Integer getNewDeviceCountByTenantId(Integer tenantId, Date startDate){
        QueryWrapper<Device> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Device::getTenantId,tenantId);
        queryWrapper.lambda().gt(Device::getCreateTime,startDate);
        return deviceMapper.selectCount(queryWrapper);
    }

    public Integer getEffectiveCountByTenantId(Integer tenantId){
        QueryWrapper<Device> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Device::getTenantId,tenantId);
        queryWrapper.lambda().eq(Device::getState,DeviceStateEnum.NORMAL.getCode());
        return deviceMapper.selectCount(queryWrapper);
    }

    public List<Device> getDeviceListByPlaceId(Integer placeId){
        if(placeId == null){
            return Collections.emptyList();
        }
        QueryWrapper<Device> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Device::getPlaceId,placeId);
        queryWrapper.lambda().eq(Device::getDeleted,"0");
        return deviceMapper.selectList(queryWrapper);
    }

    public Map<Integer,List<Device>> getPlaceDeviceMap(List<Integer> placeIds){
        if(CollectionUtils.isEmpty(placeIds)){
            return new HashMap<>();
        }

        QueryWrapper<Device> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(Device::getPlaceId,placeIds);
        queryWrapper.lambda().eq(Device::getDeleted,"0");

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
        device.setState(DeviceStateEnum.NORMAL.getCode());
        deviceMapper.insert(device);
        return device;
    }

    public int editDevice(Device device){
        Device updateDevice = new Device();
        updateDevice.setVersion(device.getVersion());
        updateDevice.setId(device.getId());
        updateDevice.setDeviceName(device.getDeviceName());
        updateDevice.setDeviceNo(device.getDeviceNo());
        updateDevice.setLastReplaceDate(device.getLastReplaceDate());
        updateDevice.setLastUseDate(device.getLastUseDate());
        updateDevice.setRemark(device.getRemark());
        updateDevice.setState(device.getState());
        return deviceMapper.updateById(updateDevice);
    }

    public int deleteDevice(Integer deviceId){
        return deviceMapper.deleteById(deviceId);
    }

    public Device getDeviceById(Integer deviceId){
        return deviceMapper.selectById(deviceId);
    }

    public List<DeviceStateCountResponseVO> getDeviceStateCount(Integer tenantId){
        List<DeviceStateCountResponseVO> deviceStateCount = deviceMapper.getDeviceStateCount(tenantId);
        if(!CollectionUtils.isEmpty(deviceStateCount)){
            deviceStateCount.stream().forEach(item -> {
                item.setDeviceTypeName(DeviceTypeEnum.valueOf(item.getDeviceType()).getName());
            });
        }
        return deviceStateCount;
    }

    public List<Device> getDeviceListByEnterpriseIds(List<Integer> enterpriseIds){
        LambdaQueryWrapper<Device> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(Device::getTenantId,enterpriseIds);
        return deviceMapper.selectList(lambdaQueryWrapper);
    }


}
