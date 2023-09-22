package com.huajie.application.api.response;

import com.github.pagehelper.PageInfo;
import com.huajie.application.api.common.ApiPage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class InspectRecordResponseVO {

    @ApiModelProperty(name = "点位数量")
    private long placeCount ;

    @ApiModelProperty(name = "已检查点位数量")
    private long inspectPlaceCount ;

    @ApiModelProperty(name = "点位列表")
    private ApiPage<PlaceInspectRecordResponseVO> placePage;



}
