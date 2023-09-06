package com.huajie.domain.common.pay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.huajie.domain.common.exception.AppStarterException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhuxiaofeng
 * @date 2023/8/21
 */
@Configuration
public class AlipayConfig {

    /**
     * 默认使用沙箱模式的网关地址
     */
    @Value("${alipay.serverUrl:https://openapi-sandbox.dl.alipaydev.com/gateway.do}")
    private String serverUrl;

    @Value("${alipay.app-id:9021000126655234}")
    private String appId;

    @Value("${alipay.private-key:MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDl5OFFWeFEuwulyows30xlNZGD1/hv+LXF+aDxttX/wsYVeU+5J4H546MLkR/CnYcUxg4AQtiioZ1NGEHFRClU+3FY7k2X2AXteZnl3Gq51xN/oXvdTySp5Tx09k6IdWp9KtCKmS7EgESsz61N8DPqaHGRjzGOXGtXYF3ozgXwHr6XjlgBGhmyKRUo8hV//PkLfiUYymAsMufCR3VQtT9wwg3JUq07DIUoW6mJFSzJG5z8JOwK2scMtbAUti8TRzO1tISIbJsU0J8LaU2q1Zwl15uFuOWpZZJVzC5qmWuJq2LyJca4mluC9sYVoQbTcisl7DXICCA8r9mhchCVa9PRAgMBAAECggEAeOM4Sh2PRXMOFupBd78cDwRyNmA09bxU6Xvw0+oO8eTcAKK/9179DAhVzllL2Cy5wYS8DAsam7pcWxPsUe0bxuJJojf9eWFRDKUq9iJHxN2CFVU3KxLY9rXiaqayeTM1AginjJosa4v/kNfMC/XJdvPg9GfV6URT9aho+WiM2sxwtiDAl/BaW9n4b1TlKgWhgsNOQR/Vy3WfRy5Q+e5dDGuxXzStxccuB8mhrZmu4DbNHTRTQ0zg9szpbf6OpWewYNmRZUuuPOsI0AHrnHwjOyaM4Rtgsq/TnLQAd5eRqehSg4VDPYlc+s2o+JUatHgzazKo+MLEJRNqxNHKt9SAIQKBgQD5JUI56LPHkDlUcrCRD0oXoljP7MY0ZFyuQOY6p0d9CwPrZxOXMz6chwtVgt50NB173+ZrdG5OpR8PcwnhHfG72q0KezG1sWiWnBcZkjrA5CTgCSsd3BctW1sWNXai7fDAHnwjMqTyKbq+rGUjziGcnUWX3WRV+HbErbuL7B+hNQKBgQDsOAhKTe/k1+blWrZg3askvU3qEKYt3aaxGQrhIc37Aux1ta7CKN5qjALtcVoXhMV0gJeUwFG50nhctrXphpKmk6CLyNIZP9vvGJSw753Qqz5XxO9Y9C7WCSNeg326f4QMapD4eCbtBkDywhPxF+x3ZCHDyoz+KbiwdTd+qXO3rQKBgQCAM47nc3mG+Ay4CqgWjLKIhM6Ed4bqELasJ9MQXWFo05E5wXmQNRtDui2327aSPvro8iaEJo5YQ5k+/ugXgawRCQ4/tXK31slFrp4oV1mKRhs/eRhJriXy/e5Z+DrKzfszMLyZ9AZyYcypLtaAXSJoj3jvE0n8/vRGcktuTZtc/QKBgHFWzTuBu6nsob/IT5uYoGZHYlv9Uw7MvA9bOaodwyw+EeDEpK5KV7V9qmXtLlH8hZVJofFgeZYUU2YA0LXUuQat7KlFWPF+f3NwMt/WiL03fPk157Whq9tMlSjjJG9Yga5Hb3mYhOzTRFR2JIVYekAqzjyYMiA9On2IMHFYssodAoGAIH8IWWHKRWXvOrLx/7elxTgBzVKw1npzx2vsF4NWwc1SR9M/gC0rQLNeuHILQQNxVtqoPYOBCxLFVvDke+WaSi1IeVTFtWsA5WOK1AD25kMqi6+5z2wHsxDbVBulqjyD7PP8YXoO3KTiGrTO+8m97xaKpxP0ND7YS6JK5xXurPg=}")
    private String privateKey;

    @Value("${alipay.sign-type:RSA2}")
    private String signType;

    /**
     * 证书模式 使用  应用公钥证书
     */
    @Value("${alipay.app-cert-path:}")
    private String appCertPath;

    /**
     * 证书模式 使用  支付宝公钥证书
     */
    @Value("${alipay.public-cert-path:}")
    private String publicCertPath;

    /**
     * 证书模式 使用  支付宝根证书
     */
    @Value("${alipay.root-cert-path:}")
    private String rootCertPath;

    @Bean
    public AlipayClient alipayClient(){
        CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
        //设置网关地址
        certAlipayRequest.setServerUrl(serverUrl);
        //设置应用Id
        certAlipayRequest.setAppId(appId);
        //设置应用私钥
        certAlipayRequest.setPrivateKey(privateKey);
        //设置请求格式，固定值json
        certAlipayRequest.setFormat("json");
        //设置字符集
        certAlipayRequest.setCharset("UTF-8");
        //设置签名类型
        certAlipayRequest.setSignType(signType);
        //设置应用公钥证书路径
//        certAlipayRequest.setCertPath(appCertPath);
        //设置支付宝公钥证书路径
//        certAlipayRequest.setAlipayPublicCertPath(publicCertPath);
        //设置支付宝根证书路径
//        certAlipayRequest.setRootCertPath(rootCertPath);
        AlipayClient alipayClient = null;
        try {
            alipayClient = new DefaultAlipayClient(certAlipayRequest);
        } catch (AlipayApiException e) {
            throw new AppStarterException("创建支付client失败");
        }
        return alipayClient;
    }

}
