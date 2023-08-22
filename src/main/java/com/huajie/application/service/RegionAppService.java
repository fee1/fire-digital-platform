package com.huajie.application.service;

import com.huajie.application.api.response.RegionResponseVO;
import com.huajie.domain.entity.Region;
import com.huajie.domain.service.RegionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhuxiaofeng
 * @date 2023/8/22
 */
@Service
public class RegionAppService {

    @Autowired
    private RegionService regionService;

    public List<RegionResponseVO> getProvinces() {
        List<Region> regionList = regionService.getProvinces();
        List<RegionResponseVO> responseVOList = new ArrayList<>();
        for (Region region : regionList) {
            RegionResponseVO regionResponseVO = new RegionResponseVO();
            BeanUtils.copyProperties(region, regionResponseVO);
            responseVOList.add(regionResponseVO);
        }
        return responseVOList;
    }

    public List<RegionResponseVO> getRegionListByParentId(Integer parentId) {
        List<Region> regionList = regionService.getRegionListByParentId(parentId);
        List<RegionResponseVO> responseVOList = new ArrayList<>();
        for (Region region : regionList) {
            RegionResponseVO regionResponseVO = new RegionResponseVO();
            BeanUtils.copyProperties(region, regionResponseVO);
            responseVOList.add(regionResponseVO);
        }
        return responseVOList;
    }
}
