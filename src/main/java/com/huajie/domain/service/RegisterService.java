package com.huajie.domain.service;

import com.huajie.domain.entity.GovIndustryMap;
import com.huajie.domain.entity.Tenant;
import com.huajie.domain.entity.User;
import com.huajie.infrastructure.mapper.GovIndustryMapMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author zhuxiaofeng
 * @date 2023/8/27
 */
@Service
public class RegisterService {

    @Autowired
    private UserService userService;

    @Autowired
    private TenantService tenantService;

    @Autowired
    private GovIndustryMapMapper govIndustryMapMapper;

    /**
     * 企业注册
     * @param tenant 企业租户
     * @param userList 企业用户集合
     */
    public void regiestEnterprise(Tenant tenant, List<User> userList) {
        tenantService.add(tenant);
        userService.addUsers(userList);
    }

    /**
     * 政府注册
     * @param tenant 政府租户
     * @param userList 政府用户集合
     * @param entIndustryClassification 政府管理的行业
     */
    public void regiestGoverment(Tenant tenant, List<User> userList, List<String> entIndustryClassification) {
        tenantService.add(tenant);
        userService.addUsers(userList);
        if (!CollectionUtils.isEmpty(entIndustryClassification)){
            for (String code : entIndustryClassification) {
                GovIndustryMap govIndustryMap = new GovIndustryMap();
                govIndustryMap.setTenantId(tenant.getId());
                govIndustryMap.setIndustryClassification(code);
                govIndustryMapMapper.insert(govIndustryMap);
            }
        }
    }

}
