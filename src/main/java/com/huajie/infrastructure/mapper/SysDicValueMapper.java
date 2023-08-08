package com.huajie.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huajie.domain.entity.SysDicValue;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SysDicValueMapper extends BaseMapper<SysDicValue> {
}