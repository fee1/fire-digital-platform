package com.huajie.application.service;

import com.huajie.application.api.request.LoginRequestVO;
import com.huajie.application.api.response.LoginResponseVO;
import com.huajie.domain.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;

/**
 * @author zhuxiaofeng
 * @date 2023/8/8
 */
@Service
public class LoginAppService {

    @Autowired
    private LoginService loginService;

    public LoginResponseVO login(String clientId, String secret, LoginRequestVO requestVO) {
        OAuth2AccessToken token = loginService.login(clientId, secret, requestVO.getUsername(), requestVO.getPassword());
        LoginResponseVO loginResponseVO = new LoginResponseVO();
        loginResponseVO.setAccessToken(token.getValue());
        loginResponseVO.setExpiresIn(token.getExpiresIn());
        loginResponseVO.setScope(token.getScope());
        loginResponseVO.setTokenType(token.getTokenType());
        return loginResponseVO;
    }

    public void logout() {
        loginService.logout();
    }

}
