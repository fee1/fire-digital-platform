package com.huajie.domain.service;


import com.huajie.BaseTest;
import com.huajie.application.api.LoginApi;
import com.huajie.application.api.request.LoginRequestVO;
import com.huajie.application.api.response.LoginResponseVO;
import com.huajie.application.service.LoginAppService;
import com.huajie.domain.entity.Tenant;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequestWrapper;
import java.util.ArrayList;
import java.util.List;

public class GovermentOrganizationServiceTest extends BaseTest {

    @Autowired
    private GovermentOrganizationService govermentOrganizationService;

    @Autowired
    private LoginAppService loginAppService;

    @Before
    public void userInfo(){
        LoginRequestVO loginRequestVO = new LoginRequestVO();
        loginRequestVO.setUsername("18039596250");
        loginRequestVO.setPassword("123456");
        LoginResponseVO login = loginAppService.login("admin", "admin", loginRequestVO);


        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername("admin")
                .password("admin")
                .authorities(new ArrayList<>())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false).build();
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, null);
        SecurityContextImpl securityContext = new SecurityContextImpl(usernamePasswordAuthenticationToken);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void getAdminEnterpriseList() {
        List<Tenant> adminEnterpriseList = govermentOrganizationService.getAdminEnterpriseList();
        System.out.println(adminEnterpriseList);
    }

}