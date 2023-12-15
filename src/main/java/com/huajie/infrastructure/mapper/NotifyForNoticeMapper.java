package com.huajie.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huajie.domain.entity.NotifyForNotice;
import com.huajie.domain.entity.NotifyForNoticeExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface NotifyForNoticeMapper extends BaseMapper<NotifyForNotice> {
    void insertBatch(List<NotifyForNotice> notifyForNoticeList);
}