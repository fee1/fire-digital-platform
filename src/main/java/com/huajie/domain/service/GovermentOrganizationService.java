package com.huajie.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huajie.application.api.request.EditEnterpriseRequestVO;
import com.huajie.domain.common.constants.RoleCodeConstants;
import com.huajie.domain.entity.GovIndustryMap;
import com.huajie.domain.entity.Role;
import com.huajie.domain.entity.Tenant;
import com.huajie.domain.entity.User;
import com.huajie.infrastructure.mapper.GovIndustryMapMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private GovIndustryMapMapper govIndustryMapMapper;

    public void editGovermentInfo(Tenant tenant, List<String> entIndustryClassification) {
        tenantService.updateById(tenant);
        QueryWrapper<GovIndustryMap> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GovIndustryMap::getTenantId, tenant.getId());
        List<GovIndustryMap> govIndustryMaps = govIndustryMapMapper.selectList(queryWrapper);
        //当前管理的行业
        Set<String> currentGovIndustryMaps = govIndustryMaps.stream().map(GovIndustryMap::getIndustryClassification).collect(Collectors.toSet());
        //新编辑管理的行业
        Set<String> newGovIndustryMaps = new HashSet<>(entIndustryClassification);
        // 计算出应该移除的 公式 : 1、2、3 -  3、4、5 = 1、2
        Set<String> delGovIndustryMaps = new HashSet<>(currentGovIndustryMaps);
        delGovIndustryMaps.removeAll(newGovIndustryMaps);
        queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(GovIndustryMap::getTenantId, tenant.getId())
                .in(GovIndustryMap::getIndustryClassification, delGovIndustryMaps);
        govIndustryMapMapper.delete(queryWrapper);

        // 计算出应该添加的 公式 : 3、4、5 - 1、2、3 = 4、5
        Set<String> addGovIndustryMaps = new HashSet<>(newGovIndustryMaps);
        addGovIndustryMaps.removeAll(currentGovIndustryMaps);
        for (String addGovIndustryMap : addGovIndustryMaps) {
            GovIndustryMap govIndustryMap = new GovIndustryMap();
            govIndustryMap.setTenantId(tenant.getId());
            govIndustryMap.setIndustryClassification(addGovIndustryMap);
            govIndustryMapMapper.insert(govIndustryMap);
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
        return tenantService.getTenantLikeNameAndNotapprove(enterpriseName);
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
        return tenantService.getTenantLikeNameAndApprove(enterpriseName);
    }
}
