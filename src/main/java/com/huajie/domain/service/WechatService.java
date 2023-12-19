package com.huajie.domain.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import com.huajie.application.api.request.WechatEditUserInfoRequestVO;
import com.huajie.application.api.response.WechatUserManagementResponseVO;
import com.huajie.domain.common.constants.CommonConstants;
import com.huajie.domain.common.exception.ServerException;
import com.huajie.domain.common.oauth2.token.WechatAuthenticationToken;
import com.huajie.domain.common.oauth2.token.WechatOAuth2AccessToken;
import com.huajie.domain.common.utils.ObjectReflectUtil;
import com.huajie.domain.common.utils.OkHttpUtil;
import com.huajie.domain.common.utils.UserContext;
import com.huajie.domain.entity.Role;
import com.huajie.domain.entity.User;
import com.huajie.domain.model.AccessTokenResponseDTO;
import com.huajie.domain.model.WechatAppLoginResponseDTO;
import com.huajie.domain.model.WechatPhoneResponseDTO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.stereotype.Service;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author zhuxiaofeng
 * @date 2023/9/18
 */
@Service
@Slf4j
public class WechatService {

    @Value("${wechat.app.id:}")
    private String appId;

    @Value("${wechat.app.secret:}")
    private String appSecret;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private TokenEndpoint tokenEndpoint;

    private final String WECHAT_GRANT_TYPE = "grant_type";

    private final String APP_ID = "appid";

    private final String SECRET = "secret";

    private final String JS_CODE = "js_code";

    private String accessToken;

    public WechatOAuth2AccessToken appLogin(String jsCode) {
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put(APP_ID, appId);
        requestParams.put(SECRET, appSecret);
        requestParams.put(JS_CODE, jsCode);
        requestParams.put(WECHAT_GRANT_TYPE, "authorization_code");
        try {
            JSON response = OkHttpUtil.get("https://api.weixin.qq.com/sns/jscode2session", null, requestParams);
            log.debug("jscode2session http response: {}", response.toJSONString());
            WechatAppLoginResponseDTO wechatAppLoginResponseDTO = response.toJavaObject(WechatAppLoginResponseDTO.class);
            log.debug("wechatAppLoginResponseDTO: {}", wechatAppLoginResponseDTO);
            String openid = wechatAppLoginResponseDTO.getOpenid();
            String sessionKey = wechatAppLoginResponseDTO.getSessionKey();
            User userByOpenId = userService.getUserByOpenId(openid);
//            System.out.println(this.login(userService.getUserByOpenId("oB7PV5fWmFxl9a7qVAljav9ZL4ys"), "oB7PV5fWmFxl9a7qVAljav9ZL4ys", ""));
            if (userByOpenId == null){
                WechatOAuth2AccessToken wechatOAuth2AccessToken = new WechatOAuth2AccessToken();
                wechatOAuth2AccessToken.setSessionKey(sessionKey);
                wechatOAuth2AccessToken.setOpenId(openid);
                return wechatOAuth2AccessToken;
            }else {
                return (WechatOAuth2AccessToken)this.login(userByOpenId, openid, sessionKey);
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("jscode2session ex:", e);
            throw new ServerException(e.getMessage());
        }
    }

    public OAuth2AccessToken login(User user, String openid, String sessionKey){
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(CommonConstants.CLIENT_ID)
                .password(CommonConstants.SECRET)
                .authorities(new ArrayList<>())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false).build();
        WechatAuthenticationToken wechatAuthenticationToken = new WechatAuthenticationToken(userDetails, null, null);
        Map<String, String> parameters = new HashMap<>();
        parameters.put(CommonConstants.SCOPE, CommonConstants.ALL);
        parameters.put(CommonConstants.GRANT_TYPE, CommonConstants.WECHAT);
        parameters.put(CommonConstants.USERNAME, user.getPhone());
        parameters.put(CommonConstants.OPEN_ID, openid);
        parameters.put(CommonConstants.SESSION_KEY, sessionKey);
        try {
            ResponseEntity<OAuth2AccessToken> responseEntity = tokenEndpoint.postAccessToken(wechatAuthenticationToken, parameters);
            return responseEntity.getBody();
        } catch (HttpRequestMethodNotSupportedException e) {
            e.printStackTrace();
            throw new ApiException("系统错误，认证失败");
        }
    }

