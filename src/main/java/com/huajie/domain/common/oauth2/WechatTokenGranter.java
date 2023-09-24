package com.huajie.domain.common.oauth2;

import com.huajie.domain.common.constants.CommonConstants;
import com.huajie.domain.common.oauth2.token.WechatAuthenticationToken;
import com.huajie.domain.common.oauth2.token.WechatOAuth2AccessToken;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author zhuxiaofeng
 * @date 2023/9/23
 */
public class WechatTokenGranter extends AbstractTokenGranter {

    private static final String GRANT_TYPE = CommonConstants.WECHAT;


    public WechatTokenGranter(AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory) {
        super(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String, String> parameters = new LinkedHashMap<String, String>(tokenRequest.getRequestParameters());
        String username = parameters.get(CommonConstants.USERNAME);
        String openId = parameters.get(CommonConstants.OPEN_ID);

        AbstractAuthenticationToken userAuth = new WechatAuthenticationToken(username, openId);
        userAuth.setDetails(parameters);

        OAuth2Request oAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
        return new OAuth2Authentication(oAuth2Request, userAuth);
    }

    @Override
    public WechatOAuth2AccessToken grant(String grantType, TokenRequest tokenRequest) {
        OAuth2AccessToken grant = super.grant(grantType, tokenRequest);
        Map<String, String> parameters = new LinkedHashMap<String, String>(tokenRequest.getRequestParameters());
        WechatOAuth2AccessToken wechatOAuth2AccessToken = new WechatOAuth2AccessToken();
        BeanUtils.copyProperties(grant, wechatOAuth2AccessToken);
        String sessionKey = parameters.get(CommonConstants.SESSION_KEY);
        wechatOAuth2AccessToken.setSessionKey(sessionKey);
        return wechatOAuth2AccessToken;
    }
}
