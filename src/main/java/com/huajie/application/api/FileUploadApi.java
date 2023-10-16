package com.huajie.application.api;

import com.huajie.application.api.common.ApiResult;
import com.huajie.application.api.response.SignResponseVO;
import com.huajie.application.service.FileUploadAppService;
import com.huajie.domain.service.CommonService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文件上传API
 *
 * @author zhuxiaofeng
 * @date 2023/10/16
 */
@Api(tags = "文件上传相关API")
@RestController
@RequestMapping(value = "file/upload/")
public class FileUploadApi {

    @Autowired
    private FileUploadAppService fileUploadAppService;

    @GetMapping("/sign")
    public ApiResult<SignResponseVO> getSign(){
        SignResponseVO signResponseVO = fileUploadAppService.getSign();
        return ApiResult.ok(signResponseVO);
    }

}
