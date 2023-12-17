package com.huajie.application.api.response.statistic;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WorkMessageResponseVO {

    @ApiModelProperty("消息类型: problem:隐患, notice 通知, ")
    private String messageType;

    @ApiModelProperty("消息内容")
    private String message;





}
