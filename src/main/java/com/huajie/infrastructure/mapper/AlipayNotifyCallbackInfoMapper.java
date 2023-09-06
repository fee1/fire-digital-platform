package com.huajie.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huajie.domain.entity.AlipayNotifyCallbackInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AlipayNotifyCallbackInfoMapper extends BaseMapper<AlipayNotifyCallbackInfo> {

}