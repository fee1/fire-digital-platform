package com.huajie.application.api.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
public class ProblemReformActionVO {

    /** 隐患id */
    @ApiModelProperty(name = "隐患id",notes = "")
    @NotNull
    private Long problemId ;
    /** 备注说明 */
    @ApiModelProperty(name = "备注说明",notes = "")
    @NotEmpty
    private String remark ;
    /** 图片1 */
    @ApiModelProperty(name = "图片1",notes = "")
    private List<String> problemPicList ;
}
