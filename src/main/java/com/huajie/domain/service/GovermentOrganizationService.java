package com.huajie.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huajie.application.api.request.EditEnterpriseRequestVO;
import com.huajie.domain.common.constants.RoleCodeConstants;
import com.huajie.domain.common.exception.ServerException;
import com.huajie.domain.common.oauth2.model.TenantModel;
import com.huajie.domain.common.utils.UserContext;
import com.huajie.domain.entity.GovIndustryMap;
import com.huajie.domain.entity.Role;
import com.huajie.domain.entity.Tenant;
import com.huajie.domain.entity.User;
import com.huajie.infrastructure.mapper.GovIndustryMapMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author zhuxiaofeng
 * @date 2023/9/3
 */
@Service
public class GovermentOrganizationService {

    @Autowired
    private TenantService tenantService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private GovIndustryMapService govIndustryMapService;

    public void editGovermentInfo(Tenant tenant, List<String> entIndustryClassification) {
        tenantService.updateById(tenant);
        List<GovIndustryMap> govIndustryMaps = govIndustryMapService.getGovIndustryMapByTenantId(tenant.getId());
        //当前管理的行业
        Set<String> currentGovIndustryMaps = govIndustryMaps.stream().map(GovIndustryMap::getIndustryClassification).collect(Collectors.toSet());
        //新编辑管理的行业
        Set<String> newGovIndustryMaps = new HashSet<>(entIndustryClassification);
        // 计算出应该移除的 公式 : 1、2、3 -  3、4、5 = 1、2
        Set<String> delGovIndustryMaps = new HashSet<>(currentGovIndustryMaps);
        delGovIndustryMaps.removeAll(newGovIndustryMaps);

        govIndustryMapService.deleteByTenantIdAndGovIndustryMaps(tenant.getId(), delGovIndustryMaps);

        // 计算出应该添加的 公式 : 3、4、5 - 1、2、3 = 4、5
        Set<String> addGovIndustryMaps = new HashSet<>(newGovIndustryMaps);
        addGovIndustryMaps.removeAll(currentGovIndustryMaps);
        for (String addGovIndustryMap : addGovIndustryMaps) {
            GovIndustryMap govIndustryMap = new GovIndustryMap();
            govIndustryMap.setTenantId(tenant.getId());
            govIndustryMap.setIndustryClassification(addGovIndustryMap);
            govIndustryMapService.insertGovIndustryMap(govIndustryMap);

        }
    }

    public void addAdminUser(User user) {
        Role govAdminCodeRole = roleService.getRoleByCode(RoleCodeConstants.GOV_ADMIN_CODE);
        user.setRoleId(govAdminCodeRole.getId());
        userService.addUser(user);
    }

    public void addOperatorUser(User user) {
        Role govOperatorCodeRole = roleService.getRoleByCode(RoleCodeConstants.GOV_OPERATOR_CODE);
        user.setRoleId(govOperatorCodeRole.getId());
        userService.addUser(user);
    }

    public List<Tenant> getEnterpriseVerifyList(String enterpriseName) {
        List<Tenant> tenantLikeNameAndNotapprove = tenantService.getTenantLikeNameAndNotapprove(enterpriseName);
        TenantModel currentTenant = UserContext.getCurrentTenant();
        List<GovIndustryMap> govIndustryMapByTenantId = govIndustryMapService.getGovIndustryMapByTenantId(currentTenant.getId());

        if (CollectionUtils.isEmpty(govIndustryMapByTenantId)){
            throw new ServerException("当前政府无管理的行业，请配置");
        }

        //筛选出属于管理行业范围的租户
        List<Tenant> tenantList = new ArrayList<>();
        Set<String> industryClassifications = govIndustryMapByTenantId.stream().map(GovIndustryMap::getIndustryClassification).collect(Collectors.toSet());
        for (Tenant tenant : tenantLikeNameAndNotapprove) {
            if (industryClassifications.contains(tenant.getEntIndustryClassification())){
                tenantList.add(tenant);
            }
        }
        return tenantList;
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


    public List<Tenant> getEnterpriseList(String enterpriseName) {
        TenantModel currentTenant = UserContext.getCurrentTenant();
        List<GovIndustryMap> govIndustryMaps = govIndustryMapService.getGovIndustryMapByTenantId(currentTenant.getId());
        if (CollectionUtils.isEmpty(govIndustryMaps)){
            throw new ServerException("当前政府无管理的行业，请配置");
        }
        List<Tenant> tenantLikeNameAndApprove = tenantService.getTenantLikeNameAndApprove(enterpriseName);

        //筛选出属于管理行业范围的租户
        List<Tenant> tenantList = new ArrayList<>();
        Set<String> industryClassifications = govIndustryMaps.stream().map(GovIndustryMap::getIndustryClassification).collect(Collectors.toSet());
        for (Tenant tenant : tenantLikeNameAndApprove) {
            if (industryClassifications.contains(tenant.getEntIndustryClassification())){
                tenantList.add(tenant);
            }
        }
        return tenantList;
    }
}
