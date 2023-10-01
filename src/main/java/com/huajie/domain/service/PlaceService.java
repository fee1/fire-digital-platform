package com.huajie.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.huajie.application.api.common.exception.ApiException;
import com.huajie.domain.common.oauth2.model.CustomizeGrantedAuthority;
import com.huajie.domain.common.utils.UserContext;
import com.huajie.domain.entity.Place;
import com.huajie.domain.entity.Tenant;
import com.huajie.infrastructure.mapper.PlaceMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaceService {

    @Autowired
    private PlaceMapper placeMapper;

    public Integer getPlaceCountByTenantId(Integer tenantId){
        QueryWrapper<Place> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Place::getTenantId,tenantId);
        return placeMapper.selectCount(queryWrapper);
    }


    public Page<Place> getPagePlaceList(Integer pageNum, Integer pageSize, Integer placeId, String placeName, String placeAddress,Integer tenantId){
        QueryWrapper<Place> queryWrapper = new QueryWrapper<>();
        if (placeId != null){
            queryWrapper.lambda().eq(Place::getId,placeId);
        }
        if (StringUtils.isNotBlank(placeName)){
            queryWrapper.lambda().like(Place::getPlaceName, placeName);
        }
        if (StringUtils.isNotBlank(placeAddress)){
            queryWrapper.lambda().like(Place::getPlaceAddress, placeAddress);
        }
        queryWrapper.lambda().eq(Place::getTenantId,tenantId);
        queryWrapper.lambda().eq(Place::getDeleted,"0");

        PageHelper.startPage(pageNum, pageSize);
        return (Page<Place>) placeMapper.selectList(queryWrapper);
    }

    public Place addPlace(Place place){
        Place one = placeMapper.selectOne(new QueryWrapper<Place>().lambda().eq(Place::getNfcCode, place.getNfcCode()).eq(Place::getDeleted, "0"));
        if(one != null){
            throw new ApiException("添加点位失败，该点位信息NFCCode已存在！");
        }

        CustomizeGrantedAuthority customizeGrantedAuthority = UserContext.getCustomizeGrantedAuthority();
        place.setOperatorId(customizeGrantedAuthority.getUserId());
        place.setTenantId(customizeGrantedAuthority.getTenant().getId());
        placeMapper.insert(place);
        return place;
    }

    public int editPlace(Place place){
        Place updatePlace = new Place();
        updatePlace.setId(place.getId());
        updatePlace.setVersion(place.getVersion());
        updatePlace.setPlaceName(place.getPlaceName());
        updatePlace.setPlaceAddress(place.getPlaceAddress());
        updatePlace.setRemark(place.getRemark());
        return placeMapper.updateById(updatePlace);
    }

    public int deletePlace(Integer placeId){
        return placeMapper.deleteById(placeId);
    }

    public Place getPlaceById(Integer placeId){
        return placeMapper.selectById(placeId);
    }

    public Place getPlaceByNFCCode(String nfcCode){
        return placeMapper.selectOne(new QueryWrapper<Place>().lambda().eq(Place::getNfcCode, nfcCode).eq(Place::getDeleted, "0"));
    }

}
