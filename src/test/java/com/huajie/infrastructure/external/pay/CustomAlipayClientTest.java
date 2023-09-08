package com.huajie.infrastructure.external.pay;

import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.huajie.BaseTest;
import com.huajie.domain.common.utils.QRCodeUtils;
import com.zxf.method.trace.util.TraceFatch;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;


/**
 * @author zhuxiaofeng
 * @date 2023/9/5
 */
public class CustomAlipayClientTest extends BaseTest {

    @Autowired
    private CustomAlipayClient customAlipayClient;

    @Test
    @SneakyThrows
    public void execute() {
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参
        // 方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        String outTradeNo = "my_" + TraceFatch.getTraceId();
        model.setOutTradeNo(outTradeNo);
        model.setTotalAmount("1");
        model.setSubject("测试预下单"+outTradeNo);
        AlipayTradePrecreateResponse response = customAlipayClient.preCreateOrder(model);
        System.out.println(response.getQrCode());
        File output = new File("D:\\project\\fire-digital-platform\\test_qrcode\\" + outTradeNo + ".png");
        BufferedImage image = QRCodeUtils.encode(response.getQrCode(), null, QRCodeUtils.IMG_RESOURCE_TYPE_LOCAL, false);
        ImageIO.write(image, "png", output);
    }
}