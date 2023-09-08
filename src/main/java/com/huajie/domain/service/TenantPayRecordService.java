package com.huajie.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huajie.domain.common.constants.PayRecordStatusConstants;
import com.huajie.domain.entity.TenantPayRecord;
import com.huajie.infrastructure.mapper.TenantPayRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhuxiaofeng
 * @date 2023/9/7
 */
@Service
public class TenantPayRecordService {

    @Autowired
    private TenantPayRecordMapper tenantPayRecordMapper;


    public List<TenantPayRecord> getAlipayRecordIsNotSuccess() {
        QueryWrapper<TenantPayRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(TenantPayRecord::getStatus, PayRecordStatusConstants.ALIPAY_WAIT_BUYER_PAY);
        return tenantPayRecordMapper.selectList(queryWrapper);
    }

    public void updateById(TenantPayRecord payRecordIsNotSuccess) {
        tenantPayRecordMapper.updateById(payRecordIsNotSuccess);
    }

    public TenantPayRecord getPayRecordByOutTradeNo(String outTradeNo) {
        QueryWrapper<TenantPayRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(TenantPayRecord::getOutTradeNo, outTradeNo);
        return tenantPayRecordMapper.selectOne(queryWrapper);
    }
}
