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

import javax.servlet.http.HttpServletRequestWrapper;
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
    }

    @Test
    public void getAdminEnterpriseList() {
        List<Tenant> adminEnterpriseList = govermentOrganizationService.getAdminEnterpriseList();
        System.out.println(adminEnterpriseList);
    }

}