package com.huajie.application.api;

import com.github.pagehelper.Page;
import com.huajie.application.api.common.ApiPage;
import com.huajie.application.api.common.ApiResult;
import com.huajie.application.api.request.CreateNoticeRequestVO;
import com.huajie.application.api.request.EditNoticeRequestVO;
import com.huajie.application.api.request.PublicNoticeRequestVO;
import com.huajie.application.api.response.NoticeDetailResponseVO;
import com.huajie.application.api.response.SearchNoticeResponseVO;
import com.huajie.application.service.NoticeAppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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
    public ApiResult<NoticeDetailResponseVO> detailNotice(@RequestParam(required = true) Integer noticeId){
        return ApiResult.ok();
    }

}
