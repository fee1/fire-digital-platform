package com.huajie.application.api.request;

import lombok.Data;

import javax.validation.constraints.Size;

/**
 * @author zhuxiaofeng
 * @date 2023/11/27
 */
@Data
public class SendVerificationCodeRequestVO {

    @Size(min = 11, max = 11)
    private String phone;

}
