package com.huajie.application.api;

import com.huajie.application.api.common.ApiResult;
import com.huajie.application.api.request.EnterpriseRegiestRequestVO;
import com.huajie.application.api.request.GovermentRegiestRequestVO;
import com.huajie.application.api.response.EnterpriseRegiestResponseVO;
import com.huajie.application.service.RegisterAppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhuxiaofeng
 * @date 2023/8/27
 */
@Api(tags = "注册相关")
@RestController
@RequestMapping(value = "register")
public class RegisterApi {

    @Autowired
    private RegisterAppService registerAppService;

    @ApiOperation("企业注册")
    @PostMapping(value = "enterprise")
    public ApiResult<EnterpriseRegiestResponseVO> regiestEnterprise(@RequestBody EnterpriseRegiestRequestVO regiestRequestVO){
        EnterpriseRegiestResponseVO enterpriseRegiestResponseVO = registerAppService.regiestEnterprise(regiestRequestVO);
        return ApiResult.ok(enterpriseRegiestResponseVO);
    }

    @ApiOperation("政府注册")
    @PostMapping(value = "goverment")
    public ApiResult<Void> regiestGoverment(@RequestBody GovermentRegiestRequestVO regiestRequestVO){
        registerAppService.regiestGoverment(regiestRequestVO);
        return ApiResult.ok();
    }

}
