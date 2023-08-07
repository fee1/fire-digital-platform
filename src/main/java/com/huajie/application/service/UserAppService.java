package com.huajie.application.service;

import com.huajie.application.api.response.UserDetailResponseVO;
import com.huajie.domain.common.oauth2.model.CustomizeGrantedAuthority;
import com.huajie.domain.entity.Role;
import com.huajie.domain.entity.User;
import com.huajie.domain.service.RoleService;
import com.huajie.domain.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author zhuxiaofeng
 * @date 2023/8/7
 */
@Service
public class UserAppService {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    public List<UserDetailResponseVO> getTenantUsers() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomizeGrantedAuthority authorities = (CustomizeGrantedAuthority) auth.getAuthorities();
        Integer id = authorities.getTenant().getId();
        List<User> users = userService.getUsersByTenantId(id);
        Set<Integer> roleIds = users.stream().map(User::getRoleId).collect(Collectors.toSet());
        List<Role> roles = roleService.getRolesByIds(roleIds);


        Map<Integer, List<Role>> id2Roles = roles.stream().collect(Collectors.groupingBy(Role::getId));
        List<UserDetailResponseVO> responseVOList = new ArrayList<>();
        for (User user : users) {
            UserDetailResponseVO userDetailResponseVO = new UserDetailResponseVO();
            BeanUtils.copyProperties(user, userDetailResponseVO);
            List<Role> currentUserTole = id2Roles.get(user.getRoleId());
            if (!CollectionUtils.isEmpty(currentUserTole)) {
                userDetailResponseVO.setRoleName(currentUserTole.get(0).getRoleName());
            }
        }
        return responseVOList;
    }
}
