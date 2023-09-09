package com.huajie.domain.common.pay;


import com.huajie.domain.common.exception.AppStarterException;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.AutoUpdateCertificatesVerifier;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PrivateKey;

/**
 * @author zhuxiaofeng
 * @date 2023/9/8
 */
@Configuration
public class WechatPayConfig {

    /**
     * 商户号
     */
    @Value("${wechat.pay.mchId:1651245161}")
    private String mchId;

    /**
     * 商户证书序列号
     */
    @Value("${wechat.pay.mchSerialNo:24D46ACF1CFDF15E584EA4D209C74F22729EE7C0}")
    private String mchSerialNo;

    /**
     * V3密钥
     */
    @Value("${wechat.pay.apiV3Key:YMIB54Dr8QvzrmTbw9nhCBL9dmRf4iPY}")
    private String apiV3Key;

    @Bean
    public CloseableHttpClient wechatPayhttpClient(){
        String privateKey = null;
        try {
            privateKey = getPrivateKey();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            throw new AppStarterException("项目启动失败,因:"+e.getMessage());
        }

        CloseableHttpClient httpClient = null;
        // 加载商户私钥（privateKey：私钥字符串）
        PrivateKey merchantPrivateKey = PemUtil
                .loadPrivateKey(new ByteArrayInputStream(privateKey.getBytes(StandardCharsets.UTF_8)));

        // 加载平台证书（mchId：商户号,mchSerialNo：商户证书序列号,apiV3Key：V3密钥）
        AutoUpdateCertificatesVerifier verifier = null;
        verifier = new AutoUpdateCertificatesVerifier(
                new WechatPay2Credentials(mchId, new PrivateKeySigner(mchSerialNo, merchantPrivateKey)),apiV3Key.getBytes(StandardCharsets.UTF_8));

        // 初始化httpClient
        httpClient = WechatPayHttpClientBuilder.create()
                .withMerchant(mchId, mchSerialNo, merchantPrivateKey)
                .withValidator(new WechatPay2Validator(verifier)).build();
        return httpClient;
    }


    /**
     * 获取私钥。
     *
     * @return 私钥对象
     */
    public static String getPrivateKey() throws IOException, URISyntaxException {

        // 获取ClassLoader
//        ClassLoader classLoader = WechatPayConfig.class.getClassLoader();
        ClassPathResource resource = new ClassPathResource("cert/apiclient_key.pem");

        // 通过ClassLoader获取资源文件的URL
//        URL resourceUrl = classLoader.getResource("cert/apiclient_key.pem");

//        String content = new String(Files.readAllBytes(Paths.get(resourceUrl.toURI())), StandardCharsets.UTF_8);

        // 读取资源文件内容
        byte[] data = FileCopyUtils.copyToByteArray(resource.getInputStream());
        String content = new String(data, StandardCharsets.UTF_8);
        String privateKey = content.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");

        return privateKey;
    }


//    /**
//     * 获取私钥。
//     *
//     * @return 私钥对象
//     */
//    public static PrivateKey getPrivateKey() throws IOException, URISyntaxException {
//
//        // 获取ClassLoader
//        ClassLoader classLoader = WechatPayConfig.class.getClassLoader();
//
//        // 通过ClassLoader获取资源文件的URL
//        URL resourceUrl = classLoader.getResource("cert/apiclient_key.pem");
//
//        String content = new String(Files.readAllBytes(Paths.get(resourceUrl.toURI())), StandardCharsets.UTF_8);
//        try {
//            String privateKey = content.replace("-----BEGIN PRIVATE KEY-----", "")
//                    .replace("-----END PRIVATE KEY-----", "")
//                    .replaceAll("\\s+", "");
//
//            KeyFactory kf = KeyFactory.getInstance("RSA");
//            return kf.generatePrivate(
//                    new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey)));
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException("当前Java环境不支持RSA", e);
//        } catch (InvalidKeySpecException e) {
//            throw new RuntimeException("无效的密钥格式");
//        }
//    }

}
