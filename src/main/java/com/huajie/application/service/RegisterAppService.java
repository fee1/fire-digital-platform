package com.huajie.application.service;

import com.alibaba.fastjson.JSONObject;
import com.huajie.application.api.common.exception.ApiException;
import com.huajie.application.api.request.EnterpriseRegiestRequestVO;
import com.huajie.application.api.request.GovermentRegiestRequestVO;
import com.huajie.application.api.request.UserAddRequestVO;
import com.huajie.domain.common.constants.RoleCodeConstants;
import com.huajie.domain.common.constants.TenantStatusConstants;
import com.huajie.domain.common.constants.TenantTypeConstants;
import com.huajie.domain.common.exception.ServerException;
import com.huajie.domain.entity.Role;
import com.huajie.domain.entity.Tenant;
import com.huajie.domain.entity.User;
import com.huajie.domain.service.RegisterService;
import com.huajie.domain.service.RoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zhuxiaofeng
 * @date 2023/8/27
 */
@Service
public class RegisterAppService {

    @Autowired
    private RegisterService registerService;

    @Autowired
    private RoleService roleService;

    public void regiestEnterprise(EnterpriseRegiestRequestVO regiestRequestVO) {
        List<UserAddRequestVO> entAdminList = regiestRequestVO.getEntAdminList();
        Role entAdminCodeRole = roleService.getRoleByCode(RoleCodeConstants.ENT_ADMIN_CODE);
        for (UserAddRequestVO userAddRequestVO : entAdminList) {
            if (entAdminCodeRole == null){
                throw new ServerException("查询不到 企业消防安全责任人 角色信息");
            }
            if (!userAddRequestVO.getRoleId().equals(entAdminCodeRole.getId())){
                throw new ApiException("企业消防安全责任人 的roleId必须为: " + entAdminCodeRole.getId());
            }
        }
        List<UserAddRequestVO> entOperatorList = regiestRequestVO.getEntOperatorList();
        Role entOperatorCodeRole = roleService.getRoleByCode(RoleCodeConstants.ENT_OPERATOR_CODE);
        for (UserAddRequestVO userAddRequestVO : entOperatorList) {

            if (entOperatorCodeRole == null){
                throw new ServerException("查询不到 企业消防安全管理人 角色信息");
            }
            if (!userAddRequestVO.getRoleId().equals(entOperatorCodeRole.getId())){
                throw new ApiException("企业消防安全管理人 的roleId必须为: " + entOperatorCodeRole.getId());
            }
        }
        Tenant tenant = new Tenant();
        tenant.setTenantName(regiestRequestVO.getEnterpriseName());
        tenant.setTenantType(TenantTypeConstants.ENTERPRISE);
        tenant.setStatus(TenantStatusConstants.ENABLE);
        //todo 可用截至时间未确定
        tenant.setEffectiveEndDate(new Date());
        tenant.setProvince(regiestRequestVO.getProvince());
        tenant.setCity(regiestRequestVO.getCity());
        tenant.setRegion(regiestRequestVO.getRegion());
        tenant.setStreet(regiestRequestVO.getStreet());
        tenant.setAddress(regiestRequestVO.getAddress());
        tenant.setEnterpriseType(regiestRequestVO.getEnterpriseType());
        tenant.setEntIndustryClassification(regiestRequestVO.getEntIndustryClassification());
        tenant.setEntFireType(regiestRequestVO.getEntFireType());

        List<User> userList = new ArrayList<>();
        for (UserAddRequestVO userAddRequestVO : regiestRequestVO.getEntAdminList()) {
            User user = new User();
            BeanUtils.copyProperties(userAddRequestVO, user);
            userList.add(user);
        }
        for (UserAddRequestVO userAddRequestVO : regiestRequestVO.getEntOperatorList()) {
            User user = new User();
            BeanUtils.copyProperties(userAddRequestVO, user);
            userList.add(user);
        }
        registerService.regiestEnterprise(tenant, userList);
    }

    public void regiestGoverment(GovermentRegiestRequestVO regiestRequestVO) {
        List<UserAddRequestVO> govAdminList = regiestRequestVO.getGovAdminList();
        Role govAdminCodeRole = roleService.getRoleByCode(RoleCodeConstants.GOV_ADMIN_CODE);
        for (UserAddRequestVO userAddRequestVO : govAdminList) {
            if (govAdminCodeRole == null){
                throw new ServerException("查询不到 政府消防安全责任人 角色信息");
            }
            if (!userAddRequestVO.getRoleId().equals(govAdminCodeRole.getId())){
                throw new ApiException("政府消防安全责任人 的roleId必须为: " + govAdminCodeRole.getId());
            }
        }

        List<UserAddRequestVO> govOperatorList = regiestRequestVO.getGovOperatorList();
        Role govOperatorCodeRole = roleService.getRoleByCode(RoleCodeConstants.GOV_OPERATOR_CODE);
        for (UserAddRequestVO userAddRequestVO : govOperatorList) {

            if (govOperatorCodeRole == null){
                throw new ServerException("查询不到 政府消防安全管理人 角色信息");
            }
            if (!userAddRequestVO.getRoleId().equals(govOperatorCodeRole.getId())){
                throw new ApiException("政府消防安全管理人 的roleId必须为: " + govOperatorCodeRole.getId());
            }
        }

        Tenant tenant = new Tenant();
        tenant.setTenantName(regiestRequestVO.getGovernmentName());
        tenant.setTenantType(TenantTypeConstants.ENTERPRISE);
        tenant.setStatus(TenantStatusConstants.ENABLE);
        //todo 可用截至时间未确定
        tenant.setEffectiveEndDate(new Date());
        tenant.setProvince(regiestRequestVO.getProvince());
        tenant.setCity(regiestRequestVO.getCity());
        tenant.setRegion(regiestRequestVO.getRegion());
        tenant.setStreet(regiestRequestVO.getStreet());
        tenant.setAddress(regiestRequestVO.getAddress());
        tenant.setGovernmentType(regiestRequestVO.getGovernmentType());
        tenant.setGovIndustrySector(regiestRequestVO.getGovIndustrySector());
        tenant.setEntFireType(regiestRequestVO.getEntFireType());
        tenant.setEntIndustryClassification(JSONObject.toJSONString(regiestRequestVO.getEntIndustryClassification()));
        tenant.setEntFireType(regiestRequestVO.getEntFireType());

        List<User> userList = new ArrayList<>();
        for (UserAddRequestVO userAddRequestVO : regiestRequestVO.getGovAdminList()) {
            User user = new User();
            BeanUtils.copyProperties(userAddRequestVO, user);
            userList.add(user);
        }
        for (UserAddRequestVO userAddRequestVO : regiestRequestVO.getGovOperatorList()) {
            User user = new User();
            BeanUtils.copyProperties(userAddRequestVO, user);
            userList.add(user);
        }
        this.registerService.regiestGoverment(tenant, userList);
    }
}
