package com.huajie.infrastructure.external.weather;

import com.alibaba.fastjson.JSONObject;
import com.huajie.domain.common.utils.OkHttpUtil;
import com.huajie.infrastructure.external.weather.model.WeatherResponseBody;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * 高德天气查询
 *
 * @author zhuxiaofeng
 * @date 2024/1/28
 */
@Slf4j
public class WeatherClient {


    /**
     * 根据城市编码获取城市天气
     *
     * @param cityCode 城市编码
     * @return
     */
    public static WeatherResponseBody getWeather(String cityCode){
        log.info("getWeather code:{}", cityCode);
        Map<String, String> param = new HashMap<>();
        param.put("key", "6fb6b62e942717dca866277c5f7a9690");
        param.put("city", cityCode);

        try {
            WeatherResponseBody weatherResponseBody = OkHttpUtil.get("https://restapi.amap.com/v3/weather/weatherInfo", null, param, WeatherResponseBody.class);
            log.info("getWeather request:{}, response:{}", cityCode, JSONObject.toJSONString(weatherResponseBody));
            return weatherResponseBody;
        } catch (IOException e) {
            log.error("获取天气信息失败, ", e);
            return null;
        }
    }

}
