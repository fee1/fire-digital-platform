package com.huajie.application.service;

import com.huajie.application.api.request.GeneratePayQrcodeImageRequestVO;
import com.huajie.application.api.response.GeneratePayRecordResponseVO;
import com.huajie.application.api.response.QrcodeImageResponseVO;
import com.huajie.application.api.response.TenantPayRecordResponseVO;
import com.huajie.domain.common.constants.PayChannelConstants;
import com.huajie.domain.common.enums.PayRecordReasonEnum;
import com.huajie.domain.common.utils.UserContext;
import com.huajie.domain.entity.TenantPayRecord;
import com.huajie.domain.model.EnterpriseRegiestDTO;
import com.huajie.domain.service.PayService;
import org.apache.commons.lang3.StringUtils;
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

    public QrcodeImageResponseVO generatePayQrcodeImage(GeneratePayQrcodeImageRequestVO requestVO) {
        EnterpriseRegiestDTO enterpriseRegiestDTO = payService.generatePayQrcodeImage(requestVO.getOutTradeNo());
        QrcodeImageResponseVO qrcodeImageResponseVO = new QrcodeImageResponseVO();
        qrcodeImageResponseVO.setAmount(enterpriseRegiestDTO.getAmount().toPlainString());
        if (StringUtils.equals(requestVO.getChannel(), PayChannelConstants.ALIPAY_CHANNEL)){
            qrcodeImageResponseVO.setOrderId(enterpriseRegiestDTO.getAlipayOrderId());
            qrcodeImageResponseVO.setQrcodeUrl(enterpriseRegiestDTO.getAlipayQrcodeUrl());
        }else if (StringUtils.equals(requestVO.getChannel(), PayChannelConstants.WECHAT_CHANNEL)){
            qrcodeImageResponseVO.setOrderId(enterpriseRegiestDTO.getWechatPayOrderId());
            qrcodeImageResponseVO.setQrcodeUrl(enterpriseRegiestDTO.getWechatPayQrcodeUrl());
        }
        return qrcodeImageResponseVO;
    }

    public GeneratePayRecordResponseVO generatePayRecord() {
        return this.payService.generatePayRecord(PayRecordReasonEnum.ENTERPRISE_USER_RENEWAL, UserContext.getCurrentTenant());
    }
}
