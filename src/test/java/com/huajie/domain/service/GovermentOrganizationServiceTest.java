package com.huajie.domain.service;


import com.huajie.BaseTest;
import com.huajie.application.api.LoginApi;
import com.huajie.application.api.request.LoginRequestVO;
import com.huajie.application.api.response.LoginResponseVO;
import com.huajie.application.service.LoginAppService;
import com.huajie.domain.common.oauth2.model.CustomizeGrantedAuthority;
import com.huajie.domain.common.utils.UserContext;
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
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequestWrapper;
import java.util.ArrayList;
import java.util.List;

public class GovermentOrganizationServiceTest extends BaseTest {

    @Autowired
    private GovermentOrganizationService govermentOrganizationService;

    @Autowired
    private LoginAppService loginAppService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Before
    public void userInfo(){
//        LoginRequestVO loginRequestVO = new LoginRequestVO();
//        loginRequestVO.setUsername("18039596250");
//        loginRequestVO.setPassword("123456");
//        LoginResponseVO login = loginAppService.login("admin", "admin", loginRequestVO);

        UserDetails userDetails = userDetailsService.loadUserByUsername("18811110050");
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(usernamePasswordAuthenticationToken);
    }

    @Test
    public void getAdminEnterpriseList() {
        List<Tenant> adminEnterpriseList = govermentOrganizationService
                .getAdminEnterpriseList(1, Integer.MAX_VALUE, "", "", 1);

        List<Tenant> adminGovernmentList = govermentOrganizationService
                .getAdminGovernmentList(1, Integer.MAX_VALUE, "");

        List<Tenant> adminEnterpriseList1 = govermentOrganizationService
                .getAdminEnterpriseList(1, Integer.MAX_VALUE, "", "", 1);

        List<Tenant> adminGovernmentList2 = govermentOrganizationService
                .getAdminGovernmentList(1, Integer.MAX_VALUE, "");


//        List<Tenant> adminEnterpriseList2 = govermentOrganizationService.getAdminEnterpriseList();
        System.out.println(adminEnterpriseList);
    }

}