package com.huajie.infrastructure.external.oss;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.aliyun.oss.model.PutObjectResult;
import com.huajie.domain.common.constants.CommonConstants;
import com.huajie.infrastructure.external.oss.model.SignModel;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Date;

@Component
@ConfigurationProperties(prefix = "aliyunoos.config")
@Data
public class AliyunFileClient {

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

    /**
     * 上传文件
     * @param filename 文件名
     * @param objectName 文件夹名字
     * @param inputStream 文件流
     * @throws Exception
     */
    public void upload(String objectName, String filename ,InputStream inputStream)throws Exception{
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        PutObjectResult result = ossClient.putObject(bucketName, objectName+filename,  bufferedInputStream);
        // 关闭OSSClient。
        ossClient.shutdown();
    }

    /**
     * 删除文件
     * @param objectName 文件夹名字
     * @param filename 文件名
     * @throws Exception
     */
    public void delete(String objectName, String filename)throws Exception{
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        ossClient.deleteObject(bucketName, objectName+filename);
        ossClient.shutdown();
    }

    public SignModel getSign() {
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        long expireTime = 30;
        long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
        Date expiration = new Date(expireEndTime);
        PolicyConditions policyConds = new PolicyConditions();
        policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
        policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, CommonConstants.OTHER_FOLDER);//根据参数dir计算的policy，如果和前端uploadfile中参数key的相应字段不一致的话是会报错的

        String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
        byte[] binaryData = postPolicy.getBytes();
        String encodedPolicy = BinaryUtil.toBase64String(binaryData);
        String postSignature = ossClient.calculatePostSignature(postPolicy);
        ossClient.shutdown();

        SignModel signModel = new SignModel();
        signModel.setAccessKeyId(accessKeyId);
        signModel.setEncodedPolicy(encodedPolicy);
        signModel.setPostSignature(postSignature);
        return signModel;
    }
}
