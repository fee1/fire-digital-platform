package com.huajie.infrastructure.external.weather;


import com.huajie.infrastructure.external.weather.model.WeatherResponseBody;
import org.junit.Test;

/**
 * @author zhuxiaofeng
 * @date 2024/1/28
 */
public class WeatherClientTest {

    @Test
    public void getWeather() {
        WeatherResponseBody weather = WeatherClient.getWeather("330700");
        System.out.println(weather);
    }
}