package com.huajie;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.huajie.application.api.common.ApiResult;
import com.huajie.application.api.response.RegionResponseVO;

public class SimpleTest {

    public static void main(String[] args) {
        ApiResult<RegionResponseVO> apiResult = new ApiResult<>();
        RegionResponseVO regionResponseVO = new RegionResponseVO();
        regionResponseVO.setGovernmentName("123123");
        apiResult.setCode("1");
        apiResult.setData(regionResponseVO);
        apiResult.setMessage("112323");

        String json = JSON.toJSONString(apiResult);
        ApiResult<RegionResponseVO> responseVOApiResult = toApiResult(json, RegionResponseVO.class);
        System.out.println(responseVOApiResult.getData().getGovernmentName());
    }

    public static <T>ApiResult<T> toApiResult(String json, Class<T> tClass){
        return JSON.parseObject(json, new TypeReference<ApiResult<T>>(tClass){});
    }

}
