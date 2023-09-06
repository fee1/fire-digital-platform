package com.huajie.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huajie.domain.entity.GovIndustryMap;
import com.huajie.infrastructure.mapper.GovIndustryMapMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author zhuxiaofeng
 * @date 2023/9/6
 */
@Service
public class GovIndustryMapService {

    @Autowired
    private GovIndustryMapMapper govIndustryMapMapper;


    public List<GovIndustryMap> getGovIndustryMapByTenantId(Integer tenantId) {
        QueryWrapper<GovIndustryMap> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GovIndustryMap::getTenantId, tenantId);
        return govIndustryMapMapper.selectList(queryWrapper);
    }

    public void deleteByTenantIdAndGovIndustryMaps(Integer tenantId, Set<String> delGovIndustryMaps) {
        QueryWrapper<GovIndustryMap> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(GovIndustryMap::getTenantId, tenantId)
                .in(GovIndustryMap::getIndustryClassification, delGovIndustryMaps);
        govIndustryMapMapper.delete(queryWrapper);
    }

    public void insertGovIndustryMap(GovIndustryMap govIndustryMap) {
        govIndustryMapMapper.insert(govIndustryMap);
    }

}
