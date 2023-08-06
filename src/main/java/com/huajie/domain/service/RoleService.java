package com.huajie.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huajie.domain.entity.Role;
import com.huajie.infrastructure.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhuxiaofeng
 * @date 2023/8/6
 */
@Service
public class RoleService {

    @Autowired
    private RoleMapper roleMapper;

    public Role getRoleById(Integer roleId) {
        QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<>();
        return roleMapper.selectById(roleId);
    }
}
