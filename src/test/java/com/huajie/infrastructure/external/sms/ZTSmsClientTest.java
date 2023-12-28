package com.huajie.infrastructure.external.sms;

import com.huajie.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ZTSmsClientTest extends BaseTest {

    @Autowired
    private ZTSmsClient ztSmsClient;

    @Test
    public void sendSmsCode() {
        ztSmsClient.sendSmsCode("17687374990", "123456");
    }
}