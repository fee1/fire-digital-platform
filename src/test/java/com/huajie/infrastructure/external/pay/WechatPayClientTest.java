package com.huajie.infrastructure.external.pay;

import com.huajie.BaseTest;
import com.huajie.domain.common.utils.QRCodeUtils;
import com.huajie.infrastructure.external.pay.model.AmountModel;
import com.huajie.infrastructure.external.pay.model.WechatPayCreateOrderModel;
import com.huajie.infrastructure.external.pay.model.WechatPayCreateRespModel;
import com.zxf.method.trace.util.TraceFatch;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;


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
        wechatPayCreateOrderModel.setAppId("wx857a855479f492ab");
        wechatPayCreateOrderModel.setDescription("测试使用");
        wechatPayCreateOrderModel.setMchId("1651245161");
        AmountModel amountModel = new AmountModel();
        amountModel.setTotal(1);
        wechatPayCreateOrderModel.setAmount(amountModel);
        String outTradeNo = TraceFatch.getTraceId();
        wechatPayCreateOrderModel.setOutTradeNo(outTradeNo);
        wechatPayCreateOrderModel.setNotifyUrl("https://6cb0fd7a-369f-448c-8d0e-008ea7c00ece.mock.pstmn.io/alipay/scan/code/notify");
        WechatPayCreateRespModel wechatPayCreateRespModel = wechatPayClient.preCreateOrder(wechatPayCreateOrderModel);
        System.out.println(wechatPayCreateRespModel);

        File output = new File("D:\\project\\fire-digital-platform\\test_qrcode\\" + outTradeNo + ".png");
        BufferedImage image = QRCodeUtils.encode(wechatPayCreateRespModel.getCodeUrl(), null, QRCodeUtils.IMG_RESOURCE_TYPE_LOCAL, false);
        ImageIO.write(image, "png", output);
    }

}