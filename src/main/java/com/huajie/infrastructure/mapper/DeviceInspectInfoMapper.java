package com.huajie.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huajie.domain.entity.DeviceInspectInfo;
import com.huajie.domain.entity.ProblemDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface DeviceInspectInfoMapper extends BaseMapper<DeviceInspectInfo> {
}
