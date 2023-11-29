package com.huajie.domain.model;

import lombok.Data;

/**
 * @author zhuxiaofeng
 * @date 2023/11/29
 */
@Data
public class VerifyCodeCacheModel {

    /**
     * 验证码
     */
    private String code;

    /**
     * 是否已验证 0:未验证; 1:已验证
     */
    private Boolean verifyStatus;

}
