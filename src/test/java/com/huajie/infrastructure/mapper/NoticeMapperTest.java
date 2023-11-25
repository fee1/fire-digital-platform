package com.huajie.infrastructure.mapper;

import com.huajie.BaseTest;
import com.huajie.domain.entity.Notice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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

    @Test
    public void getGovPcNoticeList() {
        Set<Integer> set = new HashSet<>();
        set.addAll(Arrays.asList(1,2,3,4));
        List<Notice> fasdf = noticeMapper.searchNotices(1, new Date(), "1122", "fasdf", set);

    }
}