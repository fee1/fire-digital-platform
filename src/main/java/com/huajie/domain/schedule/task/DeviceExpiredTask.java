package com.huajie.domain.schedule.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.huajie.domain.common.enums.*;
import com.huajie.domain.entity.Device;
import com.huajie.domain.entity.ProblemDetail;
import com.huajie.domain.entity.User;
import com.huajie.domain.service.DeviceService;
import com.huajie.infrastructure.mapper.DeviceMapper;
import com.huajie.infrastructure.mapper.ProblemDetailMapper;
import com.huajie.infrastructure.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component(value = "deviceExpiredTask")
@Slf4j
public class DeviceExpiredTask {

    @Autowired
    private DeviceMapper deviceMapper;


    public void deviceExpired(){
        List<Device> expiredDeviceList = new ArrayList<>();
        this.expiredMieHuoQiList(expiredDeviceList);
        this.expiredXiaoFangShuiDaiList(expiredDeviceList);
        this.timeoutMieHuoQiList(expiredDeviceList);
        this.timeoutXuDianChiList(expiredDeviceList);
        if(!CollectionUtils.isEmpty(expiredDeviceList)){
            List<Integer> deviceIds = expiredDeviceList.stream().map(Device::getId).collect(Collectors.toList());
            UpdateWrapper<Device> deviceUpdateWrapper = new UpdateWrapper<>();
            deviceUpdateWrapper.lambda().set(Device::getState,DeviceStateEnum.EXPIRED.getCode());
            deviceUpdateWrapper.lambda().in(Device::getId,deviceIds);
            deviceMapper.update(null, deviceUpdateWrapper);
        }
    }

    private void expiredXiaoFangShuiDaiList(List<Device> deviceList){
        // 获取过期的消防水带列表
        Calendar instance1 = Calendar.getInstance();
        instance1.add(Calendar.MONTH,-60);
        QueryWrapper<Device> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Device::getDeviceType, DeviceTypeEnum.DeviceType03.getCode())
                .lt(Device::getProductionDate,instance1.getTime());
        deviceList.addAll(deviceMapper.selectList(queryWrapper));
    }

    private void expiredMieHuoQiList(List<Device> deviceList){
        // 获取即将超期的水基型灭火器列表
        Calendar instance2 = Calendar.getInstance();
        instance2.add(Calendar.MONTH,-60);
        QueryWrapper<Device> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Device::getDeviceType, DeviceTypeEnum.DeviceType01.getCode());
        queryWrapper.lambda().isNull(Device::getLastReplaceDate);
        queryWrapper.lambda().lt(Device::getProductionDate,instance2.getTime());
        deviceList.addAll(deviceMapper.selectList(queryWrapper));
    }

    private void timeoutXuDianChiList(List<Device> deviceList) {
        // 获取过期的蓄电池器列表
        Calendar instance1 = Calendar.getInstance();
        instance1.add(Calendar.MONTH, -48);
        QueryWrapper<Device> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Device::getDeviceType, DeviceTypeEnum.DeviceType05.getCode());
        queryWrapper.lambda().eq(Device::getPowerType, PowerTypeEnum.Battery.getCode());
        queryWrapper.lambda().lt(Device::getProductionDate, instance1.getTime());
        deviceList.addAll(deviceMapper.selectList(queryWrapper));
    }

    private void timeoutMieHuoQiList(List<Device> deviceList){
        // 获取即将超期的水基型灭火器列表
        Calendar instance1 = Calendar.getInstance();
        instance1.add(Calendar.MONTH,-60);
        QueryWrapper<Device> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Device::getDeviceType, DeviceTypeEnum.DeviceType01.getCode());
        queryWrapper.lambda().eq(Device::getExtinguisherType, ExtinguisherTypeEnum.WaterBase.getCode());
        queryWrapper.lambda().lt(Device::getProductionDate,instance1.getTime());
        deviceList.addAll(deviceMapper.selectList(queryWrapper));

        Calendar instance2 = Calendar.getInstance();
        instance2.add(Calendar.MONTH,-120);
        // 获取即将超期的干粉灭火器、泡沫灭火器列表
        queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Device::getDeviceType, DeviceTypeEnum.DeviceType01.getCode());
        queryWrapper.lambda().in(Device::getExtinguisherType, ExtinguisherTypeEnum.DryPowder.getCode(),ExtinguisherTypeEnum.Foam.getCode());
        queryWrapper.lambda().lt(Device::getProductionDate,instance2.getTime());
        deviceList.addAll(deviceMapper.selectList(queryWrapper));

        Calendar instance3 = Calendar.getInstance();
        instance3.add(Calendar.MONTH,-144);
        // 获取即将超期气体灭火器列表
        queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Device::getDeviceType, DeviceTypeEnum.DeviceType01.getCode());
        queryWrapper.lambda().in(Device::getExtinguisherType, ExtinguisherTypeEnum.Gas.getCode());
        queryWrapper.lambda().lt(Device::getProductionDate,instance3.getTime());
        deviceList.addAll(deviceMapper.selectList(queryWrapper));
    }

}
