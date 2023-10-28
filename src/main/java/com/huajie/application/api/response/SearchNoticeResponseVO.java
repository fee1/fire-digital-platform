package com.huajie.application.api.response;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author zhuxiaofeng
 * @date 2023/10/26
 */
@Data
public class SearchNoticeResponseVO {

    private Integer id;

    @ApiModelProperty("类型  0: 通知 / 1:通告")
    private Integer type;

    @ApiModelProperty("收件人类型：0： 企业 / 1 ： 政府机构")
    private Integer receiveType;

    @ApiModelProperty("指定范围")
    private String roleName;

    @ApiModelProperty("指定范围   0: 管理的所有政府与企业 / 1：指定的政府或者企业")
    private Integer specifyRange;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("签收率")
    private Double signInRate;

    @ApiModelProperty("有无附件, true: 存在")
    private Boolean existAppendix;

    @ApiModelProperty("状态, 0: 未发布; 1: 已发布; 2:已过期")
    private Integer status;

}
