package com.huajie.domain.common.oauth2.token;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;

/**
 * @author zhuxiaofeng
 * @date 2023/9/23
 */
public class WechatOAuth2AccessToken extends DefaultOAuth2AccessToken {

    @Setter
    @Getter
    private String sessionKey;

    @Getter
    @Setter
    private String openId;

    public WechatOAuth2AccessToken() {
        super((String) null);
    }

}
