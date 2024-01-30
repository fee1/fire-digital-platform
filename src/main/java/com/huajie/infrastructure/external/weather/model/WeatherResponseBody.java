package com.huajie.infrastructure.external.weather.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author zhuxiaofeng
 * @date 2024/1/28
 */
@Data
public class WeatherResponseBody {

    @ApiModelProperty("返回状态 1：成功；0：失败")
    private Integer status;

    @ApiModelProperty("返回结果总数目")
    private Integer count;

    @ApiModelProperty("返回的状态信息")
    private String info;

    @ApiModelProperty("返回状态说明,10000代表正确")
    private String infocode;

    @ApiModelProperty("实况天气数据信息")
    private List<Live> lives;


}
