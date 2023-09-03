package com.huajie.application.service;

import com.alibaba.fastjson.JSONObject;
import com.huajie.application.api.request.AddGovermentAdminRequestVO;
import com.huajie.application.api.request.AddGovermentOperatorRequestVO;
import com.huajie.application.api.request.EditEnterpriseRequestVO;
import com.huajie.application.api.request.EditGovermentInfoRequestVO;
import com.huajie.application.api.request.UserAddRequestVO;
import com.huajie.application.api.response.EnterpriseResponseVO;
import com.huajie.domain.common.constants.RoleCodeConstants;
import com.huajie.domain.common.oauth2.model.TenantModel;
import com.huajie.domain.common.utils.UserContext;
import com.huajie.domain.entity.Role;
import com.huajie.domain.entity.Tenant;
import com.huajie.domain.entity.User;
import com.huajie.domain.service.GovermentOrganizationService;
import com.huajie.domain.service.RoleService;
import com.huajie.domain.service.TenantService;
import com.huajie.domain.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhuxiaofeng
 * @date 2023/9/3
 */
@Service
public class GovermentOrganizationAppService {

    @Autowired
    private GovermentOrganizationService govermentOrganizationService;

    @Autowired
    private TenantService tenantService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    public void editGovermentInfo(EditGovermentInfoRequestVO requestVO) {
        Tenant tenant = new Tenant();
        BeanUtils.copyProperties(requestVO, tenant);
        if (StringUtils.isNotBlank(requestVO.getGovernmentName())){
            tenant.setTenantName(requestVO.getGovernmentName());
        }
        if (!CollectionUtils.isEmpty(requestVO.getEntIndustryClassification())){
            tenant.setEntIndustryClassification(JSONObject.toJSONString(requestVO.getEntIndustryClassification()));
        }
        TenantModel currentTenant = UserContext.getCurrentTenant();
        tenant.setId(currentTenant.getId());
        tenantService.updateById(tenant);
    }

    public void addAdminUser(AddGovermentAdminRequestVO requestVO) {
        List<UserAddRequestVO> govAdminList = requestVO.getGovAdminList();
        Role govAdminCodeRole = roleService.getRoleByCode(RoleCodeConstants.GOV_ADMIN_CODE);
        for (UserAddRequestVO userAddRequestVO : govAdminList) {
            User user =new User();
            BeanUtils.copyProperties(userAddRequestVO, user);
            user.setRoleId(govAdminCodeRole.getId());
            userService.addUser(user);
        }
    }

    public void addOperatorUser(AddGovermentOperatorRequestVO requestVO) {
        List<UserAddRequestVO> govOperatorList = requestVO.getGovOperatorList();
        Role govOperatorCodeRole = roleService.getRoleByCode(RoleCodeConstants.GOV_OPERATOR_CODE);
        for (UserAddRequestVO userAddRequestVO : govOperatorList) {
            User user =new User();
            BeanUtils.copyProperties(userAddRequestVO, user);
            user.setRoleId(govOperatorCodeRole.getId());
            userService.addUser(user);
        }
    }

    public List<EnterpriseResponseVO> getEnterpriseVerifyList(String enterpriseName) {
        List<Tenant> tenants = tenantService.getTenantLikeNameAndNotapprove(enterpriseName);
        List<EnterpriseResponseVO> enterpriseResponseVOList = new ArrayList<>();
        for (Tenant tenant : tenants) {
            EnterpriseResponseVO enterpriseResponseVO = new EnterpriseResponseVO();
            BeanUtils.copyProperties(tenant, enterpriseResponseVO);
            enterpriseResponseVO.setEnterpriseName(tenant.getTenantName());
        }
        return enterpriseResponseVOList;
    }

    public void enterpriseVerifyPass(Integer enterpriseId) {
        Tenant tenant = new Tenant();
        tenant.setId(enterpriseId);
        tenant.setApproveStatus((short) 1);
        tenantService.updateById(tenant);
    }

    public void editEnterprise(EditEnterpriseRequestVO requestVO) {
        Tenant tenant = new Tenant();
        tenant.setId(requestVO.getEnterpriseId());
        tenant.setEntIndustryClassification(requestVO.getEntIndustryClassification());
        this.tenantService.updateById(tenant);
    }

    public List<EnterpriseResponseVO> getEnterpriseList(String enterpriseName) {
        List<Tenant> tenants = tenantService.getTenantLikeNameAndApprove(enterpriseName);
        List<EnterpriseResponseVO> enterpriseResponseVOList = new ArrayList<>();
        for (Tenant tenant : tenants) {
            EnterpriseResponseVO enterpriseResponseVO = new EnterpriseResponseVO();
            BeanUtils.copyProperties(tenant, enterpriseResponseVO);
            enterpriseResponseVO.setEnterpriseName(tenant.getTenantName());
        }
        return enterpriseResponseVOList;
    }
}
