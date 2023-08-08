package com.huajie.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huajie.domain.entity.SysDic;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


@Mapper
@Repository
public interface SysDicMapper extends BaseMapper<SysDic> {
}