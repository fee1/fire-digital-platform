package com.huajie.domain.service;

import com.huajie.domain.entity.Tenant;
import com.huajie.infrastructure.mapper.TenantMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        this.tenantMapper.insert(tenant);
    }
}
