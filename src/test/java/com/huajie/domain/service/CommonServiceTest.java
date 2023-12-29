package com.huajie.domain.service;


import com.alibaba.fastjson.JSONObject;
import com.huajie.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CommonServiceTest extends BaseTest {

    @Autowired
    private CommonService commonService;

    @Test
    public void sendSms() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", "123445");
        commonService.sendSms("17687374990", jsonObject);
    }
}