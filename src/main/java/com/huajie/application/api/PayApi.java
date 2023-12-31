package com.huajie.application.api;

import com.alibaba.fastjson.JSONObject;
import com.huajie.application.api.common.ApiResult;
import com.huajie.application.api.response.TenantPayRecordResponseVO;
import com.huajie.application.service.PayAppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author zhuxiaofeng
 * @date 2023/9/7
 */
@Api(tags = "支付相关")
@RestController
@RequestMapping("pay")
@Slf4j
public class PayApi {

    @Autowired
    private PayAppService payAppService;

    @ApiOperation("根据订单号查询订单")
    @GetMapping("record/select")
    public ApiResult<TenantPayRecordResponseVO> getPayRecordByOrderId(@RequestParam String orderId){
        TenantPayRecordResponseVO tenantPayRecordResponseVO = payAppService.getPayRecordByOrderId(orderId);
        return ApiResult.ok(tenantPayRecordResponseVO);
    }

    @ApiOperation("支付回调")
    @PostMapping("callback")
    public ApiResult<Void> callback(@RequestBody Map<String, String> requestVO){
        log.debug("pay callback: {}", JSONObject.toJSONString(requestVO));
        return ApiResult.ok();
    }

}
