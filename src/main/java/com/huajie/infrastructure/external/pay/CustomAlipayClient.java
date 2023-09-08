package com.huajie.infrastructure.external.pay;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.huajie.domain.common.exception.ServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author zhuxiaofeng
 * @date 2023/8/21
 */
@Component
@Slf4j
public class CustomAlipayClient {

    @Autowired
    private AlipayClient alipayClient;

    @Value("${alipay.notify.url:https://6cb0fd7a-369f-448c-8d0e-008ea7c00ece.mock.pstmn.io/alipay/scan/code/notify}")
    private String alipayNotifyUrl;

    /**
     * 预下单 生成二维码需要的链接
     * @param model
     * @return
     */
    public AlipayTradePrecreateResponse preOrder(AlipayTradeAppPayModel model){
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        request.setNotifyUrl(alipayNotifyUrl);
        request.setBizModel(model);
        try {
            AlipayTradePrecreateResponse response = alipayClient.execute(request);
            if(!response.isSuccess()){
                throw new ServerException("预下单失败, cause: "+ response.getSubMsg());
            }
            return response;
        }catch (AlipayApiException e){
            e.printStackTrace();
            log.error("预下单失败, cause: ", e);
            throw new ServerException("预下单失败");
        }
    }

    /**
     * 订单查询
     * @param outTradeNo 商家订单号
     */
    public AlipayTradeQueryResponse checkOrder(String outTradeNo) {
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("out_trade_no", outTradeNo);
        jsonObject.put("query_options", Collections.singletonList("trade_settle_info"));
        request.setBizContent(jsonObject.toJSONString());
        try {
            AlipayTradeQueryResponse response = alipayClient.execute(request);
            if (!response.isSuccess()){
                throw new ServerException("订单查询失败, cause: "+ response.getSubMsg());
            }
            return response;
        }catch (AlipayApiException e){
            e.printStackTrace();
            log.error("查询订单失败, cause: ", e);
            throw new ServerException("查询订单失败");
        }

    }
}
