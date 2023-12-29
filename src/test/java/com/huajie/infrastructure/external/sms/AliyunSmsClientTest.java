package com.huajie.infrastructure.external.sms;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.huajie.BaseTest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zhuxiaofeng
 * @date 2023/11/25
 */
public class AliyunSmsClientTest extends BaseTest {

    @Autowired
    private AliyunSmsClient aliyunSmsClient;

    @Test
    @SneakyThrows
    public void sendSms() {
        //SMS_187745070
        //邹公子的桶装水微服务
        aliyunSmsClient.sendSms("17687374990","{\"code\":\"" + 1234 + "\"}");
    }
}