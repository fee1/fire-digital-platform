package com.huajie.domain.model;

import lombok.Data;

/**
 * @author zhuxiaofeng
 * @date 2023/9/23
 */
@Data
public class AccessTokenResponseDTO {

    /**
     * 获取到的凭证
     */
    private String accessToken;

    /**
     * 凭证有效时间，单位：秒。目前是7200秒之内的值。
     */
    private String expiresIn;

}
