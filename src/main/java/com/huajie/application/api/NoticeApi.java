package com.huajie.application.api;

import com.github.pagehelper.Page;
import com.huajie.application.api.common.ApiPage;
import com.huajie.application.api.common.ApiResult;
import com.huajie.application.api.request.CreateNoticeRequestVO;
import com.huajie.application.api.request.EditNoticeRequestVO;
import com.huajie.application.api.request.PublicNoticeRequestVO;
import com.huajie.application.api.response.EntPcNoticeResponseVO;
import com.huajie.application.api.response.GovPcNoticeResponseVO;
import com.huajie.application.api.response.NoticeAppDetailResponseVO;
import com.huajie.application.api.response.NoticeDetailResponseVO;
import com.huajie.application.api.response.SearchNoticeResponseVO;
import com.huajie.application.service.NoticeAppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;

/**
 * @author zhuxiaofeng
 * @date 2023/10/21
 */
@Api(tags = "通知通告相关")
@RequestMapping("notice")
@RestController
public class NoticeApi {

    @Autowired
    private NoticeAppService noticeAppService;

    @ApiOperation("创建通知/通告")
    @PostMapping("create")
    public ApiResult<Void> createNotice(@RequestBody@Valid CreateNoticeRequestVO requestVO){
        noticeAppService.createNotice(requestVO);
        return ApiResult.ok();
    }

    @ApiOperation("政府PC 通知通告管理列表")
    @GetMapping("search")
    public ApiResult<ApiPage<SearchNoticeResponseVO>> searchNotice(@RequestParam(required = false) String title,
                                                                   @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                                   @RequestParam(required = false, defaultValue = "10") Integer pageSize){
        Page<SearchNoticeResponseVO> responseVOPage = noticeAppService.searchNotice(title, pageNum, pageSize);
        return ApiResult.ok(ApiPage.restPage(responseVOPage));
    }

    @ApiOperation("政府PC 通知通告管理-发布操作")
    @PostMapping("public")
    public ApiResult<Void> publicNotice(@RequestBody@Valid PublicNoticeRequestVO requestVO){
        noticeAppService.publicNotice(requestVO.getId());
        return ApiResult.ok();
    }

    @ApiOperation("政府PC 通知通告管理-编辑操作")
    @PostMapping("edit")
    public ApiResult<Void> editNotice(@RequestBody@Valid EditNoticeRequestVO requestVO){
        noticeAppService.editNotice(requestVO);
        return ApiResult.ok();
    }

    @ApiOperation("政府PC 通知通告管理-详情")
    @GetMapping("detail")
    public ApiResult<NoticeDetailResponseVO> detailNotice(@RequestParam(required = true)@ApiParam("公告通知id") Integer noticeId){
        NoticeDetailResponseVO noticeDetailResponseVO = noticeAppService.detailNotice(noticeId);
        return ApiResult.ok(noticeDetailResponseVO);
    }

    @ApiOperation("政府PC 通知/通告 列表")
    @GetMapping("gov/list")
    public ApiResult<ApiPage<GovPcNoticeResponseVO>> getGovPcNoticeList(@RequestParam(required = false)@ApiParam("日期") Date date,
                                                                        @RequestParam(required = false)@ApiParam("标题") String title,
                                                                        @RequestParam(required = false)@ApiParam("发送人") String sendUserName,
                                                                        @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                                        @RequestParam(required = false, defaultValue = "10") Integer pageSize){
        Page<GovPcNoticeResponseVO> responseVOPage = this.noticeAppService.getGovPcNoticeList(date, title, sendUserName, pageNum, pageSize);
        return ApiResult.ok(ApiPage.restPage(responseVOPage));
    }

    @ApiOperation("企业PC 通知/通告 列表")
    @GetMapping("ent/list")
    public ApiResult<ApiPage<EntPcNoticeResponseVO>> getEntPcNoticeList(@RequestParam(required = false)@ApiParam("日期") Date date,
                                                                        @RequestParam(required = false)@ApiParam("标题") String title,
                                                                        @RequestParam(required = false)@ApiParam("发送人") String sendUserName,
                                                                        @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                                        @RequestParam(required = false, defaultValue = "10") Integer pageSize){
        Page<EntPcNoticeResponseVO> responseVOPage = this.noticeAppService.getEntPcNoticeList(date, title, sendUserName, pageNum, pageSize);
        return ApiResult.ok(ApiPage.restPage(responseVOPage));
    }

    @ApiOperation("签收")
    @PutMapping("receive")
    public ApiResult<Void> receive(@RequestParam(required = true)@ApiParam("公告通知id") Integer noticeId){
        this.noticeAppService.receive(noticeId);
        return ApiResult.ok();
    }

    @ApiOperation("小程序通知通知通告详情")
    @GetMapping("app/detail")
    public ApiResult<NoticeAppDetailResponseVO> appDetail(@RequestParam(required = true)@ApiParam("公告通知id") Integer noticeId){
        NoticeAppDetailResponseVO responseVO = this.noticeAppService.appDetail(noticeId);
        return ApiResult.ok(responseVO);
    }

}
