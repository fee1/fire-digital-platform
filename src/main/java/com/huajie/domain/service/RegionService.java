package com.huajie.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huajie.domain.common.constants.RegionLevelConstants;
import com.huajie.domain.entity.Region;
import com.huajie.infrastructure.mapper.RegionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhuxiaofeng
 * @date 2023/8/22
 */
@Service
public class RegionService {

    @Autowired
    private RegionMapper regionMapper;

    /**
     * 获取省
     * @return
     */
    public List<Region> getProvinces() {
        QueryWrapper<Region> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Region::getRegionLevel, RegionLevelConstants.PROVINCE_LEVEL);
        return regionMapper.selectList(queryWrapper);
    }

    /**
     * 获取市 | 区县 |街道
     * @param parentId 上级行政区id
     * @return
     */
    public List<Region> getRegionListByParentId(Integer parentId) {
        QueryWrapper<Region> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Region::getParentId, parentId);
        return regionMapper.selectList(queryWrapper);
    }

}
