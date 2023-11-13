package com.huajie.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huajie.domain.entity.SignForNotice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Mapper
@Repository
public interface SignForNoticeMapper extends BaseMapper<SignForNotice> {

    void InsertBatch(List<SignForNotice> signForNotices);
}