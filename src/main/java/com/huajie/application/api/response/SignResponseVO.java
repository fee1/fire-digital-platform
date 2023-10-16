package com.huajie.application.api.response;

import lombok.Data;

/**
 * @author zhuxiaofeng
 * @date 2023/10/16
 */
@Data
public class SignResponseVO {

    private String accessKeyId;

    private String encodedPolicy;

    private String postSignature;

}
