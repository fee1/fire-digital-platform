package com.huajie.application.service;

import com.huajie.domain.common.oauth2.UserOauth2ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author zhuxiaofeng
 * @date 2023/8/6
 */
@Service
public class UserAppService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserOauth2ServiceImpl userOauth2ServiceImpl;

    public User auth(String username, String password){
        UserDetails userDetails = userOauth2ServiceImpl.loadUserByUsername(username);
        if (StringUtils.equals(userDetails.getUsername(), username) && passwordEncoder.matches(password, userDetails.getPassword())) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = new User(username, password, auth.getAuthorities());
            return user;
        }
        return null;
    }

}
