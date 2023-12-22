package com.huajie.application.api;

import com.huajie.application.api.common.ApiPage;
import com.huajie.application.api.common.ApiResult;
import com.huajie.application.api.request.PatrolQueryRequestVO;
import com.huajie.application.api.request.PlaceAddRequestVO;
import com.huajie.application.api.request.ProblemQueryRequestVO;
import com.huajie.application.api.request.ProblemReformActionVO;
import com.huajie.application.api.response.InspectDetailResponseVO;
import com.huajie.application.api.response.PlaceResponseVO;
import com.huajie.application.api.response.ProblemDetailResponseVO;
import com.huajie.application.service.ProblemReformService;
import com.huajie.domain.common.enums.ProblemActionTypeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "隐患整改相关")
@RestController
@RequestMapping("/problemReform")
public class ProblemReformApi {


    @Autowired
    private ProblemReformService problemReformService;


    @ApiOperation("分页查询企业隐患列表")
    @GetMapping("/pageEnterpriseProblemList")
    public ApiResult<ApiPage<ProblemDetailResponseVO>> pageEnterpriseProblemList(
            @RequestParam(required = false, defaultValue = "1")Integer pageNum,
            @RequestParam(required = false, defaultValue = "10")Integer pageSize,
            ProblemQueryRequestVO requestVO){

        return ApiResult.ok(ApiPage.restPage(problemReformService.pageEnterpriseProblemList(requestVO,pageNum,pageSize)));
    }

    @ApiOperation("分页查询政府检查隐患列表")
    @GetMapping("/pageGovInspectProblemList")
    public ApiResult<ApiPage<ProblemDetailResponseVO>> pageGovInspectProblemList(
            @RequestParam(required = false, defaultValue = "1")Integer pageNum,
            @RequestParam(required = false, defaultValue = "10")Integer pageSize,
            ProblemQueryRequestVO requestVO){
        return ApiResult.ok(ApiPage.restPage(problemReformService.pageGovInspectProblemList(requestVO,pageNum,pageSize)));
    }

    @ApiOperation("分页查询政府管辖企业隐患列表")
    @GetMapping("/pageAdminEnterpriseProblemList")
    public ApiResult<ApiPage<ProblemDetailResponseVO>> pageAdminEnterpriseProblemList(
            @RequestParam(required = false, defaultValue = "1")Integer pageNum,
            @RequestParam(required = false, defaultValue = "10")Integer pageSize,
            ProblemQueryRequestVO requestVO){
        return ApiResult.ok(ApiPage.restPage(problemReformService.pageAdminEnterpriseProblemList(requestVO,pageNum,pageSize)));
    }

    @ApiOperation("查询隐患明细")
    @GetMapping("/getProblemDetail")
    public ApiResult<ProblemDetailResponseVO> getProblemList(Long problemId){
        return ApiResult.ok(problemReformService.getProblemDetailById(problemId));
    }

    @ApiOperation("签收")
    @PostMapping("/sign")
    public ApiResult sign(@RequestBody @Valid ProblemReformActionVO actionVO){
        problemReformService.doAction(ProblemActionTypeEnum.SIGN,actionVO);
        return ApiResult.ok();
    }

    @ApiOperation("回复整改意见")
    @PostMapping("/reply")
    public ApiResult reply(@RequestBody @Valid ProblemReformActionVO actionVO){
        problemReformService.doAction(ProblemActionTypeEnum.REPLY,actionVO);
        return ApiResult.ok();
    }


    @ApiOperation("整改")
    @PostMapping("/reform")
    public ApiResult reform(@RequestBody @Valid ProblemReformActionVO actionVO){
        problemReformService.doAction(ProblemActionTypeEnum.REFORM,actionVO);
        return ApiResult.ok();
    }


    @ApiOperation("延迟整改")
    @PostMapping("/delay")
    public ApiResult delay(@RequestBody @Valid ProblemReformActionVO actionVO){
        problemReformService.doAction(ProblemActionTypeEnum.DELAY,actionVO);
        return ApiResult.ok();
    }

    @ApiOperation("整改审批通过")
    @PostMapping("/reformApprovePass")
    public ApiResult reformApprovePass(@RequestBody @Valid ProblemReformActionVO actionVO){
        problemReformService.doAction(ProblemActionTypeEnum.REFORM_APPROVE_PASS,actionVO);
        return ApiResult.ok();
    }


    @ApiOperation("整改审批拒绝")
    @PostMapping("/reformApproveReject")
    public ApiResult reformApproveReject(@RequestBody @Valid ProblemReformActionVO actionVO){
        problemReformService.doAction(ProblemActionTypeEnum.REFORM_APPROVE_REJECT,actionVO);
        return ApiResult.ok();
    }

    @ApiOperation("延迟整改审批通过")
    @PostMapping("/delayApprovePass")
    public ApiResult delayApprovePass(@RequestBody @Valid ProblemReformActionVO actionVO){
        problemReformService.doAction(ProblemActionTypeEnum.DELAY_APPROVE_PASS,actionVO);
        return ApiResult.ok();
    }


    @ApiOperation("延迟整改审批拒绝")
    @PostMapping("/delayApproveReject")
    public ApiResult delayApproveReject(@RequestBody @Valid ProblemReformActionVO actionVO){
        problemReformService.doAction(ProblemActionTypeEnum.DELAY_APPROVE_REJECT,actionVO);
        return ApiResult.ok();
    }

    @ApiOperation("催促整改")
    @PostMapping("/urge")
    public ApiResult urge(@RequestBody @Valid ProblemReformActionVO actionVO){
        problemReformService.doAction(ProblemActionTypeEnum.URGE,actionVO);
        return ApiResult.ok();
    }


}
