package com.huajie.infrastructure.external.sms;

import com.alibaba.fastjson.JSONObject;
import com.huajie.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ZTSmsClientTest extends BaseTest {

    @Autowired
    private ZTSmsClient ztSmsClient;

    @Test
    public void sendSmsCode() {
        JSONObject param = new JSONObject();
        param.put("code", "123456");
        ztSmsClient.sendSms("17687374990", param.toJSONString());
    }
}