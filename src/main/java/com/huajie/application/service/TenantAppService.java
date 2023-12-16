package com.huajie.application.service;

import com.huajie.application.api.request.TenantSearchRequestVO;
import com.huajie.application.api.response.EnterpriseInfoResponseVO;
import com.huajie.application.api.response.EnterpriseUserInfoResponseVO;
import com.huajie.application.api.response.TenantResponseVO;
import com.huajie.domain.common.constants.RoleCodeConstants;
import com.huajie.domain.common.utils.UserContext;
import com.huajie.domain.entity.*;
import com.huajie.domain.service.RegionService;
import com.huajie.domain.service.RoleService;
import com.huajie.domain.service.SysDicService;
import com.huajie.domain.service.TenantService;
import com.huajie.domain.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhuxiaofeng
 * @date 2023/10/24
 */
@Service
public class TenantAppService {

    @Autowired
    private UserService userService;

    @Autowired
    private RegionService regionService;

    @Autowired
    private SysDicService sysDicService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private TenantService tenantService;

    public EnterpriseInfoResponseVO getEnterpriseInfo() {
        Tenant currentTenant = UserContext.getCurrentTenant();
        EnterpriseInfoResponseVO responseVO = new EnterpriseInfoResponseVO();
        responseVO.setEnterpriseName(currentTenant.getTenantName());
        Region province = regionService.getRegionById(currentTenant.getProvince());
        responseVO.setProvinceName(province.getRegionName());
        Region city = regionService.getRegionById(currentTenant.getCity());
        if (city != null) {
            responseVO.setCityCode(currentTenant.getCity());
            responseVO.setCityName(city.getRegionName());
        }
        Region region = regionService.getRegionById(currentTenant.getRegion());
        if (region != null ) {
            responseVO.setRegionCode(currentTenant.getRegion());
            responseVO.setRegionName(region.getRegionName());
        }
        Region street = regionService.getRegionById(currentTenant.getStreet());
        if (street != null) {
            responseVO.setStreetCode(currentTenant.getStreet());
            responseVO.setStreetName(street.getRegionName());
        }
        responseVO.setAddress(currentTenant.getAddress());
        SysDicValue enterpriseType = sysDicService.getDicValueByValueCode(currentTenant.getEnterpriseType());
        if (enterpriseType != null) {
            responseVO.setEnterpriseTypeName(enterpriseType.getValueName());
        }
        SysDicValue entIndustryClassification = sysDicService.getDicValueByValueCode(currentTenant.getEntIndustryClassification());
        if (entIndustryClassification != null) {
            responseVO.setEntIndustryClassificationName(entIndustryClassification.getValueName());
        }
        SysDicValue entFireType = sysDicService.getDicValueByValueCode(currentTenant.getEntFireType());
        if (entFireType != null ) {
            responseVO.setEntFireTypeName(entFireType.getValueName());
        }


        List<User> usersByTenantId = userService.getUsersByTenantId(currentTenant.getId());
        Role entAdminCode = roleService.getRoleByCode(RoleCodeConstants.ENT_ADMIN_CODE);
        List<User> entAdminUserList = usersByTenantId.stream().filter(item -> item.getRoleId().equals(entAdminCode.getId())).collect(Collectors.toList());

        List<EnterpriseUserInfoResponseVO> entAdminList = new ArrayList<>();
        for (User user : entAdminUserList) {
            EnterpriseUserInfoResponseVO userInfoResponseVO = new EnterpriseUserInfoResponseVO();
            BeanUtils.copyProperties(user, userInfoResponseVO);
            userInfoResponseVO.setRoleName(entAdminCode.getRoleName());
            entAdminList.add(userInfoResponseVO);
        }
        responseVO.setEntAdminList(entAdminList);

        Role entOperatorCode = roleService.getRoleByCode(RoleCodeConstants.ENT_OPERATOR_CODE);
        List<User> entOperatorUserList = usersByTenantId.stream().filter(item -> item.getRoleId().equals(entOperatorCode.getId())).collect(Collectors.toList());

        List<EnterpriseUserInfoResponseVO> entOperatorList = new ArrayList<>();
        for (User user : entOperatorUserList) {
            EnterpriseUserInfoResponseVO userInfoResponseVO = new EnterpriseUserInfoResponseVO();
            BeanUtils.copyProperties(user, userInfoResponseVO);
            userInfoResponseVO.setRoleName(entOperatorCode.getRoleName());
            entOperatorList.add(userInfoResponseVO);
        }
        responseVO.setEntOperatorList(entOperatorList);
        return responseVO;
    }

    public List<TenantResponseVO> searchTenant(TenantSearchRequestVO requestVO) {
        List<Tenant> tenants = this.tenantService.searchTenant(requestVO);
        List<TenantResponseVO> responseVOS = new ArrayList<>();
        for (Tenant tenant : tenants) {
            TenantResponseVO tenantResponseVO = new TenantResponseVO();
            BeanUtils.copyProperties(tenant, tenantResponseVO);
            responseVOS.add(tenantResponseVO);
        }
        return responseVOS;
    }
}
