package com.huajie.domain.schedule.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huajie.domain.common.enums.DeviceTypeEnum;
import com.huajie.domain.common.enums.ExtinguisherTypeEnum;
import com.huajie.domain.common.enums.PowerTypeEnum;
import com.huajie.domain.common.enums.ProblemStateEnum;
import com.huajie.domain.entity.Device;
import com.huajie.domain.entity.ProblemDetail;
import com.huajie.domain.entity.User;
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

@Component(value = "deviceInspectTask")
@Slf4j
public class DeviceInspectTask {

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private ProblemDetailMapper problemDetailMapper;

    @Autowired
    private UserMapper userMapper;

    public void deviceInspect(){
        this.replaceMieHuoQiList();
        this.replaceXiaoFangShuiDaiList();
        this.timeoutMieHuoQiList();
        this.timeoutXuDianChiList();
    }

    private void replaceXiaoFangShuiDaiList(){
        // 即将重新充装灭火器列表
        List<Device> deviceList = new ArrayList<>();
        // 获取即将超期的水基型灭火器列表
        Calendar instance1 = Calendar.getInstance();
        instance1.add(Calendar.MONTH,-59);
        QueryWrapper<Device> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Device::getDeviceType, DeviceTypeEnum.DeviceType03.getCode());
        queryWrapper.lambda().gt(Device::getProductionDate,instance1.getTime());
        deviceList.addAll(deviceMapper.selectList(queryWrapper));

