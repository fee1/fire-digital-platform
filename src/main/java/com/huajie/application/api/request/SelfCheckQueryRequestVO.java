package com.huajie.application.api.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class SelfCheckQueryRequestVO {

    @ApiModelProperty("开始日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDateTime startDate;

    @ApiModelProperty("截止日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDateTime endDate;


}
