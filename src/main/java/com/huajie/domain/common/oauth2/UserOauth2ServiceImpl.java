package com.huajie.domain.common.oauth2;

import com.huajie.application.api.common.exception.ApiException;
import com.huajie.domain.common.oauth2.model.CustomizeGrantedAuthority;
import com.huajie.domain.common.oauth2.model.RoleModel;
import com.huajie.domain.common.oauth2.model.TenantModel;
import com.huajie.domain.entity.Role;
import com.huajie.domain.entity.RoleFunctionRelation;
import com.huajie.domain.entity.RoleMenuRelation;
import com.huajie.domain.entity.Tenant;
import com.huajie.domain.entity.User;
import com.huajie.domain.service.RoleFunctionRelationService;
import com.huajie.domain.service.RoleMenuRelationService;
import com.huajie.domain.service.RoleService;
import com.huajie.domain.service.TenantService;
import com.huajie.domain.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author zhuxiaofeng
 * @date 2023/7/11
 */
@Service
public class UserOauth2ServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleFunctionRelationService roleFunctionRelationService;

    @Autowired
    private RoleMenuRelationService roleMenuRelationService;

    @Autowired
    private TenantService tenantService;

    /**
     * 根据手机号加载用户
     * @param phone
     * @return
     */
    private UserDetails loadUserByPhone(String phone){
        User user = userService.getUserByPhone(phone);
        if (!Objects.isNull(user)) {
            return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
                    Collections.singleton(loadGrantedAuthority(user)));
        }else {
            return null;
        }
    }

    /**
     * 根据邮箱号加载用户
     * @param email
     * @return
     */
    private UserDetails loadUserByEmail(String email){
        User user = userService.getUserByEmail(email);
        if (!Objects.isNull(user)) {
            return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
                    Collections.singleton(loadGrantedAuthority(user)));
        } else {
            return null;
        }
    }

    /**
     * 加载用户权限、菜单、功能等信息
     * @param user 用户
     * @return
     */
    private CustomizeGrantedAuthority loadGrantedAuthority(User user){
        Role role = roleService.getRoleById(user.getRoleId());
        List<RoleMenuRelation> roleMenuRelations = roleMenuRelationService.getMenuRelationByRoleId(user.getRoleId());
        List<RoleFunctionRelation> roleFunctionRelations = roleFunctionRelationService.getFunctionRelationByRoleId(user.getRoleId());
        Tenant tenant = tenantService.getTenantByTenantId(user.getTenantId());

        if (role == null){
            throw new ApiException("该用户没有角色，请给予角色后再尝试登录");
        }
        CustomizeGrantedAuthority customizeGrantedAuthority = new CustomizeGrantedAuthority();
        RoleModel roleModel = new RoleModel();
        BeanUtils.copyProperties(role, roleModel);
        customizeGrantedAuthority.setRole(roleModel);

        if (!CollectionUtils.isEmpty(roleMenuRelations)) {
            List<Integer> menuIds = roleMenuRelations.stream().map(RoleMenuRelation::getMenuId).collect(Collectors.toList());
            customizeGrantedAuthority.setRoleMenuRelationIds(menuIds);
        }

        if (!CollectionUtils.isEmpty(roleFunctionRelations)) {
            List<Integer> functionIds = roleFunctionRelations.stream().map(RoleFunctionRelation::getFunctionId).collect(Collectors.toList());
            customizeGrantedAuthority.setRoleFunctionRelationIds(functionIds);
        }

        TenantModel tenantModel = new TenantModel();
        BeanUtils.copyProperties(tenant, tenantModel);
        customizeGrantedAuthority.setTenant(tenantModel);
        return customizeGrantedAuthority;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //todo 邮箱与手机号的正则表达式判断走哪一个登录账号逻辑
        UserDetails userDetails = loadUserByEmail(username);
        if (Objects.isNull(userDetails)){
            userDetails = loadUserByPhone(username);
        }
        if (Objects.isNull(userDetails)) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        return userDetails;
    }
}