        if(!CollectionUtils.isEmpty(deviceList)){
            for (Device item : deviceList) {
                createProblemDetail(item,"灭火器距离上一次充装日期已达期限，请注意处理！","建议重新充装灭火器");
            }
        }
    }

    private void replaceMieHuoQiList(){
        // 即将重新充装灭火器列表
        List<Device> deviceList = new ArrayList<>();
        // 获取即将超期的水基型灭火器列表
        Calendar instance1 = Calendar.getInstance();
        instance1.add(Calendar.MONTH,-23);
        QueryWrapper<Device> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Device::getDeviceType, DeviceTypeEnum.DeviceType01.getCode());
        queryWrapper.lambda().isNotNull(Device::getLastReplaceDate);
        queryWrapper.lambda().gt(Device::getLastReplaceDate,instance1.getTime());
        deviceList.addAll(deviceMapper.selectList(queryWrapper));

        Calendar instance2 = Calendar.getInstance();
        instance2.add(Calendar.MONTH,-59);
        queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Device::getDeviceType, DeviceTypeEnum.DeviceType01.getCode());
        queryWrapper.lambda().isNull(Device::getLastReplaceDate);
        queryWrapper.lambda().gt(Device::getProductionDate,instance2.getTime());
        deviceList.addAll(deviceMapper.selectList(queryWrapper));

        if(!CollectionUtils.isEmpty(deviceList)){
            for (Device item : deviceList) {
                createProblemDetail(item,"消防水带将于一个月后过期，请注意处理！","建议更换消防水带");
            }
        }
    }

    private void timeoutXuDianChiList() {
        // 即将过期灭火器列表
        List<Device> deviceList = new ArrayList<>();
        // 获取即将超期的水基型灭火器列表
        Calendar instance1 = Calendar.getInstance();
        instance1.add(Calendar.MONTH, -47);
        QueryWrapper<Device> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Device::getDeviceType, DeviceTypeEnum.DeviceType05.getCode());
        queryWrapper.lambda().eq(Device::getPowerType, PowerTypeEnum.Battery.getCode());
        queryWrapper.lambda().gt(Device::getProductionDate, instance1.getTime());
        deviceList.addAll(deviceMapper.selectList(queryWrapper));
        if(!CollectionUtils.isEmpty(deviceList)){
            for (Device item : deviceList) {
                createProblemDetail(item,"蓄电池将于一个月后过期，请注意处理！","建议更换蓄电池");
            }
        }
    }

    private void timeoutMieHuoQiList(){
        // 即将过期灭火器列表
        List<Device> deviceList = new ArrayList<>();
        // 获取即将超期的水基型灭火器列表
        Calendar instance1 = Calendar.getInstance();
        instance1.add(Calendar.MONTH,-59);
        QueryWrapper<Device> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Device::getDeviceType, DeviceTypeEnum.DeviceType01.getCode());
        queryWrapper.lambda().eq(Device::getExtinguisherType, ExtinguisherTypeEnum.WaterBase.getCode());
        queryWrapper.lambda().gt(Device::getProductionDate,instance1.getTime());
        deviceList.addAll(deviceMapper.selectList(queryWrapper));

        Calendar instance2 = Calendar.getInstance();
        instance2.add(Calendar.MONTH,-119);
        // 获取即将超期的干粉灭火器、泡沫灭火器列表
        queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Device::getDeviceType, DeviceTypeEnum.DeviceType01.getCode());
        queryWrapper.lambda().in(Device::getExtinguisherType, ExtinguisherTypeEnum.DryPowder.getCode(),ExtinguisherTypeEnum.Foam.getCode());
        queryWrapper.lambda().gt(Device::getProductionDate,instance2.getTime());
        deviceList.addAll(deviceMapper.selectList(queryWrapper));

        Calendar instance3 = Calendar.getInstance();
        instance3.add(Calendar.MONTH,-143);
        // 获取即将超期气体灭火器列表
        queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Device::getDeviceType, DeviceTypeEnum.DeviceType01.getCode());
        queryWrapper.lambda().in(Device::getExtinguisherType, ExtinguisherTypeEnum.Gas.getCode());
        queryWrapper.lambda().gt(Device::getProductionDate,instance3.getTime());
        deviceList.addAll(deviceMapper.selectList(queryWrapper));

        if(!CollectionUtils.isEmpty(deviceList)){
            for (Device item : deviceList) {
                createProblemDetail(item,"灭火器将于一个月后过期，请注意处理！","建议更换灭火器");
            }
        }
    }


    private void createProblemDetail(Device device,String problemDesc, String preReformDesc){
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.lambda().eq(User::getTenantId,device.getTenantId());
        userQueryWrapper.lambda().eq(User::getRoleId,4);
        List<User> entUsers = userMapper.selectList(userQueryWrapper);
        User user = null;
        if(!CollectionUtils.isEmpty(entUsers)){
            user = entUsers.get(0);
        }else {
            log.error("隐患创建失败，无法获取企业消防安全责任人,企业id:{}",device.getTenantId());
            return;
        }

        ProblemDetail problemDetail = new ProblemDetail();
        problemDetail.setEntTenantId(device.getTenantId());
        problemDetail.setState(ProblemStateEnum.SUBMIT.getStateCode());
        problemDetail.setProblemType("system");
        problemDetail.setPlaceId(device.getPlaceId());
        problemDetail.setPlaceName(device.getPlaceName());
        problemDetail.setDeviceId(device.getId());
        problemDetail.setDeviceName(device.getDeviceName());
        problemDetail.setProblemDesc(problemDesc);
        problemDetail.setPreReformDesc(preReformDesc);
        problemDetail.setSubmitTime(new Date());
        problemDetail.setSubmitUserId(user.getId());
        problemDetail.setSubmitUserName(user.getUserName());
        problemDetail.setSubmitUserPhone(user.getPhone());
        problemDetail.setCreateTime(new Date());
        problemDetail.setCreateUser("system");

        try {
            problemDetailMapper.insert(problemDetail);
            log.info("设备自动检查隐患创建成功，设备id:{},隐患描述:{}",device.getId(),preReformDesc);
        }catch (Exception e){
            log.error("设备自动检查隐患创建失败，失败原因:{}, {}",e.getMessage(), e.getStackTrace());
        }


    }

}
