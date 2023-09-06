package com.huajie.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huajie.domain.common.constants.PayChannelConstants;
import com.huajie.domain.common.constants.TenantStatusConstants;
import com.huajie.domain.entity.AlipayNotifyCallbackInfo;
import com.huajie.domain.entity.Tenant;
import com.huajie.domain.entity.TenantPayRecord;
import com.huajie.infrastructure.mapper.AlipayNotifyCallbackInfoMapper;
import com.huajie.infrastructure.mapper.TenantPayRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhuxiaofeng
 * @date 2023/9/6
 */
@Service
public class AlipayNotifyService {

    @Autowired
    private AlipayNotifyCallbackInfoMapper alipayNotifyCallbackInfoMapper;

    @Autowired
    private TenantPayRecordMapper tenantPayRecordMapper;

    @Autowired
    private TenantService tenantService;

    public void prePayQrcode(AlipayNotifyCallbackInfo alipayNotifyCallbackInfo) {
        alipayNotifyCallbackInfoMapper.insert(alipayNotifyCallbackInfo);

        //todo 回查支付宝，保证安全性
        QueryWrapper<TenantPayRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(TenantPayRecord::getOutTradeNo, alipayNotifyCallbackInfo.getOutTradeNo());
        TenantPayRecord tenantPayRecord = tenantPayRecordMapper.selectOne(queryWrapper);
        tenantPayRecord.setPayAmount(alipayNotifyCallbackInfo.getSellerEmail());
        tenantPayRecord.setPayChannel(PayChannelConstants.ALIPAY_CHANNEL);
        tenantPayRecord.setDate(alipayNotifyCallbackInfo.getGmtPayment());
        tenantPayRecord.setRemark(alipayNotifyCallbackInfo.getSubject());
        //更新支付记录
        tenantPayRecordMapper.updateById(tenantPayRecord);

        //更新租户启用状态
        Tenant tenant = tenantService.getTenantByTenantId(tenantPayRecord.getTenantId());
        tenant.setStatus(TenantStatusConstants.ENABLE);
        tenantService.updateById(tenant);
    }
}
