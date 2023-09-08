package com.huajie.infrastructure.external.pay;

import com.huajie.BaseTest;
import com.huajie.infrastructure.external.pay.model.AmountModel;
import com.huajie.infrastructure.external.pay.model.WechatPayCreateOrderModel;
import com.huajie.infrastructure.external.pay.model.WechatPayCreateRespModel;
import com.zxf.method.trace.util.TraceFatch;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author zhuxiaofeng
 * @date 2023/9/8
 */
public class WechatPayClientTest extends BaseTest {

    @Autowired
    private WechatPayClient wechatPayClient;

    @SneakyThrows
    @Test
    public void preCreateOrderTest(){
        WechatPayCreateOrderModel wechatPayCreateOrderModel = new WechatPayCreateOrderModel();
        wechatPayCreateOrderModel.setAppId("");
        wechatPayCreateOrderModel.setDescription("测试使用");
        wechatPayCreateOrderModel.setMchId("1651245161");
        AmountModel amountModel = new AmountModel();
        amountModel.setTotal(1);
        wechatPayCreateOrderModel.setAmount(amountModel);
        wechatPayCreateOrderModel.setOutTradeNo(TraceFatch.getTraceId());
        wechatPayCreateOrderModel.setNotifyUrl("https://6cb0fd7a-369f-448c-8d0e-008ea7c00ece.mock.pstmn.io/alipay/scan/code/notify");
        WechatPayCreateRespModel wechatPayCreateRespModel = wechatPayClient.preCreateOrder(wechatPayCreateOrderModel);
        System.out.println(wechatPayCreateRespModel);
    }

}