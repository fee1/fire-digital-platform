package com.huajie.application.service;

import com.github.pagehelper.Page;
import com.huajie.application.api.request.ChangePasswordRequestVO;
import com.huajie.application.api.request.TenantUsersRequestVO;
import com.huajie.application.api.request.UserAddRequestVO;
import com.huajie.application.api.request.UserUpdateRequestVO;
import com.huajie.application.api.response.CurrentUserResponseVO;
import com.huajie.application.api.response.UserDetailResponseVO;
import com.huajie.domain.common.oauth2.model.CustomizeGrantedAuthority;
import com.huajie.domain.common.oauth2.model.TenantModel;
import com.huajie.domain.common.utils.UserContext;
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

    public Page<UserDetailResponseVO> getPageTenantUsers(TenantUsersRequestVO requestVO) {
        Page<User> users = userService.getPageTenantUsers(requestVO.getPageNum(), requestVO.getPageSize(),requestVO.getUserNo(), requestVO.getPhone(), requestVO.getUserName());
//        List<User> users = userService.getUsersByTenantId(id);
        Set<Integer> roleIds = users.stream().map(User::getRoleId).collect(Collectors.toSet());
        List<Role> roles = roleService.getRolesByIds(roleIds);


        Map<Integer, List<Role>> id2Roles = roles.stream().collect(Collectors.groupingBy(Role::getId));
        Page<UserDetailResponseVO> responseVOList = new Page<>();
        BeanUtils.copyProperties(users, responseVOList);
        for (User user : users) {
            UserDetailResponseVO userDetailResponseVO = new UserDetailResponseVO();
            BeanUtils.copyProperties(user, userDetailResponseVO);
            List<Role> currentUserTole = id2Roles.get(user.getRoleId());
            if (!CollectionUtils.isEmpty(currentUserTole)) {
                userDetailResponseVO.setRoleName(currentUserTole.get(0).getRoleName());
            }
            responseVOList.add(userDetailResponseVO);
        }
        return responseVOList;
    }

    public void addUser(UserAddRequestVO requestVO) {
        User user = new User();
        BeanUtils.copyProperties(requestVO, user);
        TenantModel currentTenant = UserContext.getCurrentTenant();
        Integer id = currentTenant.getId();
        user.setTenantId(id);
        userService.addUser(user);
    }

    public void updateUser(UserUpdateRequestVO requestVO) {
        User user = new User();
        BeanUtils.copyProperties(requestVO, user);
        userService.updateUser(user);
    }

    public UserDetailResponseVO getUserDetail(Integer userId) {
        User user = userService.getUserById(userId);
        UserDetailResponseVO userDetailResponseVO = new UserDetailResponseVO();
        BeanUtils.copyProperties(user, userDetailResponseVO);

        Role role = roleService.getRoleById(user.getRoleId());
        userDetailResponseVO.setRoleName(role.getRoleName());
        return userDetailResponseVO;
    }

    public void changePassword(ChangePasswordRequestVO requestVO) {
        userService.changePassword(requestVO.getUserId(), requestVO.getPassword());
    }

    public CurrentUserResponseVO getCurrentUser() {
        org.springframework.security.core.userdetails.User currentUser = UserContext.getCurrentLoginUser();
        CurrentUserResponseVO currentUserResponseVO = new CurrentUserResponseVO();
        BeanUtils.copyProperties(currentUser, currentUserResponseVO);
        currentUserResponseVO.setAuthorities(currentUser.getAuthorities());
        return currentUserResponseVO;
    }


}
