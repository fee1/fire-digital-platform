package com.huajie.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Device implements Serializable {

    private Integer id;

    private Integer tenantId;

    private Integer placeId;

    private String deviceType;

    private String deviceName;

    private String deviceNo;

    private Date productionDate;

    private Date lastUseDate;

    private Date lastReplaceDate;

    private String extinguisherType;

    private String powerType;

    private Integer operatorId;

    private String remark;

    /**
     * 设备状态  NORMAL ABNORMAL EXPIRED
     */

    private String state;

    @TableLogic(value = "0",delval = "1")
    private Integer deleted;

    @Version
    private Integer version;

    @ApiModelProperty(name = "创建时间",notes = "")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime ;
    /** 创建人 */
    @ApiModelProperty(name = "创建人",notes = "")
    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private String createUser ;
    /** 更新时间 */
    @ApiModelProperty(name = "更新时间",notes = "")
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime ;
    /** 更新人 */
    @ApiModelProperty(name = "更新人",notes = "")
    @TableField(value = "update_user", fill = FieldFill.UPDATE)
    private String updateUser ;

    @TableField(exist = false)
    private String placeName;
}
