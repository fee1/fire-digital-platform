package com.huajie.domain.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.huajie.domain.common.constants.InspectTypeConstants;
import com.huajie.domain.common.utils.DateUtil;
import com.huajie.domain.common.utils.PeriodUtil;
import com.huajie.domain.common.utils.UserContext;
import com.huajie.domain.entity.DeviceInspectInfo;
import com.huajie.domain.entity.InspectDetail;
import com.huajie.infrastructure.mapper.DeviceInspectInfoMapper;
import com.huajie.infrastructure.mapper.InspectDetailMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


@Service
public class InspectDetailService {
    @Autowired
    private InspectDetailMapper inspectDetailMapper;

    @Autowired
    private DeviceInspectInfoMapper deviceInspectInfoMapper;


    public InspectDetail insert(InspectDetail inspectDetail){
        inspectDetailMapper.insert(inspectDetail);
        return inspectDetail;
    }

    /**
     * 获取某企业下的企业检查记录
     * @param enterpriseId
     * @param startTime
     * @param endTime
     * @return
     */
    public List<InspectDetail> getEnterpriseInspectList(Integer enterpriseId, LocalDate startTime, LocalDate endTime){
        QueryWrapper<InspectDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(InspectDetail::getEntTenantId, enterpriseId)
                .eq(InspectDetail::getInspectType,InspectTypeConstants.INSPECT);
        queryWrapper.lambda().ge(InspectDetail::getCreateTime,startTime);
        queryWrapper.lambda().le(InspectDetail::getCreateTime,endTime.plusDays(1));
        queryWrapper.lambda().orderByDesc(InspectDetail::getCreateTime);

        return inspectDetailMapper.selectList(queryWrapper);
    }

    /**
     * 获取某企业下的政府检查记录
     * @param enterpriseId
     * @param adminGovernmentIds
     * @param startDate
     * @param endDate
     * @return
     */
    public List<InspectDetail> getGovernmentInspectList(Integer enterpriseId, List<Integer> adminGovernmentIds,
                                                        LocalDate startDate, LocalDate endDate ){
        QueryWrapper<InspectDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(InspectDetail::getEntTenantId,enterpriseId)
                .eq(InspectDetail::getInspectType,InspectTypeConstants.INSPECT);
        if(!CollectionUtils.isEmpty(adminGovernmentIds)){
            queryWrapper.lambda().in(InspectDetail::getGovTenantId, adminGovernmentIds);
        }
        queryWrapper.lambda().ge(InspectDetail::getCreateTime,startDate);
        queryWrapper.lambda().le(InspectDetail::getCreateTime,endDate.plusDays(1));
        queryWrapper.lambda().orderByDesc(InspectDetail::getCreateTime);

        return inspectDetailMapper.selectList(queryWrapper);
    }



    public List<InspectDetail> getCurrentMonthInspectListByGovernment(Integer govTenantId){
        QueryWrapper<InspectDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(InspectDetail::getGovTenantId,govTenantId)
                .eq(InspectDetail::getInspectType,InspectTypeConstants.INSPECT);
        queryWrapper.lambda().gt(InspectDetail::getCreateTime, DateUtil.getCurrentMonthStartDate());

        return inspectDetailMapper.selectList(queryWrapper);
    }


    public Page<InspectDetail> getPageSelfCheckList(Integer pageNum, Integer pageSize,
                                                    LocalDate startDate, LocalDate endDate){
        QueryWrapper<InspectDetail> queryWrapper = new QueryWrapper<>();

        queryWrapper.lambda()
                .eq(InspectDetail::getEntTenantId, UserContext.getCurrentTenant().getId())
                .eq(InspectDetail::getInspectType,InspectTypeConstants.SELF_CHECK);
        queryWrapper.lambda().ge(InspectDetail::getCreateTime,startDate);
        queryWrapper.lambda().le(InspectDetail::getCreateTime,endDate.plusDays(1));
        queryWrapper.lambda().orderByDesc(InspectDetail::getCreateTime);
        PageHelper.startPage(pageNum, pageSize);
        return (Page<InspectDetail>) inspectDetailMapper.selectList(queryWrapper);

    }


    public Page<InspectDetail> getPagePatrolList(Integer pageNum, Integer pageSize,
                                                 Integer placeId, String placeName,
                                                 Integer deviceId, String deviceName,
                                                 LocalDate startTime, LocalDate endTime){
        QueryWrapper<InspectDetail> queryWrapper = new QueryWrapper<>();

        queryWrapper.lambda()
                .eq(InspectDetail::getEntTenantId, UserContext.getCurrentTenant().getId())
                .eq(InspectDetail::getInspectType,InspectTypeConstants.PATROL);
        if(placeId != null){
            queryWrapper.lambda().eq(InspectDetail::getPlaceId,placeId);
        }
        if(StringUtils.isNotBlank(placeName)){
            queryWrapper.lambda().like(InspectDetail::getPlaceName,placeName);
        }
        if(deviceId != null){
            queryWrapper.lambda().eq(InspectDetail::getDeviceId,deviceId);
        }
        if(StringUtils.isNotBlank(deviceName)){
            queryWrapper.lambda().like(InspectDetail::getDeviceName,deviceName);
        }
        if(startTime == null){
            startTime = PeriodUtil.getTodayStartTime().toLocalDate();
        }
        if(endTime == null){
            endTime = PeriodUtil.getTodayEndTime().toLocalDate().plusDays(1);
        }
        queryWrapper.lambda().ge(InspectDetail::getCreateTime,startTime);
        queryWrapper.lambda().le(InspectDetail::getCreateTime,endTime);
        queryWrapper.lambda().orderByDesc(InspectDetail::getCreateTime);
        PageHelper.startPage(pageNum, pageSize);
        return (Page<InspectDetail>) inspectDetailMapper.selectList(queryWrapper);

    }

    public List<InspectDetail> getInspectListByPlaceId(Integer placeId, LocalDate startTime, LocalDate endTime, String inspectType){
        QueryWrapper<InspectDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(InspectDetail::getPlaceId,placeId);
        queryWrapper.lambda().eq(InspectDetail::getInspectType,inspectType);
        if(startTime != null){
            queryWrapper.lambda().ge(InspectDetail::getCreateTime,startTime);
        }
        if(endTime != null){
            queryWrapper.lambda().le(InspectDetail::getCreateTime,endTime.plusDays(1));
        }
        queryWrapper.lambda().orderByDesc(InspectDetail::getCreateTime);
        return inspectDetailMapper.selectList(queryWrapper);
    }

    public List<DeviceInspectInfo> getDeviceInspectInfos(String deviceType){
        QueryWrapper<DeviceInspectInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DeviceInspectInfo::getDeviceType,deviceType);
        queryWrapper.lambda().orderByAsc(DeviceInspectInfo::getCheckNo);
        return deviceInspectInfoMapper.selectList(queryWrapper);
    }



}
