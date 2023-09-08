package com.huajie.application.service;

import com.huajie.application.api.response.TenantPayRecordResponseVO;
import com.huajie.domain.entity.TenantPayRecord;
import com.huajie.domain.service.PayService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhuxiaofeng
 * @date 2023/9/8
 */
@Service
public class PayAppService {

    @Autowired
    private PayService payService;

    public TenantPayRecordResponseVO getPayRecordByOrderId( String orderId) {
        TenantPayRecord tenantPayRecord = payService.getPayRecordByOrderId(orderId);
        TenantPayRecordResponseVO tenantPayRecordResponseVO = new TenantPayRecordResponseVO();
        BeanUtils.copyProperties(tenantPayRecord, tenantPayRecordResponseVO);
        return tenantPayRecordResponseVO;
    }
}
