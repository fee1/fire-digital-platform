package com.huajie.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huajie.application.api.response.statistic.DeviceStateCountResponseVO;
import com.huajie.domain.entity.Device;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Mapper
@Repository
public interface DeviceMapper extends BaseMapper<Device> {


    List<DeviceStateCountResponseVO> getDeviceStateCount(@Param("tenantId")Integer tenantId);
}