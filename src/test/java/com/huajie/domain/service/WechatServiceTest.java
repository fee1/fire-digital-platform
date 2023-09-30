package com.huajie.domain.service;

import com.huajie.BaseTest;
import com.huajie.domain.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author zhuxiaofeng
 * @date 2023/9/29
 */
public class WechatServiceTest extends BaseTest {

    @Autowired
    private WechatService wechatService;

    @Autowired
    private UserService userService;


    @Test
    public void login() {
        String openId = "oB7PV5fWmFxl9a7qVAljav9ZL4ys";
        User user = userService.getUserByOpenId(openId);
        OAuth2AccessToken login = wechatService.login(user, openId, "");
        System.out.println(login);
    }
}