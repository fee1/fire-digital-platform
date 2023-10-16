package com.huajie.infrastructure.external.oss.model;

import lombok.Data;

/**
 * @author zhuxiaofeng
 * @date 2023/10/16
 */
@Data
public class SignModel {

    private String accessKeyId;

    private String encodedPolicy;

    private String postSignature;

}
