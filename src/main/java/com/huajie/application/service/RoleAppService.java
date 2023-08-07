package com.huajie.application.service;

import com.huajie.application.api.response.RoleResponseVO;
import com.huajie.domain.common.oauth2.model.CustomizeGrantedAuthority;
import com.huajie.domain.entity.Role;
import com.huajie.domain.service.RoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhuxiaofeng
 * @date 2023/8/7
 */
@Service
public class RoleAppService {

    @Autowired
    private RoleService roleService;

    public List<RoleResponseVO> getRoleList() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomizeGrantedAuthority authorities = (CustomizeGrantedAuthority) auth.getAuthorities();
        Integer id = authorities.getTenant().getId();
        List<Role> roles = roleService.getRolesByTenantId(id);
        List<RoleResponseVO> responseVOS = new ArrayList<>();
        for (Role role : roles) {
            RoleResponseVO roleResponseVO = new RoleResponseVO();
            BeanUtils.copyProperties(role, roleResponseVO);
            responseVOS.add(roleResponseVO);
        }
        return responseVOS;
    }

    public RoleResponseVO getRoleDetail(Integer id) {
        Role role = roleService.getRoleById(id);
        RoleResponseVO responseVO = new RoleResponseVO();
        BeanUtils.copyProperties(role, responseVO);
        return responseVO;
    }
}
