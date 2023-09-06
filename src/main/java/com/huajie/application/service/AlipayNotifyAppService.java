package com.huajie.application.service;

import com.alibaba.fastjson.JSONObject;
import com.huajie.domain.entity.AlipayNotifyCallbackInfo;
import com.huajie.domain.service.AlipayNotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author zhuxiaofeng
 * @date 2023/9/6
 */
@Service
public class AlipayNotifyAppService {

    @Autowired
    private AlipayNotifyService alipayNotifyService;

    public void prePayQrcode(Map<String, String> requestVO) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.putAll(requestVO);
        AlipayNotifyCallbackInfo alipayNotifyCallbackInfo = jsonObject.toJavaObject(AlipayNotifyCallbackInfo.class);
        alipayNotifyCallbackInfo.setRequestOriginal(jsonObject.toJSONString());

        alipayNotifyService.prePayQrcode(alipayNotifyCallbackInfo);
    }

}
