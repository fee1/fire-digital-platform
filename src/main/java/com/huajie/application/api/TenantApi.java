package com.huajie.application.api;

import com.huajie.application.api.common.ApiResult;
import com.huajie.application.api.response.EnterpriseInfoResponseVO;
import com.huajie.application.service.TenantAppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhuxiaofeng
 * @date 2023/8/7
 */
@Api(tags = "租户相关")
@RestController
@RequestMapping("tenant")
public class TenantApi {

    @Autowired
    private TenantAppService tenantAppService;

    @ApiOperation("企业信息")
    @GetMapping(value = "enterprise/info")
    public ApiResult<EnterpriseInfoResponseVO> getEnterpriseInfo(){
        EnterpriseInfoResponseVO responseVO = tenantAppService.getEnterpriseInfo();
        return ApiResult.ok(responseVO);
    }



}
