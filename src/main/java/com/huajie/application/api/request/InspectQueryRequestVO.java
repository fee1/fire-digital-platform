package com.huajie.application.api.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class InspectQueryRequestVO {


    @ApiModelProperty("企业id")
    @NotNull
    Integer enterpriseId;

    @ApiModelProperty("点位id")
    Integer placeId;

    @ApiModelProperty("点位名称 模糊查询")
    String placeName;

    @ApiModelProperty("开始日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDateTime startDate;

    @ApiModelProperty("截止日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDateTime endDate;


}
