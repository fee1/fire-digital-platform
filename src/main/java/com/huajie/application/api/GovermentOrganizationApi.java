package com.huajie.application.api;

import com.huajie.application.api.common.ApiResult;
import com.huajie.application.api.request.AddGovermentAdminRequestVO;
import com.huajie.application.api.request.AddGovermentOperatorRequestVO;
import com.huajie.application.api.request.EditEnterpriseRequestVO;
import com.huajie.application.api.request.EditGovermentInfoRequestVO;
import com.huajie.application.api.request.EnterpriseVerifyRequestVO;
import com.huajie.application.api.response.EnterpriseResponseVO;
import com.huajie.application.service.GovermentOrganizationAppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zhuxiaofeng
 * @date 2023/8/27
 */
@Api(tags = "政府端-组织管理")
@RestController
@RequestMapping("goverment/organization")
public class GovermentOrganizationApi {

    @Autowired
    private GovermentOrganizationAppService govermentOrganizationAppService;

    @ApiOperation("政府基本信息编辑")
    @PostMapping("edit/info")
    public ApiResult<Void> editInfo(@RequestBody EditGovermentInfoRequestVO requestVO){
        govermentOrganizationAppService.editGovermentInfo(requestVO);
        return ApiResult.ok();
    }

    @ApiOperation("新增企业消防安全责任人")
    @PostMapping("add/admin/user")
    public ApiResult<Void> addAdminUser(@RequestBody AddGovermentAdminRequestVO requestVO){
        govermentOrganizationAppService.addAdminUser(requestVO);
        return ApiResult.ok();
    }

    @ApiOperation("新增企业消防安全责任人")
    @PostMapping("add/operator/user")
    public ApiResult<Void> addOperatorUser(@RequestBody AddGovermentOperatorRequestVO requestVO){
        govermentOrganizationAppService.addOperatorUser(requestVO);
        return ApiResult.ok();
    }

    @ApiOperation("企业审核列表")
    @GetMapping("enterprise/verify/list")
    public ApiResult<List<EnterpriseResponseVO>> getEnterpriseVerifyList(@RequestParam(required = false)@ApiParam("企业名称") String enterpriseName){
        List<EnterpriseResponseVO> enterpriseResponseVOList = govermentOrganizationAppService.getEnterpriseVerifyList(enterpriseName);
        return ApiResult.ok(enterpriseResponseVOList);
    }

    @ApiOperation("企业审核 编辑")
    @PostMapping("enterprise/edit")
    public ApiResult<Void> editEnterprise(@RequestBody EditEnterpriseRequestVO requestVO){
        govermentOrganizationAppService.editEnterprise(requestVO);
        return ApiResult.ok();
    }

    @ApiOperation("企业审核")
    @PostMapping("enterprise/verify")
    public ApiResult<Void> enterpriseVerifyPass(@RequestBody EnterpriseVerifyRequestVO requestVO){
        govermentOrganizationAppService.enterpriseVerifyPass(requestVO.getEnterpriseId());
        return ApiResult.ok();
    }

    @ApiOperation("管辖企业")
    @GetMapping("enterprise/list")
    public ApiResult<List<EnterpriseResponseVO>> getEnterpriseList(@RequestParam(required = false)@ApiParam("企业名称") String enterpriseName){
        List<EnterpriseResponseVO> enterpriseResponseVOList = govermentOrganizationAppService.getEnterpriseList(enterpriseName);
        return ApiResult.ok(enterpriseResponseVOList);
    }

}
