package com.huajie.domain.model;

import lombok.Data;

/**
 * @author zhuxiaofeng
 * @date 2023/9/24
 */
@Data
public class WechatPhoneResponseDTO {

    /**
     * 错误码
     */
    private String errcode;

    /**
     * 错误信息
     */
    private String errmsg;

    /**
     * 用户手机号信息
     */
    private PhoneInfoDTO phoneInfo;

}
