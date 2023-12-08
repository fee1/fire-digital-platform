package com.huajie.application.api.response.statistic;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GovUserInspectCountResponseVO {

    private Integer userId;

    private String headPic;

    private String userName;

    @ApiModelProperty("月检查覆盖用户数")
    private Integer inspectEnterpriseCount;

    @ApiModelProperty("月累计发现隐患数")
    private Integer submitProblemCount;

    @ApiModelProperty("进入新增隐患数")
    private Integer todaySubmitProblemCount;


    public GovUserInspectCountResponseVO() {
        this.inspectEnterpriseCount = 0;
        this.submitProblemCount = 0;
        this.todaySubmitProblemCount = 0;
    }
}
