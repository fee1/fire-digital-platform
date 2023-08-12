package com.huajie.data;

import com.huajie.BaseTest;
import com.huajie.domain.entity.Function;
import com.huajie.domain.entity.Role;
import com.huajie.domain.entity.User;
import com.huajie.infrastructure.mapper.FunctionMapper;
import com.huajie.infrastructure.mapper.RoleMapper;
import com.huajie.infrastructure.mapper.UserMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author zhuxiaofeng
 * @date 2023/8/6
 */
public class InsertData extends BaseTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private FunctionMapper functionMapper;


    @Test
    public void insertUser(){
        User user = new User();
        user.setUserNo("1");
        user.setTenantId(1);
        user.setUserName("MRL");
        user.setPassword(passwordEncoder.encode("123456"));
        user.setPhone("19089890980");
        user.setEmail("test@qq.com");
        user.setRoleId(2);
        userMapper.insert(user);
    }

    @Test
    public void insertRole(){
        Role role = new Role();
        role.setRoleCode("asdfaf");
        role.setRoleName("中文2");
        role.setTenantId(1);
        role.setDescription("中文2");
        role.setCreateUser("system");
        roleMapper.insert(role);
    }



}
