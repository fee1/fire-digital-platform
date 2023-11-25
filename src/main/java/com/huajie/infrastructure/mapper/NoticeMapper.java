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


    @Select("<script>"+
            "SELECT t0.* FROM notice t0 LEFT JOIN user t1 ON t0.from_user_id = t1.id " +
            "WHERE t0.type = #{noticeType,jdbcType=INTEGER} AND t0.send_time = #{date, jdbcType=TIMESTAMP} AND t0.id IN "+
            "<foreach item='item' collection='noticeIds' open='(' separator=',' close=')'>"+
            "#{item}"+
            "</foreach>"+
            "AND t0.title LIKE concat('%',#{title, jdbcType=VARCHAR},'%') AND t1.user_name LIKE concat('%',#{sendUserName, jdbcType=VARCHAR},'%')"+
            "</script>")
    List<Notice> searchNotices(@Param("noticeType") Integer noticeType, @Param("date") Date date,@Param("title") String title,
                               @Param("sendUserName") String sendUserName,@Param("noticeIds") Set<Integer> noticeIds);

}