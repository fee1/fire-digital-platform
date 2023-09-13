package com.huajie.application.api.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class AddInspectRequestVO {


    @NotNull(message = "检查类型")
    @ApiModelProperty("检查类型 patrol 巡查，inspect 检查 selfCheck综合自查")
    private String inspectType ;

    @NotNull(message = "检查结果")
    @ApiModelProperty("检查结果 pass/error")
    private String inspectResult ;

    @ApiModelProperty("检查明细")
    private String inspectDetail ;

    @ApiModelProperty("点位id pass/error")
    private Integer placeId ;

    @ApiModelProperty("设备id pass/error")
    private Integer deviceId ;

    /** 问题描述 */
    @ApiModelProperty("问题描述")
    private String problemDesc ;

    /** 预整改方案 */
    @ApiModelProperty("预整改方案")
    private String preReformDesc ;

    /** 隐患图片1 */
    private String problemPic1 ;
    /** 隐患图片2 */
    private String problemPic2 ;
    /** 隐患图片3 */
    private String problemPic3 ;
    /** 隐患图片4 */
    private String problemPic4 ;
    /** 隐患图片5 */
    private String problemPic5 ;
    /** 隐患图片6 */
    private String problemPic6 ;
    /** 隐患图片7 */
    private String problemPic7 ;
    /** 隐患图片8 */
    private String problemPic8 ;

    /** 点位名称 */
    private String placeName ;
    /** 点位地址 */
    private String placeAddress ;
    /** 设备名称 */
    private String deviceName ;
    /** 企业租户id */
    private Integer entTenantId ;

}
