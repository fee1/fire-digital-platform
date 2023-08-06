package com.huajie.application.service;

import com.huajie.application.api.request.LoginRequestVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

/**
 * @author zhuxiaofeng
 * @date 2023/8/6
 */
@Service
public class LoginAppService {

    @Autowired
    private UserAppService userAppService;

    public User login(String username, String password) {
        return userAppService.auth(username, password);
    }

}
