package com.huajie.application.api.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author zhuxiaofeng
 * @date 2023/10/26
 */
@Data
public class SearchNoticeRequestVO {

    @ApiModelProperty("标题")
    private String title;

}
