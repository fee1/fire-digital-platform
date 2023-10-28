package com.huajie.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.huajie.domain.common.oauth2.model.CustomizeGrantedAuthority;
import com.huajie.domain.entity.Menu;
import com.huajie.domain.entity.Role;
import com.huajie.infrastructure.mapper.MenuMapper;
import com.huajie.infrastructure.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    private RoleMenuRelationService roleMenuRelationService;

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

    public void add(String roleName, String roleCode, String description, List<Integer> menuIds) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomizeGrantedAuthority authorities = null;
        for (GrantedAuthority authority : auth.getAuthorities()) {
            authorities = (CustomizeGrantedAuthority) authority;
            break;
        }
        Role role = new Role();
        role.setRoleCode(roleCode);
        role.setRoleName(roleName);
        role.setDescription(description);
        role.setTenantId(authorities.getTenant().getId());
        roleMapper.insert(role);

        for (Integer menuId : menuIds) {
            roleMenuRelationService.add(role.getId(), menuId);
        }
    }

    public Role getRoleByCode(String entAdminCode) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Role::getRoleCode, entAdminCode);
        return this.roleMapper.selectOne(queryWrapper);
    }

    public List<Role> getAllRole() {
        return this.roleMapper.selectList(new QueryWrapper<>());
    }
}
