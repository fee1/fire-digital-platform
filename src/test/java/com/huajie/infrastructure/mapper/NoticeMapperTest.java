package com.huajie.infrastructure.mapper;

import com.github.pagehelper.PageHelper;
import com.huajie.BaseTest;
import com.huajie.domain.entity.Notice;
import com.huajie.domain.service.NoticeService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author zhuxiaofeng
 * @date 2023/11/24
 */
public class NoticeMapperTest extends BaseTest {

    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private NoticeService noticeService;

    @Test
    @SneakyThrows
    public void getGovPcNoticeList() {
        Set<Integer> set = new HashSet<>();
        set.addAll(Arrays.asList(1,2,3,4));
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date parse = simpleDateFormat.parse("2023-12-01 10:56:10");
        PageHelper.startPage(1, 10);
        List<Notice> fasdf = noticeMapper.searchNotices(1, "1122", "fasdf", set);

//        noticeService.getGovPcNoticeList(1, date, "1122", "fasdf", 1, 10);
    }
}