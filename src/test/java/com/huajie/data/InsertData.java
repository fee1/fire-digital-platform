package com.huajie.data;

import com.huajie.BaseTest;
import com.huajie.domain.entity.User;
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


    @Test
    public void insertUser(){
        User user = new User();
        user.setUserNo("1");
        user.setTenantId(1);
        user.setUserName("zxf");
        user.setPassword(passwordEncoder.encode("123456"));
        user.setPhone("17687374990");
        user.setEmail("1063446979@qq.com");
        user.setRoleId(1);
        userMapper.insert(user);
    }

}
