package com.huajie.infrastructure.external.sms;

import com.alibaba.fastjson.JSONObject;
import com.huajie.domain.common.utils.OkHttpUtil;
import com.huajie.infrastructure.external.sms.model.ZTResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 助通短信
 */
@Component
@Slf4j
public class ZTSmsClient implements ISmsClient {

    @Override
    public void sendSms(String mobile, String jsonCode){
        JSONObject jsonObject = JSONObject.parseObject(jsonCode);
        String code = (String) jsonObject.get("code");

        log.info("sendSmsCode mobile: {} code: {}", mobile, code);

        JSONObject requestBody = new JSONObject();
        requestBody.put("mobile", mobile);
        requestBody.put("content", "【蓝朋友智慧消防】您的验证码为：" + code);
        requestBody.put("username", "jhlpkjhy");

        Long tkey = System.currentTimeMillis() / 1000;
        requestBody.put("password", this.md5Hash("ee79e6e724de3863fd200c421c543d85" + tkey));
        requestBody.put("tKey", tkey);
        ZTResponseBody responseBody = null;
        try {
            responseBody = OkHttpUtil.post("https://api.mix2.zthysms.com/v2/sendSms", null, requestBody, ZTResponseBody.class);
        } catch (IOException e) {
            log.error("短信发送失败: ", e);
        }
        log.info("助通短信 response body: {}", responseBody);
    }

    // 对字符串进行MD5哈希
    private String md5Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());

            // 转换为32位小写表示
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
