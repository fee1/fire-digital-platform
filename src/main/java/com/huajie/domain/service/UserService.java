package com.huajie.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huajie.domain.entity.User;
import com.huajie.infrastructure.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhuxiaofeng
 * @date 2023/8/6
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User getUserByPhone(String phone){
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.lambda().eq(User::getPhone, phone);
        return userMapper.selectOne(userQueryWrapper);
    }

    public User getUserByEmail(String email) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.lambda().eq(User::getEmail, email);
        return userMapper.selectOne(userQueryWrapper);
    }

    public List<User> getUsersByTenantId(Integer tenantId) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getTenantId, tenantId);
        return userMapper.selectList(queryWrapper);
    }
}
