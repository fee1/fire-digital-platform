package com.huajie.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.huajie.application.api.request.TenantSearchRequestVO;
import com.huajie.domain.common.constants.SystemConstants;
import com.huajie.domain.common.constants.TenantTypeConstants;
import com.huajie.domain.common.utils.UserContext;
import com.huajie.domain.entity.Tenant;
import com.huajie.domain.entity.User;
import com.huajie.infrastructure.mapper.TenantMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhuxiaofeng
 * @date 2023/8/6
 */
@Service
public class TenantService {

    @Autowired
    private TenantMapper tenantMapper;

    @Autowired
    private GovermentOrganizationService govermentOrganizationService;

    public Tenant getTenantByTenantId(Integer tenantId) {
        return tenantMapper.selectById(tenantId);
    }

    public void add(Tenant tenant) {
        tenant.setCreateUser(SystemConstants.SYSTEM);
        tenant.setCreateTime(new Date());
        this.tenantMapper.insert(tenant);
    }

    public void updateById(Tenant tenant) {
        tenantMapper.updateById(tenant);
    }

    public Page<Tenant> pageTenantByQueryWrapper(Integer pageNum, Integer pageSize,QueryWrapper<Tenant> queryWrapper){
        PageHelper.startPage(pageNum, pageSize);
        return (Page<Tenant>)tenantMapper.selectList(queryWrapper);
    }

    public Integer getCountByQueryWrapper(QueryWrapper<Tenant> queryWrapper){
        return tenantMapper.selectCount(queryWrapper);
    }

    public List<Tenant> getListByQueryWrapper(QueryWrapper<Tenant> queryWrapper){
        return tenantMapper.selectList(queryWrapper);
    }

    public Page<Tenant> getEnterpriseVerifyList(Integer pageNum, Integer pageSize, String enterpriseName, Collection<String> industryClassifications) {
        PageHelper.startPage(pageNum, pageSize);
        Tenant tenant = UserContext.getCurrentTenant();

        QueryWrapper<Tenant> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Tenant::getTenantType, TenantTypeConstants.ENTERPRISE)
                .eq(Tenant::getApproveStatus, 0)
                .eq(Tenant::getProvince, tenant.getProvince())
                .in(Tenant::getEntIndustryClassification, industryClassifications);
        if (!Objects.isNull(tenant.getCity())){
            queryWrapper.lambda().eq(Tenant::getCity, tenant.getCity());
        }
        if (!Objects.isNull(tenant.getRegion())){
            queryWrapper.lambda().eq(Tenant::getRegion, tenant.getRegion());
        }
        if (!Objects.isNull(tenant.getStreet())){
            queryWrapper.lambda().eq(Tenant::getStreet, tenant.getStreet());
        }
        if (StringUtils.isNotBlank(enterpriseName)){
            queryWrapper.lambda().like(Tenant::getTenantName, enterpriseName);
        }
        return (Page<Tenant>)tenantMapper.selectList(queryWrapper);
    }

    public Page<Tenant> getEnterpriseList(Integer pageNum, Integer pageSize, String enterpriseName, Collection<String> industryClassifications) {
        PageHelper.startPage(pageNum, pageSize);
        Tenant tenant = UserContext.getCurrentTenant();

        QueryWrapper<Tenant> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Tenant::getTenantType, TenantTypeConstants.ENTERPRISE)
                .eq(Tenant::getApproveStatus, 1)
                .eq(Tenant::getProvince, tenant.getProvince())
                .in(Tenant::getEntIndustryClassification, industryClassifications);

        if (!Objects.isNull(tenant.getCity())){
            queryWrapper.lambda().eq(Tenant::getCity, tenant.getCity());
        }
        if (!Objects.isNull(tenant.getRegion())){
            queryWrapper.lambda().eq(Tenant::getRegion, tenant.getRegion());
        }
        if (!Objects.isNull(tenant.getStreet())){
            queryWrapper.lambda().eq(Tenant::getStreet, tenant.getStreet());
        }
        if (StringUtils.isNotBlank(enterpriseName)){
            queryWrapper.lambda().like(Tenant::getTenantName, enterpriseName);
        }
        if (StringUtils.isNotBlank(enterpriseName)){
            queryWrapper.lambda().like(Tenant::getTenantName, enterpriseName);
        }
        return (Page<Tenant>) tenantMapper.selectList(queryWrapper);
    }


    public Map<Integer, String> getTenantNameMap(Collection<Integer> tenantId){
        List<Tenant> tenants = tenantMapper.selectBatchIds(tenantId);
        return tenants.stream().collect(Collectors.toMap(Tenant::getId, Tenant::getTenantName));
    }

    public List<Tenant> getTenantByTenantIds(List<Integer> tenantIds) {
        QueryWrapper<Tenant> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(Tenant::getId, tenantIds);
        return this.tenantMapper.selectList(queryWrapper);
    }

    public List<Tenant> searchTenant(TenantSearchRequestVO requestVO) {
        List<Tenant> tenants = new ArrayList<>();
        if (requestVO.getSearchType() == 1){
            Page<Tenant> adminEnterpriseList = this.govermentOrganizationService.getAdminEnterpriseList(1, Integer.MAX_VALUE, requestVO.getEnterpriseType(), requestVO.getTenantName(), 1);

            for (Tenant tenant : adminEnterpriseList) {
                if (requestVO.getRegionId() != null && requestVO.getRegionId().equals(tenant.getRegion())){
                    if (requestVO.getStreetId() != null && requestVO.getStreetId().equals(tenant.getStreet())){
                        tenants.add(tenant);
                    }
                }
            }
            return tenants;
        }else {
            Page<Tenant> adminEnterpriseList = this.govermentOrganizationService.getAdminGovernmentList(1, Integer.MAX_VALUE, requestVO.getTenantName());
            for (Tenant tenant : adminEnterpriseList) {
                if (requestVO.getRegionId() != null && requestVO.getRegionId().equals(tenant.getRegion())){
                    if (requestVO.getStreetId() != null && requestVO.getStreetId().equals(tenant.getStreet())){
                        tenants.add(tenant);
                    }
                }
            }
            return tenants;
        }
    }
}
