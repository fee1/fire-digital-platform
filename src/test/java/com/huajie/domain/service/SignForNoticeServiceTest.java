package com.huajie.domain.service;


import com.huajie.BaseTest;
import com.huajie.domain.entity.SignForNotice;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


/**
 * @author zhuxiaofeng
 * @date 2023/12/10
 */
public class SignForNoticeServiceTest extends BaseTest {

    @Autowired
    private SignForNoticeService signForNoticeService;

    @Test
    public void insertBatch() {
        SignForNotice signForNotice = new SignForNotice();
        signForNotice.setSendUserId(1);
        signForNotice.setSendTime(new Date());
        signForNotice.setUserId(1);
        signForNotice.setNoticeId(1);
        List<SignForNotice> signForNoticeList = new ArrayList<>();
        signForNoticeList.add(signForNotice);
        signForNoticeService.insertBatch(signForNoticeList);
    }
}