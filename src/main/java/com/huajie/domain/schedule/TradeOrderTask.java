package com.huajie.domain.schedule;

import com.alipay.api.response.AlipayTradeQueryResponse;
import com.huajie.domain.common.constants.PayChannelConstants;
import com.huajie.domain.common.constants.PayRecordStatusConstants;
import com.huajie.domain.common.constants.SystemConstants;
import com.huajie.domain.common.constants.TenantStatusConstants;
import com.huajie.domain.entity.Tenant;
import com.huajie.domain.entity.TenantPayRecord;
import com.huajie.domain.service.TenantPayRecordService;
import com.huajie.domain.service.TenantService;
import com.huajie.infrastructure.external.pay.CustomAlipayClient;
import com.huajie.infrastructure.external.pay.WechatPayClient;
import com.huajie.infrastructure.external.pay.model.WechatPayCheckRespModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * @author zhuxiaofeng
 * @date 2023/9/7
 */
@Component
public class TradeOrderTask {

    @Autowired
    private TenantPayRecordService tenantPayRecordService;

    @Autowired
    private CustomAlipayClient customAlipayClient;

    @Autowired
    private WechatPayClient wechatPayClient;

    @Autowired
    private TenantService tenantService;

    @Scheduled(cron = "*/20 * * * * *")
    public void tradeRecordSync(){
        List<TenantPayRecord> tenantPayRecordIsNotSuccess = tenantPayRecordService.getAlipayRecordIsNotSuccess();
        if (!CollectionUtils.isEmpty(tenantPayRecordIsNotSuccess)) {
            for (TenantPayRecord payRecordIsNotSuccess : tenantPayRecordIsNotSuccess) {
                AlipayTradeQueryResponse alipayTradeQueryResponse = customAlipayClient.checkOrder(payRecordIsNotSuccess.getOutTradeNo());
                // 支付宝订单号
                if (StringUtils.equals(alipayTradeQueryResponse.getTradeStatus(), PayRecordStatusConstants.ALIPAY_TRADE_SUCCESS)
                        || StringUtils.equals(alipayTradeQueryResponse.getTradeStatus(), PayRecordStatusConstants.ALIPAY_TRADE_FINISHED)) {
                    payRecordIsNotSuccess.setStatus(alipayTradeQueryResponse.getTradeStatus());
                    payRecordIsNotSuccess.setTradeNo(alipayTradeQueryResponse.getTradeNo());
                    payRecordIsNotSuccess.setPayAmount(alipayTradeQueryResponse.getBuyerLogonId());
                    payRecordIsNotSuccess.setPayChannel(PayChannelConstants.ALIPAY_CHANNEL);
                    payRecordIsNotSuccess.setDate(alipayTradeQueryResponse.getSendPayDate());
                    payRecordIsNotSuccess.setUpdateUser(SystemConstants.SYSTEM);
                    payRecordIsNotSuccess.setUpdateTime(new Date());
                    //更新交易记录
                    tenantPayRecordService.updateById(payRecordIsNotSuccess);

                    //更新租户状态为可用状态
                    Tenant tenantByTenantId = tenantService.getTenantByTenantId(payRecordIsNotSuccess.getTenantId());
                    tenantByTenantId.setStatus(TenantStatusConstants.ENABLE);
                    tenantService.updateById(tenantByTenantId);
                    return;
                }

                WechatPayCheckRespModel wechatPayCheckRespModel = wechatPayClient.checkOrder(payRecordIsNotSuccess.getOutTradeNo());
                if (StringUtils.equals(wechatPayCheckRespModel.getTradeState(), PayRecordStatusConstants.WECHAT_PAY_SUCCESS)){
                    payRecordIsNotSuccess.setStatus(wechatPayCheckRespModel.getTradeState());
                    payRecordIsNotSuccess.setTradeNo(wechatPayCheckRespModel.getTransactionId());
                    payRecordIsNotSuccess.setPayAmount(wechatPayCheckRespModel.getPayer().getOpenId());
                    payRecordIsNotSuccess.setPayChannel(PayChannelConstants.WECHAT_CHANNEL);
                    payRecordIsNotSuccess.setDate(wechatPayCheckRespModel.getSuccessTime());
                    payRecordIsNotSuccess.setUpdateUser(SystemConstants.SYSTEM);
                    payRecordIsNotSuccess.setUpdateTime(new Date());
                    //更新交易记录
                    tenantPayRecordService.updateById(payRecordIsNotSuccess);

                    //更新租户状态为可用状态
                    Tenant tenantByTenantId = tenantService.getTenantByTenantId(payRecordIsNotSuccess.getTenantId());
                    tenantByTenantId.setStatus(TenantStatusConstants.ENABLE);
                    tenantService.updateById(tenantByTenantId);
                    return;
                }

                //支付宝+微信 都超时未支付
                if (StringUtils.equals(alipayTradeQueryResponse.getTradeStatus(), PayRecordStatusConstants.ALIPAY_TRADE_CLOSED)
                        && StringUtils.equals(wechatPayCheckRespModel.getTradeState(), PayRecordStatusConstants.WECHAT_PAY_CLOSED)){
                    payRecordIsNotSuccess.setStatus(PayRecordStatusConstants.ALIPAY_TRADE_CLOSED);
                    payRecordIsNotSuccess.setUpdateUser(SystemConstants.SYSTEM);
                    payRecordIsNotSuccess.setUpdateTime(new Date());
                    //更新交易记录
                    tenantPayRecordService.updateById(payRecordIsNotSuccess);
                }
            }
        }
    }


}
