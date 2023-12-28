package com.huajie.domain.service;

import com.alibaba.fastjson.JSONObject;
import com.huajie.domain.common.constants.CommonConstants;
import com.huajie.domain.common.exception.ServerException;
import com.huajie.domain.common.utils.QRCodeUtils;
import com.huajie.infrastructure.external.oss.AliyunFileClient;
import com.huajie.infrastructure.external.oss.model.SignModel;
import com.huajie.infrastructure.external.sms.AliyunSmsClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author zhuxiaofeng
 * @date 2023/9/8
 */
@Service
@Slf4j
public class CommonService {

    @Autowired
    private AliyunFileClient aliyunFileClient;

    @Autowired
    private AliyunSmsClient aliyunSmsClient;

    @Value("${aliyunoos.config.url}")
    private String url;
    private Object sign;

    /**
     * 生成二维码图片并上传至阿里云
     * @param qrcode 二维码url
     * @param fileName 文件名
     * @return 图片地址
     */
    public String generateQrImageAndUpLoadAliyun(String qrcode, String fileName){
        //二维码图片生成
        BufferedImage image = null;
        try {
            image = QRCodeUtils.encode(qrcode, null, QRCodeUtils.IMG_RESOURCE_TYPE_LOCAL, false);
        }catch (Exception e){
            e.printStackTrace();
            log.error("图片生成失败: ", e);
            throw new ServerException("图片生成失败");
        }

        //二维码图片上传
        // 创建一个ByteArrayOutputStream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 将BufferedImage写入ByteArrayOutputStream
        try {
            ImageIO.write(image, "png", baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 将ByteArrayOutputStream的内容转换为InputStream
        InputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
        try {
            aliyunFileClient.upload(CommonConstants.QRCODE_FOLDER, fileName, inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("阿里云图片上传失败", e);
            throw new ServerException("阿里云图片上传失败");
        }
        return url + fileName;
    }

    public SignModel getSign() {
        return aliyunFileClient.getSign(CommonConstants.USERDATA_FOLDER);
    }

    public void sendSms(String phone, JSONObject param){
        try {
            aliyunSmsClient.sendSms(phone, param.toJSONString());
        }catch (Exception e){
            e.printStackTrace();
            log.error("短信发送失败: ", e);
            throw new ServerException(e.getMessage());
        }
    }

}
