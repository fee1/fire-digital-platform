package com.huajie.infrastructure.external.oss;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PutObjectResult;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.InputStream;

@Component
@ConfigurationProperties(prefix = "aliyunoos.config")
@Data
public class AliyunFileClient {

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
    private String objectName;

    /**
     * 上传文件
     * @param filename 文件名
     * @param inputStream 文件流
     * @throws Exception
     */
    public void upload(String filename, InputStream inputStream)throws Exception{
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        PutObjectResult result = ossClient.putObject(bucketName, objectName+filename,  bufferedInputStream);
        // 关闭OSSClient。
        ossClient.shutdown();
    }

    /**
     * 删除文件
     * @param filename
     * @throws Exception
     */
    public void delete(String filename)throws Exception{
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        ossClient.deleteObject(bucketName, objectName+filename);
        ossClient.shutdown();
    }

}
