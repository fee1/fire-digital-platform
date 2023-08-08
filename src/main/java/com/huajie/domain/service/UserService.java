package com.huajie.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import com.huajie.domain.common.oauth2.model.CustomizeGrantedAuthority;
import com.huajie.domain.entity.User;
import com.huajie.infrastructure.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.stereotype.Service;

import java.util.Date;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ConsumerTokenServices consumerTokenServices;

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
        user.setCreateTime(new Date());
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
        if (Objects.isNull(currentUserInfo)){
            throw new ApiException("用户不存在");
        }

        User updateUserInfo = new User();
        updateUserInfo.setEmail(user.getEmail());
        updateUserInfo.setPhone(user.getPhone());
        updateUserInfo.setUserName(user.getUserName());
        updateUserInfo.setRoleId(user.getRoleId());
        updateUserInfo.setUpdateUser(user.getUpdateUser());
        updateUserInfo.setUpdateTime(new Date());

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getId, user.getId());
        userMapper.update(updateUserInfo, queryWrapper);
    }

    public User getUserById(Integer userId) {
        return userMapper.selectById(userId);
    }

    public void changePassword(Integer userId, String password) {
        User user = userMapper.selectById(userId);
        if (Objects.isNull(user)){
            throw new ApiException("用户不存在");
        }
        User updateInfo = new User();
        updateInfo.setPassword(passwordEncoder.encode(password));
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getId, userId);
        userMapper.update(updateInfo, queryWrapper);
    }

    public void logout() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //清除认证
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
        String tokenValue = details.getTokenValue();
        consumerTokenServices.revokeToken(tokenValue);
    }

    public org.springframework.security.core.userdetails.User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        org.springframework.security.core.userdetails.User user =
                (org.springframework.security.core.userdetails.User) auth.getPrincipal();
        return user;
    }

    public List<User> getTenantUsers(String userNo, String phone, String userName) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomizeGrantedAuthority authorities = (CustomizeGrantedAuthority) auth.getAuthorities();
        Integer tenantId = authorities.getTenant().getId();

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getTenantId, tenantId);
        if (StringUtils.isNotBlank(userNo)){
            queryWrapper.lambda().eq(User::getUserNo, userNo);
        }
        if (StringUtils.isNotBlank(phone)){
            queryWrapper.lambda().eq(User::getPhone, phone);
        }
        if (StringUtils.isNotBlank(userName)){
            queryWrapper.lambda().like(User::getUserName, userName);
        }
        return userMapper.selectList(queryWrapper);
    }
}
