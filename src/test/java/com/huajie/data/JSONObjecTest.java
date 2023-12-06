package com.huajie.data;

import com.alibaba.fastjson.JSONObject;
import com.huajie.domain.entity.AlipayNotifyCallbackInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhuxiaofeng
 * @date 2023/9/6
 */
public class JSONObjecTest {

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        map.put("gmt_create", "2023-09-05 17:40:37");
        map.put("out_trade_no", "123456");
        map.put("fasdfasdf", "ffasfdaf");
        JSONObject jsonObject = new JSONObject();
        jsonObject.putAll(map);
        AlipayNotifyCallbackInfo alipayNotifyCallbackInfo = jsonObject.toJavaObject(AlipayNotifyCallbackInfo.class);
        System.out.println(alipayNotifyCallbackInfo);

        //test
    }

}
