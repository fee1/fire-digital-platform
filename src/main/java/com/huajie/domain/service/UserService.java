package com.huajie.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import com.huajie.domain.entity.User;
import com.huajie.infrastructure.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

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

    public User getUserByUserNo(String userNo){
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.lambda().eq(User::getUserNo, userNo);
        return userMapper.selectOne(userQueryWrapper);
    }

    public List<User> getUsersByTenantId(Integer tenantId) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getTenantId, tenantId);
        return userMapper.selectList(queryWrapper);
    }


    public void addUser(User user) {
        User userByUserNo = this.getUserByUserNo(user.getUserNo());
        if (!Objects.isNull(userByUserNo)){
            throw new ApiException("用户账号不可与现存用户相同");
        }
        User userByEmail = this.getUserByEmail(user.getEmail());
        if (!Objects.isNull(userByEmail)){
            throw new ApiException("用户邮箱不可与现存用户相同");
        }
        User userByPhone = this.getUserByPhone(user.getPhone());
        if (!Objects.isNull(userByPhone)){
            throw new ApiException("用户手机号不可与现存用户相同");
        }
        userMapper.insert(user);
    }

    public void updateUser(User user) {
        User userByEmail = this.getUserByEmail(user.getEmail());
        if (!Objects.isNull(userByEmail)){
            throw new ApiException("用户邮箱不可与现存用户相同");
        }
        User userByPhone = this.getUserByPhone(user.getPhone());
        if (!Objects.isNull(userByPhone)){
            throw new ApiException("用户手机号不可与现存用户相同");
        }
        User currentUserInfo = userMapper.selectById(user.getId());
        currentUserInfo.setEmail(user.getEmail());
        currentUserInfo.setPhone(user.getPhone());
        currentUserInfo.setUserName(user.getUserName());
        currentUserInfo.setRoleId(user.getRoleId());
        userMapper.updateById(currentUserInfo);
    }

    public User getUserById(Integer userId) {
        return userMapper.selectById(userId);
    }
}
