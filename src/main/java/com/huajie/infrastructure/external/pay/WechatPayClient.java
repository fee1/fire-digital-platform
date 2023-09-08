package com.huajie.infrastructure.external.pay;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huajie.domain.common.exception.ServerException;
import com.huajie.infrastructure.external.pay.model.WechatPayCheckRespModel;
import com.huajie.infrastructure.external.pay.model.WechatPayCreateOrderModel;
import com.huajie.infrastructure.external.pay.model.WechatPayCreateRespModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author zhuxiaofeng
 * @date 2023/9/8
 */
@Component
@Slf4j
public class WechatPayClient {

    /**
     * 商户号
     */
    @Value("${wechat.pay.mchId:1651245161}")
    private String mchId;

    @Autowired
    private CloseableHttpClient wechatPayhttpClient;

    /**
     * 预下单
     * @param createOrderModel
     * @return 二维码链接
     * @throws Exception
     */
    public WechatPayCreateRespModel preCreateOrder(WechatPayCreateOrderModel createOrderModel){
        HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/pay/transactions/native");
        // 请求body参数
        StringEntity entity = new StringEntity(JSONObject.toJSONString(createOrderModel),"utf-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");

        //完成签名并执行请求

        CloseableHttpResponse response = null;
        String qrcode = "";
        try {
            response = wechatPayhttpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) { //处理成功
                log.debug("success,return body = " + EntityUtils.toString(response.getEntity()));
            } else if (statusCode == 204) { //处理成功，无返回Body
                log.debug("success");
            } else {
                log.debug("failed,resp code = " + statusCode+ ",return body = " + EntityUtils.toString(response.getEntity()));
                throw new ServerException("request failed");
            }
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(entity.getContent(), WechatPayCreateRespModel.class);
        } catch (Exception e){
            e.printStackTrace();
            log.error("预下单失败: ", e);
            throw new ServerException("预下单失败");
        }finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    log.error("流关闭异常: ", e);
                }
            }
        }
    }


    public WechatPayCheckRespModel checkOrder(String outTradeNo) {
        CloseableHttpResponse response = null;

        try {
            //请求URL
            URIBuilder uriBuilder = new URIBuilder("https://api.mch.weixin.qq.com/v3/pay/transactions/out-trade-no/" + outTradeNo);
            uriBuilder.setParameter("mchid", mchId);
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            httpGet.addHeader("Accept", "application/json");
            response = wechatPayhttpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                log.debug("success,return body = " + EntityUtils.toString(response.getEntity()));
            } else if (statusCode == 204) {
                log.debug("success");
            } else {
                log.debug("failed,resp code = " + statusCode+ ",return body = " + EntityUtils.toString(response.getEntity()));
                throw new IOException("request failed");
            }
            HttpEntity entity = response.getEntity();
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(entity.getContent(), WechatPayCheckRespModel.class);
        }catch (Exception e){
            e.printStackTrace();
            log.error("查询订单失败: ", e);
            throw new ServerException("查询订单失败");
        }finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    log.error("流关闭异常: ", e);
                }
            }
        }
    }
}
