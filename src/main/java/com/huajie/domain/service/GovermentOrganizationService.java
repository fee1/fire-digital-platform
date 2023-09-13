package com.huajie.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.huajie.application.api.request.EditEnterpriseRequestVO;
import com.huajie.domain.common.constants.RoleCodeConstants;
import com.huajie.domain.common.constants.TenantTypeConstants;
import com.huajie.domain.common.enums.EnterpriseFireTypeEnum;
import com.huajie.domain.common.enums.EnterpriseTypeEnum;
import com.huajie.domain.common.enums.GovernmentTypeEnum;
import com.huajie.domain.common.exception.ServerException;
import com.huajie.domain.common.utils.UserContext;
import com.huajie.domain.entity.GovIndustryMap;
import com.huajie.domain.entity.Role;
import com.huajie.domain.entity.Tenant;
import com.huajie.domain.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
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

    public Page<Tenant> getEnterpriseVerifyList(Integer pageNum, Integer pageSize, String enterpriseName) {

        Tenant currentTenant = UserContext.getCurrentTenant();

        List<GovIndustryMap> govIndustryMapByTenantId = govIndustryMapService.getGovIndustryMapByTenantId(currentTenant.getId());

        Set<String> industryClassifications = govIndustryMapByTenantId.stream().map(GovIndustryMap::getIndustryClassification).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(govIndustryMapByTenantId)){
            throw new ServerException("当前政府无管理的行业，请配置");
        }

        Page<Tenant> tenantLikeNameAndNotapprove = tenantService.getEnterpriseVerifyList(pageNum, pageSize, enterpriseName, industryClassifications);
        return tenantLikeNameAndNotapprove;
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


    /**
     * 分页获取管理企业列表
     * @param pageNum
     * @param pageSize
     * @param enterpriseName
     * @param enterpriseState 企业状态, 0 待审核, 1已审核
     * @return
     */
    public Page<Tenant> getAdminEnterpriseList(Integer pageNum, Integer pageSize, String enterpriseType, String enterpriseName,int enterpriseState) {
        Tenant currentTenant = UserContext.getCurrentTenant();

        QueryWrapper<Tenant> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Tenant::getTenantType, TenantTypeConstants.ENTERPRISE)
                .eq(Tenant::getApproveStatus, enterpriseState)
                .eq(Tenant::getProvince, currentTenant.getProvince());
        if (!Objects.isNull(currentTenant.getCity())){
            queryWrapper.lambda().eq(Tenant::getCity, currentTenant.getCity());
        }
        if (!Objects.isNull(currentTenant.getRegion())){
            queryWrapper.lambda().eq(Tenant::getRegion, currentTenant.getRegion());
        }
        if (!Objects.isNull(currentTenant.getStreet())){
            queryWrapper.lambda().eq(Tenant::getStreet, currentTenant.getStreet());
        }
        if(GovernmentTypeEnum.SubLeader.getCode().equals(currentTenant.getGovernmentType())){
            // 分管领导仅按地区匹配管辖企业

        }else if(GovernmentTypeEnum.FireDepartment.getCode().equals(currentTenant.getGovernmentType())){
            // 消防救援机构 按地区 + 企业消防安全类型:消防安全重点单位 管辖企业
            queryWrapper.lambda().eq(Tenant::getEnterpriseType, EnterpriseFireTypeEnum.ImportantFirePlace.getCode());
        }else {
            // 其余 按地区 + 管理行业
            List<GovIndustryMap> govIndustryMaps = govIndustryMapService.getGovIndustryMapByTenantId(currentTenant.getId());

            Set<String> industryClassifications = govIndustryMaps.stream().map(GovIndustryMap::getIndustryClassification).collect(Collectors.toSet());
            if (CollectionUtils.isEmpty(govIndustryMaps)){
                throw new ServerException("当前政府无管理的行业，请配置");
            }
            queryWrapper.lambda().in(Tenant::getEntIndustryClassification,industryClassifications);
        }
        if (StringUtils.isNotBlank(enterpriseType)){
            queryWrapper.lambda().eq(Tenant::getEnterpriseType, enterpriseType);
        }
        if (StringUtils.isNotBlank(enterpriseName)){
            queryWrapper.lambda().like(Tenant::getTenantName, enterpriseName);
        }
        return tenantService.pageTenantByQueryWrapper(pageNum,pageSize,queryWrapper);
    }



    /**
     * 分页获取管理政府列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    public Page<Tenant> getAdminGovernmentList(Integer pageNum, Integer pageSize, String governmentName) {
        Tenant currentTenant = UserContext.getCurrentTenant();

        QueryWrapper<Tenant> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Tenant::getTenantType, TenantTypeConstants.GOVERMENT)
                .eq(Tenant::getProvince, currentTenant.getProvince());

        if (Objects.isNull(currentTenant.getRegion()) && Objects.isNull(currentTenant.getStreet())){
            // 市级政府，仅能管理区/县级政府
            queryWrapper.lambda().eq(Tenant::getCity, currentTenant.getCity());
            queryWrapper.lambda().isNotNull(Tenant::getRegion);
            queryWrapper.lambda().isNull(Tenant::getStreet);
        } else if (!Objects.isNull(currentTenant.getRegion()) && Objects.isNull(currentTenant.getStreet())){
            // 区/县级政府，仅能管理乡镇街道级政府
            queryWrapper.lambda().eq(Tenant::getCity, currentTenant.getCity());
            queryWrapper.lambda().eq(Tenant::getRegion,currentTenant.getRegion());
            queryWrapper.lambda().isNotNull(Tenant::getStreet);
        } else if(!Objects.isNull(currentTenant.getRegion()) && !Objects.isNull(currentTenant.getStreet())){
            // 乡镇街道级政府，无可管理政府机构
            return  (Page<Tenant>) Collections.EMPTY_LIST;
        }

        // 消防救援机构,行业部门管理政府机构范围特殊，其余政府机构都属于上一级分管领导管理
        if(GovernmentTypeEnum.FireDepartment.getCode().equals(currentTenant.getGovernmentType())){
            // 消防救援机构 仅可管理 下一级消防救援机构政府
            queryWrapper.lambda().eq(Tenant::getGovernmentType,currentTenant.getGovernmentType());
        }else if(GovernmentTypeEnum.IndustrySector.getCode().equals(currentTenant.getGovernmentType())){
            // 行业部门 仅可管理 下一级同行业部门政府机构
            queryWrapper.lambda().eq(Tenant::getGovernmentType,currentTenant.getGovernmentType());
            queryWrapper.lambda().eq(Tenant::getGovIndustrySector,currentTenant.getGovIndustrySector());
        }

        if (StringUtils.isNotBlank(governmentName)){
            queryWrapper.lambda().like(Tenant::getTenantName, governmentName);
        }
        return tenantService.pageTenantByQueryWrapper(pageNum,pageSize,queryWrapper);
    }
}
