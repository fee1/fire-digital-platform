package com.huajie.application.service;

import com.huajie.domain.common.exception.ServerException;
import com.huajie.domain.common.oauth2.model.CustomizeGrantedAuthority;
import com.huajie.domain.common.utils.GuavaUtil;
import com.huajie.domain.common.utils.UserContext;
import com.huajie.domain.entity.Tenant;
import com.huajie.domain.service.UserService;
import com.huajie.infrastructure.external.weather.WeatherClient;
import com.huajie.infrastructure.external.weather.model.Live;
import com.huajie.infrastructure.external.weather.model.WeatherResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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



    public Live getWeather() {
        CustomizeGrantedAuthority userInfo = UserContext.getCustomizeGrantedAuthority();
        Tenant tenant = userInfo.getTenant();
        Integer city = tenant.getCity();
        Live live =(Live) GuavaUtil.get(String.valueOf(city));
        if (Objects.isNull(live)) {
            WeatherResponseBody weather = WeatherClient.getWeather(Integer.toString(city));

            if (Objects.isNull(weather) && CollectionUtils.isEmpty(weather.getLives())) {
                throw new ServerException("天气查询异常");
            }
            live = weather.getLives().get(0);
            GuavaUtil.set(Integer.toString(city), live, 3600);
        }
        return live;
    }
}
