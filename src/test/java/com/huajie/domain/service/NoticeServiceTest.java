package com.huajie.domain.service;


import com.huajie.BaseTest;
import com.huajie.domain.model.SysCreateNotice;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

public class NoticeServiceTest extends BaseTest {

    @Autowired
    private NoticeService noticeService;

    @Test
    public void sysCreateAndPublicNotice() {
        SysCreateNotice sysCreateNotice = new SysCreateNotice();
        sysCreateNotice.setUserIdList(Arrays.asList(1));
        sysCreateNotice.setType(1);
        sysCreateNotice.setTitle("sysy test");
        sysCreateNotice.setText("sys test");
        sysCreateNotice.setSaveDays(365);
        noticeService.sysCreateAndPublicNotice(sysCreateNotice);
    }
}