package com.huajie.domain.common.utils;

import com.huajie.domain.common.exception.ServerException;
import com.huajie.domain.common.oauth2.model.CustomizeGrantedAuthority;
import com.huajie.domain.entity.Tenant;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.Objects;

/**
 * @author zhuxiaofeng
 * @date 2023/9/3
 */
public class UserContext {

    public static boolean isLogin(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null){
            return false;
        }
        return true;
    }

    public static User getCurrentLoginUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (User) auth.getPrincipal();
    }

    public static CustomizeGrantedAuthority getCustomizeGrantedAuthority(){
        User currentLoginUser = getCurrentLoginUser();
        for (GrantedAuthority authority : currentLoginUser.getAuthorities()) {
            return (CustomizeGrantedAuthority) authority;
        }
        return null;
    }

    public static Integer getCurrentUserId(){
        CustomizeGrantedAuthority customizeGrantedAuthority = getCustomizeGrantedAuthority();
        if (!Objects.isNull(customizeGrantedAuthority)) {
            return customizeGrantedAuthority.getUserId();
        }else {
            throw new ServerException("没有登陆态");
        }
    }

    public static Tenant getCurrentTenant(){
        CustomizeGrantedAuthority authority = getCustomizeGrantedAuthority();
        return authority.getTenant();
    }

}
