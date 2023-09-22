package com.huajie.domain.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.huajie.domain.common.constants.InspectTypeConstants;
import com.huajie.domain.common.utils.PeriodUtil;
import com.huajie.domain.common.utils.UserContext;
import com.huajie.domain.entity.DeviceInspectInfo;
import com.huajie.domain.entity.InspectDetail;
import com.huajie.infrastructure.mapper.DeviceInspectInfoMapper;
import com.huajie.infrastructure.mapper.InspectDetailMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;


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

    public List<InspectDetail> getEnterpriseInspectList(Integer enterpriseId, LocalDateTime startTime, LocalDateTime endTime){
        QueryWrapper<InspectDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(InspectDetail::getEntTenantId, enterpriseId)
                .eq(InspectDetail::getInspectType,InspectTypeConstants.INSPECT);
        queryWrapper.lambda().gt(InspectDetail::getCreateTime,startTime);
        queryWrapper.lambda().lt(InspectDetail::getCreateTime,endTime);
        queryWrapper.lambda().orderByDesc(InspectDetail::getCreateTime);

        return inspectDetailMapper.selectList(queryWrapper);
    }

    public List<InspectDetail> getGovernmentInspectList(Integer enterpriseId, List<Integer> adminGovernmentIds, LocalDateTime startTime, LocalDateTime endTime){
        QueryWrapper<InspectDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(InspectDetail::getEntTenantId,enterpriseId)
                .eq(InspectDetail::getInspectType,InspectTypeConstants.INSPECT)
                .in(InspectDetail::getGovTenantId, adminGovernmentIds);
        queryWrapper.lambda().gt(InspectDetail::getCreateTime,startTime);
        queryWrapper.lambda().lt(InspectDetail::getCreateTime,endTime);
        queryWrapper.lambda().orderByDesc(InspectDetail::getCreateTime);

        return inspectDetailMapper.selectList(queryWrapper);
    }


    public Page<InspectDetail> getPageSelfCheckList(Integer pageNum, Integer pageSize,
                                                 LocalDateTime startTime, LocalDateTime endTime){
        QueryWrapper<InspectDetail> queryWrapper = new QueryWrapper<>();

        queryWrapper.lambda()
                .eq(InspectDetail::getEntTenantId, UserContext.getCurrentTenant().getId())
                .eq(InspectDetail::getInspectType,InspectTypeConstants.SELF_CHECK);
        if(startTime == null){
            startTime = PeriodUtil.getTodayStartTime();
        }
        if(endTime == null){
            endTime = PeriodUtil.getTodayEndTime();
        }
        queryWrapper.lambda().gt(InspectDetail::getCreateTime,startTime);
        queryWrapper.lambda().lt(InspectDetail::getCreateTime,endTime);
        queryWrapper.lambda().orderByDesc(InspectDetail::getCreateTime);
        PageHelper.startPage(pageNum, pageSize);
        return (Page<InspectDetail>) inspectDetailMapper.selectList(queryWrapper);

    }


    public Page<InspectDetail> getPagePatrolList(Integer pageNum, Integer pageSize,
                                                 Integer placeId, String placeName,
                                                 Integer deviceId, String deviceName,
                                                 LocalDateTime startTime, LocalDateTime endTime){
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
            startTime = PeriodUtil.getTodayStartTime();
        }
        if(endTime == null){
            endTime = PeriodUtil.getTodayEndTime();
        }
        queryWrapper.lambda().gt(InspectDetail::getCreateTime,startTime);
        queryWrapper.lambda().lt(InspectDetail::getCreateTime,endTime);
        queryWrapper.lambda().orderByDesc(InspectDetail::getCreateTime);
        PageHelper.startPage(pageNum, pageSize);
        return (Page<InspectDetail>) inspectDetailMapper.selectList(queryWrapper);

    }

    public List<InspectDetail> getInspectListByPlaceId(Integer placeId, LocalDateTime startTime, LocalDateTime endTime, String inspectType){
        QueryWrapper<InspectDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(InspectDetail::getPlaceId,placeId);
        queryWrapper.lambda().eq(InspectDetail::getInspectType,inspectType);
        if(startTime != null){
            queryWrapper.lambda().gt(InspectDetail::getCreateTime,startTime);
        }
        if(endTime != null){
            queryWrapper.lambda().lt(InspectDetail::getCreateTime,endTime);
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
