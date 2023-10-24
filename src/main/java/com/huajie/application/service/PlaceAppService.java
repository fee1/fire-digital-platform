package com.huajie.application.service;


import com.github.pagehelper.Page;
import com.huajie.application.api.common.ApiPage;
import com.huajie.application.api.common.ApiResult;
import com.huajie.application.api.common.exception.ApiException;
import com.huajie.application.api.request.PlaceAddRequestVO;
import com.huajie.application.api.request.PlaceEditRequestVO;
import com.huajie.application.api.request.PlaceQueryRequestVO;
import com.huajie.application.api.response.DeviceResponseVO;
import com.huajie.application.api.response.MenuResponseVO;
import com.huajie.application.api.response.PlaceResponseVO;
import com.huajie.application.api.response.RegionResponseVO;
import com.huajie.domain.common.utils.UserContext;
import com.huajie.domain.entity.Device;
import com.huajie.domain.entity.Menu;
import com.huajie.domain.entity.Place;
import com.huajie.domain.entity.Tenant;
import com.huajie.domain.service.DeviceService;
import com.huajie.domain.service.PlaceService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PlaceAppService {

    @Autowired
    private PlaceService placeService;

    public Page<PlaceResponseVO> pageEntPlaceList(Integer pageNum, Integer pageSize, PlaceQueryRequestVO requestVO){
        Page<Place> page = placeService.getPagePlaceList(pageNum, pageSize, requestVO.getPlaceId(), requestVO.getPlaceName(), requestVO.getPlaceAddress(), UserContext.getCurrentTenant().getId());
        Page<PlaceResponseVO> result = new Page<>();
        BeanUtils.copyProperties(page,result);
        for (Place place: page) {
            PlaceResponseVO placeResponseVO = new PlaceResponseVO();
            BeanUtils.copyProperties(place,placeResponseVO);
            result.add(placeResponseVO);
        }
        return result;
    }

    public PlaceResponseVO addPlace(PlaceAddRequestVO requestVO){
        Place place = new Place();
        BeanUtils.copyProperties(requestVO,place);
        place = placeService.addPlace(place);

        PlaceResponseVO placeResponseVO = new PlaceResponseVO();
        BeanUtils.copyProperties(place,placeResponseVO);
        return placeResponseVO;
    }

    public void editPlace(PlaceEditRequestVO requestVO){
        Place place = new Place();
        BeanUtils.copyProperties(requestVO,place);
        if(placeService.editPlace(place) < 1){
            throw new ApiException("更新点位失败，请重试");
        }
    }

    public void deletePlace(Integer placeId){
        if(placeService.deletePlace(placeId) < 1){
            throw new ApiException("删除点位失败，请重试");
        }
    }


}
