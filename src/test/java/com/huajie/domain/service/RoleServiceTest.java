package com.huajie.domain.service;

import com.huajie.BaseTest;
import com.huajie.domain.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author zhuxiaofeng
 * @date 2023/8/7
 */
public class RoleServiceTest extends BaseTest {

    @Autowired
    private RoleService roleService;

    @Test
    public void getRolesByTenantId() {
        List<Role> rolesByTenantId = roleService.getPageRolesByTenantId(2, 1, 100);
        System.out.println(rolesByTenantId);
    }
}