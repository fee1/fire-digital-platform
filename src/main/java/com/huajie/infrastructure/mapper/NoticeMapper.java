package com.huajie.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huajie.domain.entity.Notice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Mapper
@Repository
public interface NoticeMapper extends BaseMapper<Notice> {


    List<Notice> searchNotices(@Param("noticeType") Integer noticeType, @Param("startDate") Date startDate,
                               @Param("endDate") Date endDate,@Param("title") String title,
                               @Param("sendUserName") String sendUserName,@Param("noticeIds") Set<Integer> noticeIds);

}