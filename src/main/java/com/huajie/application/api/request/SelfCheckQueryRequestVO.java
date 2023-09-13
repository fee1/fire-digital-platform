package com.huajie.application.api.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class SelfCheckQueryRequestVO {

    @ApiModelProperty("开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDateTime startTime;

    @ApiModelProperty("截止时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDateTime endTime;


}
