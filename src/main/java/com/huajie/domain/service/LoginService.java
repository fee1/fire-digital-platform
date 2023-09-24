package com.huajie.domain.service;

import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import com.huajie.domain.common.constants.CommonConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.stereotype.Service;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhuxiaofeng
 * @date 2023/8/8
 */
@Service
public class LoginService {

    @Autowired
    private ConsumerTokenServices consumerTokenServices;

    @Autowired
    private TokenEndpoint tokenEndpoint;

    public OAuth2AccessToken login(String clientId, String secret, String username, String password) {
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(clientId)
                .password(secret)
                .authorities(new ArrayList<>())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false).build();
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, null);
        Map<String, String> parameters = new HashMap<>();
        parameters.put(CommonConstants.SCOPE, CommonConstants.ALL);
        parameters.put(CommonConstants.GRANT_TYPE, CommonConstants.PASSWORD);
        parameters.put(CommonConstants.USERNAME, username);
        parameters.put(CommonConstants.PASSWORD, password);
        try {
            ResponseEntity<OAuth2AccessToken> responseEntity = tokenEndpoint.postAccessToken(usernamePasswordAuthenticationToken, parameters);
            return responseEntity.getBody();
        } catch (HttpRequestMethodNotSupportedException e) {
            e.printStackTrace();
            throw new ApiException("系统错误，认证失败");
        }
    }

    public void logout() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //清除认证
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
        String tokenValue = details.getTokenValue();
        consumerTokenServices.revokeToken(tokenValue);
    }

}
