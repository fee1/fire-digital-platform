package com.huajie.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
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
}
