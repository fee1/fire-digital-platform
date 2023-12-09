package com.huajie.application.api.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author zhuxiaofeng
 * @date 2023/12/9
 */
@Data
public class ReceiveRequestVO {

    @ApiModelProperty("Integer noticeId")
    @NotNull
    private Integer noticeId;

}
