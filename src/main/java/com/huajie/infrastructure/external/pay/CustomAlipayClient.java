package com.huajie.infrastructure.external.pay;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.huajie.domain.common.exception.ServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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

    public AlipayTradePrecreateResponse execute(AlipayTradeAppPayModel model){
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
            log.error("预下单失败, cause: ", e);
            throw new ServerException("预下单失败");
        }
    }

}
