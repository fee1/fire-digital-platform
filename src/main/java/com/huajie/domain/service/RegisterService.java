package com.huajie.domain.service;

import com.huajie.application.api.common.exception.ApiException;
import com.huajie.application.api.request.UserAddRequestVO;
import com.huajie.domain.common.constants.RoleCodeConstants;
import com.huajie.domain.common.constants.TenantStatusConstants;
import com.huajie.domain.common.constants.TenantTypeConstants;
import com.huajie.domain.common.exception.ServerException;
import com.huajie.domain.entity.GovIndustryMap;
import com.huajie.domain.entity.Role;
import com.huajie.domain.entity.Tenant;
import com.huajie.domain.entity.User;
import com.huajie.infrastructure.mapper.GovIndustryMapMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zhuxiaofeng
 * @date 2023/8/27
 */
@Service
public class RegisterService {

    @Autowired
    private UserService userService;

    @Autowired
    private TenantService tenantService;

    @Autowired
    private GovIndustryMapMapper govIndustryMapMapper;

    @Autowired
    private RoleService roleService;

    /**
     * 企业注册
     * @param tenant 企业租户
     * @param entAdminList 企业消防安全责任人
     * @param entOperatorList 企业消防安全管理人
     */
    public void regiestEnterprise(Tenant tenant,  List<UserAddRequestVO> entAdminList, List<UserAddRequestVO> entOperatorList) {
        Role entAdminCodeRole = roleService.getRoleByCode(RoleCodeConstants.ENT_ADMIN_CODE);
        if (entAdminCodeRole == null){
            throw new ServerException("企业消防安全责任人 权限不存在");
        }
        Role entOperatorCodeRole = roleService.getRoleByCode(RoleCodeConstants.ENT_OPERATOR_CODE);
        if (entOperatorCodeRole == null){
            throw new ServerException("企业消防安全管理人 权限不存在");
        }

        List<User> userList = new ArrayList<>();
        for (UserAddRequestVO userAddRequestVO : entAdminList) {
            User user = new User();
            BeanUtils.copyProperties(userAddRequestVO, user);
            user.setRoleId(entAdminCodeRole.getId());
            userList.add(user);
        }
        for (UserAddRequestVO userAddRequestVO : entOperatorList) {
            User user = new User();
            BeanUtils.copyProperties(userAddRequestVO, user);
            user.setRoleId(entOperatorCodeRole.getId());
            userList.add(user);
        }
        tenantService.add(tenant);
        userService.addUsers(userList);
    }

    /**
     * 政府注册
     * @param userList 政府用户集合
     * @param tenant 政府租户
     * @param govAdminList
     * @param govOperatorList
     * @param entIndustryClassification 政府管理的行业
     */
    public void regiestGoverment(Tenant tenant, List<UserAddRequestVO> govAdminList,
                                 List<UserAddRequestVO> govOperatorList, List<String> entIndustryClassification) {
        Role govAdminCodeRole = roleService.getRoleByCode(RoleCodeConstants.GOV_ADMIN_CODE);
        if (govAdminCodeRole == null){
            throw new ServerException("政府消防安全责任人 权限不存在");
        }
        Role govOperatorCodeRole = roleService.getRoleByCode(RoleCodeConstants.GOV_OPERATOR_CODE);
        if (govOperatorCodeRole == null){
            throw new ServerException("政府消防安全管理人 权限不存在");
        }

        List<User> userList = new ArrayList<>();
        for (UserAddRequestVO userAddRequestVO : govAdminList) {
            User user = new User();
            BeanUtils.copyProperties(userAddRequestVO, user);
            user.setRoleId(govAdminCodeRole.getId());
            userList.add(user);
        }
        for (UserAddRequestVO userAddRequestVO : govOperatorList) {
            User user = new User();
            BeanUtils.copyProperties(userAddRequestVO, user);
            user.setRoleId(govOperatorCodeRole.getId());
            userList.add(user);
        }
        tenantService.add(tenant);
        userService.addUsers(userList);
        if (!CollectionUtils.isEmpty(entIndustryClassification)){
            for (String code : entIndustryClassification) {
                GovIndustryMap govIndustryMap = new GovIndustryMap();
                govIndustryMap.setTenantId(tenant.getId());
                govIndustryMap.setIndustryClassification(code);
                govIndustryMapMapper.insert(govIndustryMap);
            }
        }
    }

}
