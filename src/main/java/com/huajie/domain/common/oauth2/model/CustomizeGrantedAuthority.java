package com.huajie.domain.common.oauth2.model;

import com.huajie.domain.entity.Role;
import com.huajie.domain.entity.RoleFunctionRelation;
import com.huajie.domain.entity.RoleMenuRelation;
import com.huajie.domain.entity.Tenant;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

/**
 * @author zhuxiaofeng
 * @date 2023/8/6
 */
@Data
public class CustomizeGrantedAuthority implements GrantedAuthority {

    /**
     * 角色信息
     */
    private RoleModel role;

    /**
     * 租户信息
     */
    private TenantModel tenant;

    /**
     * 角色菜单关联关系信息
     */
    private List<Integer> roleMenuRelationIds;

    /**
     * 角色方法关联关系信息
     */
    private List<Integer> roleFunctionRelationIds;


    @Override
    public String getAuthority() {
        return this.role.getRoleCode();
    }

}
