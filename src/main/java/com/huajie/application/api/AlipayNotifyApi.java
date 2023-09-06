package com.huajie.application.api;

import com.huajie.application.api.common.ApiResult;
import com.huajie.application.service.AlipayNotifyAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author zhuxiaofeng
 * @date 2023/9/6
 */
@RestController
@RequestMapping("alipay/notify")
public class AlipayNotifyApi {

    @Autowired
    private AlipayNotifyAppService alipayNotifyAppService;

    @PostMapping("pre/pay/qrcode")
    public ApiResult<Void> prePayQrcode(@RequestBody Map<String, String> requestVO){
        alipayNotifyAppService.prePayQrcode(requestVO);
        return ApiResult.ok();
    }

}
