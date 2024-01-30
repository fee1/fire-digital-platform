package com.huajie.domain.model;

import com.huajie.application.api.common.FileModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author zhuxiaofeng
 * @date 2024/1/28
 */
@Data
public class SysCreateNotice {

    @ApiModelProperty("指定用户发送通知")
    private List<Integer> userIdList;

    @ApiModelProperty("类型  0: 通知 / 1:通告")
    @NotNull(message = "type: 类型不能为空")
    private Integer type;

    @ApiModelProperty("标题")
    @NotNull(message = "title:标题不能为空")
    @NotBlank(message = "title:标题不能为空")
    private String title;

    @ApiModelProperty("正文")
    @NotBlank(message = "text:正文不能为空")
    @NotNull(message = "text:正文不能为空")
    private String text;

    @ApiModelProperty("指定的企业/政府 ids")
    private List<Integer> tenantIds;


    @ApiModelProperty("附件")
    private List<FileModel> appendix;

    @ApiModelProperty("通知保留时间 1个月，3个月，6个月，1年, 单位天")
    private Integer saveDays;

}
