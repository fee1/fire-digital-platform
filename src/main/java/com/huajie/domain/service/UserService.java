package com.huajie.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.huajie.application.api.common.exception.ApiException;
import com.huajie.domain.common.oauth2.model.CustomizeGrantedAuthority;
import com.huajie.domain.entity.User;
import com.huajie.infrastructure.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        user.setPassword(passwordEncoder.encode(user.getPassword()));
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
        if (StringUtils.isNotBlank(user.getEmail())) {
            updateUserInfo.setEmail(user.getEmail());
        }
        if (StringUtils.isNotBlank(user.getPhone())) {
            updateUserInfo.setPhone(user.getPhone());
        }
        if (StringUtils.isNotBlank(user.getUserName())) {
            updateUserInfo.setUserName(user.getUserName());
        }
        if (user.getRoleId() != null) {
            updateUserInfo.setRoleId(user.getRoleId());
        }
        if (StringUtils.isNotBlank(user.getOpenId())) {
            updateUserInfo.setOpenId(user.getOpenId());
        }

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

    public Page<User> getPageTenantUsers(Integer pageNum, Integer pageSize, String userNo, String phone, String userName) {
        PageHelper.startPage(pageNum, pageSize);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomizeGrantedAuthority authorities = null;
        for (GrantedAuthority authority : auth.getAuthorities()) {
            authorities = (CustomizeGrantedAuthority) authority;
            break;
        }

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
        return (Page<User>)userMapper.selectList(queryWrapper);
    }

    //todo 优化成批量插入
    @Transactional(rollbackFor = Exception.class)
    public void addUsers(List<User> userList) {
        for (User user : userList) {
            this.addUser(user);
        }
    }

    public List<User> getUsersByTenantIdAndRoleId(Integer tenantId, Integer roleId) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getTenantId, tenantId).eq(User::getRoleId, roleId);
        return this.userMapper.selectList(queryWrapper);
    }

    public User getUserByOpenId(String openid) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getOpenId, openid);
        return this.userMapper.selectOne(queryWrapper);
    }
}
