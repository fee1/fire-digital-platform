package com.huajie.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.huajie.domain.entity.Role;
import com.huajie.infrastructure.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

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

    public Page<Role> getPageRolesByTenantId(Integer tenantId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Role::getTenantId, tenantId);
        return (Page<Role>) roleMapper.selectList(queryWrapper);
    }

    public List<Role> getRolesByIds(Set<Integer> roleIds) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(Role::getId, roleIds);
        return roleMapper.selectList(queryWrapper);
    }
}
