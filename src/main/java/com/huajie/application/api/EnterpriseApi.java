package com.huajie.application.api;

import com.huajie.application.api.common.ApiResult;
import com.huajie.application.api.response.EnterpriseInfoResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhuxiaofeng
 * @date 2023/10/23
 */
@Api(tags = "企业相关")
@RestController
@RequestMapping(value = "enterprise")
public class EnterpriseApi {

    @ApiOperation("企业信息")
    @GetMapping(value = "info")
    public ApiResult<EnterpriseInfoResponseVO> getEnterpriseInfo(){
        return ApiResult.ok();
    }

}
