package com.huajie.application.api.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author zhuxiaofeng
 * @date 2023/9/3
 */
@Data
public class AddGovermentAdminRequestVO {

    @ApiModelProperty("政府消防安全责任人")
    private List<UserAddRequestVO> govAdminList;

}
