package com.huajie.application.api.response.statistic;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class GovIndexResponseVO {

    @ApiModelProperty("整改率")
    private BigDecimal reformRate;

    @ApiModelProperty("隐患数量")
    private int problemCount;

    @ApiModelProperty("今日新增隐患数量")
    private int todayNewProblemCount;

    @ApiModelProperty("未完成隐患数量")
    private int unfinishedProblemCount;

    @ApiModelProperty("今日新增未完成隐患数量")
    private int todayNewUnfinishedProblemCount;

    @ApiModelProperty("待审核用户数量")
    private int unApproveEnterpriseCount;

    @ApiModelProperty("总管理用户量")
    private int adminEnterpriseCount;

    @ApiModelProperty("月度检查覆盖用户数")
    private int inspectEnterpriseCountInCurrentMonth;

    @ApiModelProperty("政府消防安全管理人检查统计list")
    private List<GovUserInspectCountResponseVO> govUserInspectCountList;

    @ApiModelProperty("近30日新增用户")
    private int before30DaysNewEnterpriseCount;

    @ApiModelProperty("总用户")
    private int totalCount;

    @ApiModelProperty("机关团体事业单位数量")
    private int companyCount;

    @ApiModelProperty("企业、场所数量")
    private int enterpriseCount;

    @ApiModelProperty("商户")
    private int merchantCount;

    @ApiModelProperty("出租屋")
    private int rentalHouseCount;

    @ApiModelProperty("未签收通知数")
    private int unsignedNoticeCount;


}
