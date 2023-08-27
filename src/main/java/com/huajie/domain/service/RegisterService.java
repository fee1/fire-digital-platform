package com.huajie.domain.service;

import com.huajie.domain.entity.Tenant;
import com.huajie.domain.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhuxiaofeng
 * @date 2023/8/27
 */
@Service
public class RegisterService {

    @Autowired
    private UserService userService;

    @Autowired
    private TenantService tenantService;

    public void regiestEnterprise(Tenant tenant, List<User> userList) {
        tenantService.add(tenant);
        userService.addUsers(userList);
    }

    public void regiestGoverment(Tenant tenant, List<User> userList) {
        tenantService.add(tenant);
        userService.addUsers(userList);
    }

}
