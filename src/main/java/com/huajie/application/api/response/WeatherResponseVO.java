package com.huajie.application.api.response;

import com.huajie.infrastructure.external.weather.model.Live;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author zhuxiaofeng
 * @date 2024/1/28
 */
@Data
@Builder
public class WeatherResponseVO {

    @ApiModelProperty("实况天气数据信息")
    private List<Live> lives;

}
