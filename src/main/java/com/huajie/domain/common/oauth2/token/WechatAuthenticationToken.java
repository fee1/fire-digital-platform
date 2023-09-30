package com.huajie.domain.common.oauth2.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author zhuxiaofeng
 * @date 2023/9/23
 */
public class WechatAuthenticationToken extends UsernamePasswordAuthenticationToken {


//    private final Object principal;
//
//    private Object credentials;


    public WechatAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
//        this.principal = principal;
//        this.credentials = credentials;
//        setAuthenticated(true);
    }

    public WechatAuthenticationToken(Object principal, Object credentials,
                                               Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
//        this.principal = principal;
//        this.credentials = credentials;
//        super.setAuthenticated(true); // must use super, as we override
    }

//    @Override
//    public Object getCredentials() {
//        return this.credentials;
//    }
//
//    @Override
//    public Object getPrincipal() {
//        return this.principal;
//    }
}
