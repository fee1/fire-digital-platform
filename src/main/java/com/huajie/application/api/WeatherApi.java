package com.huajie.application.api;

import com.huajie.application.api.common.ApiResult;
import com.huajie.application.api.response.WeatherResponseVO;
import com.huajie.application.service.WeatherAppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhuxiaofeng
 * @date 2024/1/28
 */
@Api(tags = "天气API")
@RestController
@RequestMapping("/weather")
@Slf4j
public class WeatherApi {

    @Autowired
    private WeatherAppService weatherAppService;

    @ApiOperation("获取当前用户地址天气")
    @GetMapping("/get")
    public ApiResult<WeatherResponseVO> getWeather(){
        return ApiResult.ok(weatherAppService.getWeather());
    }

}
