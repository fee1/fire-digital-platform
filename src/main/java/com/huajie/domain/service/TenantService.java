package com.huajie.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huajie.domain.common.constants.SystemConstants;
import com.huajie.domain.entity.Tenant;
import com.huajie.infrastructure.mapper.TenantMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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

    public List<Tenant> getTenantLikeNameAndNotapprove(String enterpriseName) {
        QueryWrapper<Tenant> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Tenant::getApproveStatus, 0);
        if (StringUtils.isNotBlank(enterpriseName)){
            queryWrapper.lambda().like(Tenant::getTenantName, enterpriseName);
        }
        return tenantMapper.selectList(queryWrapper);
    }

    public List<Tenant> getTenantLikeNameAndApprove(String enterpriseName) {
        QueryWrapper<Tenant> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Tenant::getApproveStatus, 1);
        if (StringUtils.isNotBlank(enterpriseName)){
            queryWrapper.lambda().like(Tenant::getTenantName, enterpriseName);
        }
        return tenantMapper.selectList(queryWrapper);
    }
}
