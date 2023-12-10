package com.huajie.domain.service;

import com.huajie.BaseTest;
import com.huajie.domain.entity.NotifyForNotice;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author zhuxiaofeng
 * @date 2023/12/10
 */
public class NotifyForNoticeServiceTest extends BaseTest {

    @Autowired
    private NotifyForNoticeService notifyForNoticeService;

    @Test
    public void insertBatch() {
        NotifyForNotice notifyForNotice = new NotifyForNotice();
        notifyForNotice.setNoticeId(1);
        notifyForNotice.setSendTime(new Date());
        notifyForNotice.setSendUserId(1);
        notifyForNotice.setUserId(1);
        notifyForNoticeService.insertBatch(Collections.singletonList(notifyForNotice));
    }
}