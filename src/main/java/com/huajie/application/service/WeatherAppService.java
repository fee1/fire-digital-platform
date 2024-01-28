package com.huajie.application.service;

import com.huajie.application.api.convertor.LiveConvertor;
import com.huajie.application.api.response.WeatherResponseVO;
import com.huajie.infrastructure.external.weather.model.Live;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhuxiaofeng
 * @date 2024/1/28
 */
@Service
@Slf4j
public class WeatherAppService {

    @Autowired
    private WeatherService weatherService;

    public WeatherResponseVO getWeather() {
        List<Live> lives = this.weatherService.getWeather();
        return WeatherResponseVO.builder().lives(lives).build();
    }
}
