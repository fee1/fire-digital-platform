package com.huajie.infrastructure.external.sms;

import com.aliyuncs.exceptions.ClientException;

public interface ISmsClient {

    void sendSms(String mobile, String content) throws ClientException;

}
