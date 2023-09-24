package com.huajie.domain.model;

import lombok.Data;

/**
 * @author zhuxiaofeng
 * @date 2023/9/24
 */
@Data
public class PhoneInfoDTO {

    /**
     * 用户绑定的手机号（国外手机号会有区号）
     */
    private String phoneNumber;

    /**
     * 没有区号的手机号
     */
    private String purePhoneNumber;

    /**
     * 区号
     */
    private String countryCode;

}
