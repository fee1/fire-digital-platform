package com.huajie.application.api;

import com.github.pagehelper.Page;
import com.huajie.application.api.common.ApiPage;
import com.huajie.application.api.common.ApiResult;
import com.huajie.application.api.request.AddGovermentAdminRequestVO;
import com.huajie.application.api.request.AddGovermentOperatorRequestVO;
import com.huajie.application.api.request.EditEnterpriseRequestVO;
import com.huajie.application.api.request.EditGovermentInfoRequestVO;
import com.huajie.application.api.request.EnterpriseVerifyRequestVO;
import com.huajie.application.api.response.EnterpriseResponseVO;
import com.huajie.application.api.response.GovermentInfoResponseVO;
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

import javax.validation.Valid;
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

    @ApiOperation("政府基本信息")
    @GetMapping("info")
    public ApiResult<GovermentInfoResponseVO> getInfo(){
        GovermentInfoResponseVO govermentInfoResponseVO = govermentOrganizationAppService.getInfo();
        return ApiResult.ok(govermentInfoResponseVO);
    }

    @ApiOperation("政府基本信息编辑")
    @PostMapping("edit/info")
    public ApiResult<Void> editInfo(@Valid@RequestBody EditGovermentInfoRequestVO requestVO){
        govermentOrganizationAppService.editGovermentInfo(requestVO);
        return ApiResult.ok();
    }

    @ApiOperation("新增企业消防安全责任人")
    @PostMapping("add/admin/user")
    public ApiResult<Void> addAdminUser(@Valid@RequestBody AddGovermentAdminRequestVO requestVO){
        govermentOrganizationAppService.addAdminUser(requestVO);
        return ApiResult.ok();
    }

    @ApiOperation("新增企业消防安全责任人")
    @PostMapping("add/operator/user")
    public ApiResult<Void> addOperatorUser(@Valid@RequestBody AddGovermentOperatorRequestVO requestVO){
        govermentOrganizationAppService.addOperatorUser(requestVO);
        return ApiResult.ok();
    }

    @ApiOperation("企业审核列表")
    @GetMapping("enterprise/verify/list")
    public ApiResult<ApiPage<EnterpriseResponseVO>> getEnterpriseVerifyList(@ApiParam("当前页码")@RequestParam(required = false, defaultValue = "0") Integer pageNum,
                                                                         @ApiParam("每页数量")@RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                                         @RequestParam(required = false)@ApiParam("企业名称") String enterpriseName){
        Page<EnterpriseResponseVO> enterpriseResponseVOList = govermentOrganizationAppService.getEnterpriseVerifyList(pageNum, pageSize, null, enterpriseName);
        return ApiResult.ok(ApiPage.restPage(enterpriseResponseVOList));
    }

    @ApiOperation("企业审核 编辑")
    @PostMapping("enterprise/edit")
    public ApiResult<Void> editEnterprise(@Valid@RequestBody EditEnterpriseRequestVO requestVO){
        govermentOrganizationAppService.editEnterprise(requestVO);
        return ApiResult.ok();
    }

    @ApiOperation("企业审核通过")
    @PostMapping("enterprise/verify")
    public ApiResult<Void> enterpriseVerifyPass(@Valid@RequestBody EnterpriseVerifyRequestVO requestVO){
        govermentOrganizationAppService.enterpriseVerifyPass(requestVO.getEnterpriseId());
        return ApiResult.ok();
    }

    @ApiOperation("管辖企业")
    @GetMapping("enterprise/list")
    public ApiResult<ApiPage<EnterpriseResponseVO>> getEnterpriseList(@ApiParam("当前页码")@RequestParam(required = false, defaultValue = "0") Integer pageNum,
                                                                      @ApiParam("每页数量")@RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                                      @ApiParam("企业名称")@RequestParam(required = false) String enterpriseName,
                                                                      @ApiParam("企业类型")@RequestParam(required = false) String enterpriseType){
        Page<EnterpriseResponseVO> enterpriseResponseVOList = govermentOrganizationAppService.getAdminEnterpriseList(pageNum, pageSize,enterpriseType, enterpriseName);
        return ApiResult.ok(ApiPage.restPage(enterpriseResponseVOList));
    }

}
