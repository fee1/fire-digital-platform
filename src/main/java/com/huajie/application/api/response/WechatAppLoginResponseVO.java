package com.huajie.application.api.response;

import lombok.Data;

/**
 * @author zhuxiaofeng
 * @date 2023/9/18
 */
@Data
public class WechatAppLoginResponseVO extends LoginResponseVO {

    private String sessionKey;

    private String openId;

}