    public String getUserPhoneNumber(String jsCode, String openId) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("code", jsCode);
        try {
            JSON response = OkHttpUtil.post("https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=" + this.accessToken, null, requestBody);
            log.debug("getuserphonenumber http response: {}", response.toJSONString());
            WechatPhoneResponseDTO wechatPhoneResponseDTO = response.toJavaObject(WechatPhoneResponseDTO.class);
            log.debug("wechatPhoneResponseDTO: {}", wechatPhoneResponseDTO);
            User userByPhone = userService.getUserByPhone(wechatPhoneResponseDTO.getPhoneInfo().getPhoneNumber());
            if (userByPhone != null){
                User user = new User();
                user.setId(userByPhone.getId());
                user.setOpenId(openId);
                userService.updateUser(user);
                return userByPhone.getPhone();
            }else {
                throw new ServerException("当前用户并没有注册到系统中！");
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("getuserphonenumber ex:", e);
            throw new ServerException(e.getMessage());
        }

    }

    @PostConstruct
    public synchronized void refreshAccessToken(){
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put(APP_ID, appId);
        requestParams.put(SECRET, appSecret);
        requestParams.put(WECHAT_GRANT_TYPE, "client_credential");
        try {
            JSON response = OkHttpUtil.get("https://api.weixin.qq.com/cgi-bin/token", null, requestParams);
            log.debug("token http response: {}", response.toJSONString());
            AccessTokenResponseDTO accessTokenResponseDTO = response.toJavaObject(AccessTokenResponseDTO.class);
            log.debug("accessTokenResponseDTO: {}", accessTokenResponseDTO);
            this.accessToken = accessTokenResponseDTO.getAccessToken();
        }catch (Exception e){
            e.printStackTrace();
            log.error("token ex:", e);
            throw new ServerException(e.getMessage());
        }
    }

    @SneakyThrows
    public void editUserInfo(WechatEditUserInfoRequestVO requestVO) {
        User user = new User();
        user.setId(UserContext.getCurrentUserId());
        user.setUserName(requestVO.getUsername());
        user.setHeadPic(requestVO.getHeadPic());
        this.userService.updateUser(user);
        org.springframework.security.core.userdetails.User currentUser = UserContext.getCurrentLoginUser();
        if (StringUtils.isNotBlank(requestVO.getUsername())) {
            ObjectReflectUtil.setFieldValue(currentUser, "username", requestVO.getUsername());
            Set<GrantedAuthority> authorities = (Set<GrantedAuthority>) currentUser.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                ObjectReflectUtil.setFieldValue(authority, "userName", requestVO.getUsername());
            }
        }
        if (StringUtils.isNotBlank(requestVO.getHeadPic())) {
            Set<GrantedAuthority> authorities = (Set<GrantedAuthority>) currentUser.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                ObjectReflectUtil.setFieldValue(authority, "headPic", requestVO.getHeadPic());
            }
        }
    }

    public List<WechatUserManagementResponseVO> userManagement() {
        Integer tenantId = UserContext.getCurrentTenant().getId();
        List<User> usersByTenantId = this.userService.getUsersByTenantId(tenantId);
        List<WechatUserManagementResponseVO> wechatUserManagementResponseVOList = new ArrayList<>();
        for (User user : usersByTenantId) {
            WechatUserManagementResponseVO wechatUserManagementResponseVO = new WechatUserManagementResponseVO();
            BeanUtils.copyProperties(user, wechatUserManagementResponseVO);
            Role role = this.roleService.getRoleById(user.getRoleId());
            wechatUserManagementResponseVO.setRoleCode(role.getRoleCode());
            wechatUserManagementResponseVOList.add(wechatUserManagementResponseVO);
        }
        return wechatUserManagementResponseVOList;
    }
}
