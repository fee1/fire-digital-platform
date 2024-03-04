package com.huajie.application.service;

import com.huajie.application.api.request.UsrLoginRequestVO;
import com.huajie.application.api.request.WechatEditUserInfoRequestVO;
import com.huajie.application.api.response.WechatAppLoginResponseVO;
import com.huajie.application.api.response.WechatGetPhoneResponseVO;
import com.huajie.application.api.response.WechatUserManagementResponseVO;
import com.huajie.domain.common.oauth2.token.WechatOAuth2AccessToken;
import com.huajie.domain.entity.User;
import com.huajie.domain.model.WechatAppLoginResponseDTO;
import com.huajie.domain.service.WechatService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhuxiaofeng
 * @date 2023/9/18
 */
@Service
public class WechatAppService {

    @Autowired
    private WechatService wechatService;

    public WechatAppLoginResponseVO appLogin(String jsCode) {
        WechatOAuth2AccessToken wechatOAuth2AccessToken = wechatService.appLogin(jsCode);
        WechatAppLoginResponseVO responseVO = new WechatAppLoginResponseVO();
        responseVO.setAccessToken(wechatOAuth2AccessToken.getValue());
        responseVO.setExpiresIn(wechatOAuth2AccessToken.getExpiresIn());
        responseVO.setScope(wechatOAuth2AccessToken.getScope());
        responseVO.setTokenType(wechatOAuth2AccessToken.getTokenType());
        responseVO.setSessionKey(wechatOAuth2AccessToken.getSessionKey());
        responseVO.setOpenId(wechatOAuth2AccessToken.getOpenId());
        return responseVO;
    }

    public WechatGetPhoneResponseVO userPhoneBinding(String jsCode, String openId) {
        String userPhoneNumber = wechatService.getUserPhoneNumber(jsCode, openId);
        WechatGetPhoneResponseVO wechatGetPhoneResponseVO = new WechatGetPhoneResponseVO();
        wechatGetPhoneResponseVO.setPhone(userPhoneNumber);
        return wechatGetPhoneResponseVO;
    }

    public void editUserInfo(WechatEditUserInfoRequestVO requestVO) {
        wechatService.editUserInfo(requestVO);
    }

    public List<WechatUserManagementResponseVO> userManagement() {
        List<WechatUserManagementResponseVO> responseVOList = this.wechatService.userManagement();
        return responseVOList;
    }

    public WechatAppLoginResponseVO login(UsrLoginRequestVO requestVO) {
        WechatAppLoginResponseVO responseVO = this.wechatService.login(requestVO);
        return responseVO;
    }
}
