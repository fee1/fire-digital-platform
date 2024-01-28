package com.huajie.application.service;

import com.huajie.domain.common.exception.ServerException;
import com.huajie.domain.common.oauth2.model.CustomizeGrantedAuthority;
import com.huajie.domain.common.utils.UserContext;
import com.huajie.domain.entity.Tenant;
import com.huajie.domain.service.UserService;
import com.huajie.infrastructure.external.weather.WeatherClient;
import com.huajie.infrastructure.external.weather.model.Live;
import com.huajie.infrastructure.external.weather.model.WeatherResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author zhuxiaofeng
 * @date 2024/1/28
 */
@Slf4j
@Service
public class WeatherService {

    @Autowired
    private UserService userService;

    public List<Live> getWeather() {
        CustomizeGrantedAuthority userInfo = UserContext.getCustomizeGrantedAuthority();
        Tenant tenant = userInfo.getTenant();
        Integer city = tenant.getCity();
        WeatherResponseBody weather = WeatherClient.getWeather(Integer.toString(city));
        if (Objects.isNull(weather)){
            throw new ServerException("天气查询异常");
        }
        return weather.getLives();
    }
}
