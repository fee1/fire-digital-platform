package com.huajie.infrastructure.external.sms;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.huajie.BaseTest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author zhuxiaofeng
 * @date 2023/11/25
 */
public class SmsClientTest extends BaseTest {

    @Autowired
    private SmsClient smsClient;

    @Test
    @SneakyThrows
    public void sendSms() {
        //SMS_187745070
        //邹公子的桶装水微服务
        SendSmsResponse sendSmsResponse = smsClient.sendSms("17687374990","{\"code\":\"" + 1234 + "\"}");
        System.out.println(sendSmsResponse);
    }
}