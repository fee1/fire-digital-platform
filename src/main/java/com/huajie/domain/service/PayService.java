package com.huajie.domain.service;

import com.huajie.domain.entity.TenantPayRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhuxiaofeng
 * @date 2023/9/8
 */
@Service
public class PayService {

    @Autowired
    private TenantPayRecordService tenantPayRecordService;

    public TenantPayRecord getPayRecordByOrderId(String orderId) {
        return tenantPayRecordService.getPayRecordByOutTradeNo(orderId);
    }
}
