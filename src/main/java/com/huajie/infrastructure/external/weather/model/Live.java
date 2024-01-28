package com.huajie.infrastructure.external.weather.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhuxiaofeng
 * @date 2024/1/28
 */
@Data
public class Live {

    @ApiModelProperty("省份名")
    private String province;

    @ApiModelProperty("城市名")
    private String city;

    @ApiModelProperty("区域编码")
    private String adcode;

    @ApiModelProperty("天气现象（汉字描述）")
    private String weather;

    @ApiModelProperty("实时气温，单位：摄氏度")
    private Integer temperature;

    @ApiModelProperty("风向描述")
    private String winddirection;

    @ApiModelProperty("风力级别，单位：级")
    private String windpower;

    @ApiModelProperty("空气湿度")
    private Integer humidity;

    @ApiModelProperty("数据发布的时间")
    private String reporttime;

}
