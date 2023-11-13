package com.huajie.application.api.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

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
    private String problemPic1 ;
    /** 图片2 */
    @ApiModelProperty(name = "图片2",notes = "")
    private String problemPic2 ;
    /** 图片3 */
    @ApiModelProperty(name = "图片3",notes = "")
    private String problemPic3 ;
    /** 图片4 */
    @ApiModelProperty(name = "图片4",notes = "")
    private String problemPic4 ;
    /** 图片5 */
    @ApiModelProperty(name = "图片5",notes = "")
    private String problemPic5 ;
    /** 图片6 */
    @ApiModelProperty(name = "图片6",notes = "")
    private String problemPic6 ;
    /** 图片7 */
    @ApiModelProperty(name = "图片7",notes = "")
    private String problemPic7 ;
    /** 图片8 */
    @ApiModelProperty(name = "图片8",notes = "")
    private String problemPic8 ;
}
