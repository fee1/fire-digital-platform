package com.huajie.application.api.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author zhuxiaofeng
 * @date 2023/9/3
 */
@Data
public class AddGovermentOperatorRequestVO {

    @ApiModelProperty("政府消防安全管理人")
    private List<UserAddRequestVO> govOperatorList;

}
