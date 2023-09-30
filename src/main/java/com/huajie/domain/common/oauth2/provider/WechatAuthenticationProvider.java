package com.huajie.domain.common.oauth2.provider;

import com.huajie.domain.common.exception.ServerException;
import com.huajie.domain.common.oauth2.token.WechatAuthenticationToken;
import com.huajie.domain.entity.User;
import com.huajie.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/**
 * oauth2正牌用户认证使用方式
 *
 * @author zhuxiaofeng
 * @date 2023/9/29
 */
//@Component
public class WechatAuthenticationProvider extends DaoAuthenticationProvider {

//    @Autowired
//    @Qualifier("userOauth2ServiceImpl")
//    private UserDetailsService userDetailsService;

//    @Autowired
//    private UserService userService;

//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        WechatAuthenticationToken wechatAuthenticationToken = (WechatAuthenticationToken) authentication;
//        String username = (String) wechatAuthenticationToken.getPrincipal();
//        String openId = (String) wechatAuthenticationToken.getCredentials();
//        User userByOpenId = userService.getUserByOpenId(openId);
//        if (userByOpenId == null){
//            throw new ServerException("对应 openId 没有查询到这个用户");
//        }
//        return wechatAuthenticationToken;
//    }


    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
//        super.additionalAuthenticationChecks(userDetails, authentication);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return WechatAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
