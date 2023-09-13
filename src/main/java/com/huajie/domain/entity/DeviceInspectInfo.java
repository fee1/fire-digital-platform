package com.huajie.domain.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class DeviceInspectInfo {

    @ApiModelProperty(name = "id",notes = "")
    private Integer id ;
    /** 设备类别 */
    @ApiModelProperty(name = "设备类别",notes = "")
    private String deviceType ;
    /** 检查编号 */
    @ApiModelProperty(name = "检查编号",notes = "")
    private String checkNo ;
    /** 检查内容 */
    @ApiModelProperty(name = "检查内容",notes = "")
    private String checkInfo ;
    /** 正确选项 */
    @ApiModelProperty(name = "正确选项",notes = "")
    private String correctOption ;
    /** 错误选项 */
    @ApiModelProperty(name = "错误选项",notes = "")
    private String wrongOption ;
    /** 创建时间 */
    @ApiModelProperty(name = "创建时间",notes = "")
    private Date createTime ;
    /** 创建人 */
    @ApiModelProperty(name = "创建人",notes = "")
    private String createUser ;
    /** 更新时间 */
    @ApiModelProperty(name = "更新时间",notes = "")
    private Date updateTime ;
    /** 更新人 */
    @ApiModelProperty(name = "更新人",notes = "")
    private String updateUser ;
}
