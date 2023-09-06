package com.huajie.infrastructure.external.oss;

import com.huajie.BaseTest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author zhuxiaofeng
 * @date 2023/9/6
 */
public class AliyunFileClientTest extends BaseTest {

    @Autowired
    private AliyunFileClient aliyunFileClient;

    @Test
    @SneakyThrows
    public void upload() {
        File file = new File("D:\\project\\fire-digital-platform\\test_qrcode\\my_118602652131418112.png");
        InputStream inputStream = new FileInputStream(file);
        aliyunFileClient.upload(file.getName(), inputStream);
    }
}