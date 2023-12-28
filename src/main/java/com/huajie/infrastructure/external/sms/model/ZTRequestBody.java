package com.huajie.infrastructure.external.sms.model;

import lombok.Data;

@Data
public class ZTRequestBody {

    /**
     * 用户名   ee79e6e724de3863fd200c421c543d85
     */
    private String username = "jhlpkjhy";

    /**
     * 密码，密码采用32位小写MD5二次加密，
     */
    private String password = "";

    /**
     * tKey为东八区当前时间戳，精确到秒，10位长度。
     */
    private Long tKey = System.currentTimeMillis();

    /**
     * 手机号码，多个号码用英文逗号分隔，最多支持2000个号码，重复号码系统自动忽略。例:13500000000,13000000000
     */
    private String mobile;

    /**
     * 短信内容，最多支持1000个字符，例：【签名A】短信内容。
     */
    private String content;

}
