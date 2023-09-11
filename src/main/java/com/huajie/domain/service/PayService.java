package com.huajie.domain.service;

import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.huajie.domain.common.constants.PayChannelConstants;
import com.huajie.domain.common.constants.PayRecordStatusConstants;
import com.huajie.domain.common.constants.SystemConstants;
import com.huajie.domain.common.exception.ServerException;
import com.huajie.domain.common.oauth2.model.TenantModel;
import com.huajie.domain.common.utils.UserContext;
import com.huajie.domain.entity.TenantPayRecord;
import com.huajie.domain.entity.User;
import com.huajie.domain.model.EnterpriseRegiestDTO;
import com.huajie.infrastructure.external.pay.CustomAlipayClient;
import com.huajie.infrastructure.external.pay.WechatPayClient;
import com.huajie.infrastructure.external.pay.model.AmountModel;
import com.huajie.infrastructure.external.pay.model.WechatPayCreateOrderModel;
import com.huajie.infrastructure.external.pay.model.WechatPayCreateRespModel;
import com.huajie.infrastructure.mapper.TenantPayRecordMapper;
import com.zxf.method.trace.util.TraceFatch;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zhuxiaofeng
 * @date 2023/9/8
 */
@Service
public class PayService {

    @Autowired
    private TenantPayRecordService tenantPayRecordService;

    @Autowired
    private CustomAlipayClient customAlipayClient;

    @Autowired
    private CommonService commonService;

    @Autowired
    private UserService userService;

    @Autowired
    private WechatPayClient wechatPayClient;

    @Value("${unit.price:5}")
    private Double unitPrice;

    @Value("${wechat.pay.appId:}")
    private String wechatAppId;

    @Value("${wechat.pay.mchId:}")
    private String mchId;

    @Value("${wechat.pay.notify-url}")
    private String wechatNotifyUrl;

    @Autowired
    private TenantPayRecordMapper tenantPayRecordMapper;

    public TenantPayRecord getPayRecordByOrderId(String orderId) {
        return tenantPayRecordService.getPayRecordByOutTradeNo(orderId);
    }

    @Transactional
    public EnterpriseRegiestDTO generatePayQrcodeImage(String orderId, String channel) {
        TenantModel currentTenant = UserContext.getCurrentTenant();
        Integer tenantId = currentTenant.getId();
        List<User> usersByTenantId = userService.getUsersByTenantId(tenantId);
        BigDecimal amount = new BigDecimal(unitPrice * usersByTenantId.size());

        TenantPayRecord payRecordByOrderId = this.getPayRecordByOrderId(orderId);
        String outTradeNo = "";
        if (StringUtils.equals(payRecordByOrderId.getStatus(), PayRecordStatusConstants.ALIPAY_TRADE_CLOSED)){
            outTradeNo = TraceFatch.getTraceId();

            //生成预缴费记录
            TenantPayRecord tenantPayRecord = new TenantPayRecord();
//        tenantPayRecord.setPayChannel(PayChannelConstants.ALIPAY_CHANNEL);
            tenantPayRecord.setStatus(PayRecordStatusConstants.ALIPAY_WAIT_BUYER_PAY);
            tenantPayRecord.setTenantId(tenantId);
            tenantPayRecord.setOutTradeNo(outTradeNo);
            tenantPayRecord.setTotalAmount(amount.setScale(2, BigDecimal.ROUND_HALF_UP));
            tenantPayRecord.setCreateUser(SystemConstants.SYSTEM);
            tenantPayRecordMapper.insert(tenantPayRecord);
        }else {
            outTradeNo = payRecordByOrderId.getOutTradeNo();
        }

        String subject = "企业用户注册: " + currentTenant.getTenantName();
        if (StringUtils.equals(channel, PayChannelConstants.ALIPAY_CHANNEL)) {
            String alipayQrcodeFileName = "alipay_" + outTradeNo + ".png";
            AlipayTradePrecreateResponse response = this.alipayPreCreateOrder(outTradeNo, subject, amount.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
            String alipayQrcodeUrl = commonService.generateQrImageAndUpLoadAliyun(response.getQrCode(), alipayQrcodeFileName);

            EnterpriseRegiestDTO enterpriseRegiestDTO = new EnterpriseRegiestDTO();
            enterpriseRegiestDTO.setAmount(amount.setScale(2, BigDecimal.ROUND_HALF_UP));
            //设置 alipay 缴费订单号和二维码地址
            enterpriseRegiestDTO.setAlipayOrderId(outTradeNo);
            enterpriseRegiestDTO.setAlipayQrcodeUrl(alipayQrcodeUrl);

            return enterpriseRegiestDTO;
        }else if (StringUtils.equals(channel, PayChannelConstants.WECHAT_CHANNEL)) {

            String wechatpayQrcodeFileName = "wechatpay_" + outTradeNo + ".png";
            WechatPayCreateRespModel wechatPayCreateRespModel = this.wechatPayCreateOrder(outTradeNo, subject, amount);
            String wechatpayQrcodeUrl = commonService.generateQrImageAndUpLoadAliyun(wechatPayCreateRespModel.getCodeUrl(), wechatpayQrcodeFileName);
            EnterpriseRegiestDTO enterpriseRegiestDTO = new EnterpriseRegiestDTO();
            enterpriseRegiestDTO.setAmount(amount.setScale(2, BigDecimal.ROUND_HALF_UP));
            //设置订单号和二维码地址
            enterpriseRegiestDTO.setAlipayOrderId(outTradeNo);
            enterpriseRegiestDTO.setAlipayQrcodeUrl(wechatpayQrcodeUrl);


            return enterpriseRegiestDTO;
        }else {
            throw new ServerException("没有这个渠道");
        }
    }

    /**
     * 支付宝二维码扫码付款
     *
     * @param outTradeNo 商家订单号，自己生成的
     * @param subject 订单标题
     * @param totalAmount 金额
     * @return 二维码图片链接
     */
    public AlipayTradePrecreateResponse alipayPreCreateOrder(String outTradeNo, String subject, String totalAmount){
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setOutTradeNo(outTradeNo);
        model.setTotalAmount(totalAmount);
        model.setSubject(subject);
        return customAlipayClient.preCreateOrder(model);
    }

    public WechatPayCreateRespModel wechatPayCreateOrder(String outTradeNo, String description, BigDecimal totalAmount){
        //设置 wechat 缴费订单号和二维码地址
        WechatPayCreateOrderModel wechatPayCreateOrderModel = new WechatPayCreateOrderModel();
        wechatPayCreateOrderModel.setAppId(wechatAppId);
        wechatPayCreateOrderModel.setDescription(description);
        wechatPayCreateOrderModel.setMchId(mchId);
        AmountModel amountModel = new AmountModel();
        amountModel.setTotal(totalAmount.multiply(new BigDecimal(100)).intValue());
        wechatPayCreateOrderModel.setAmount(amountModel);
        wechatPayCreateOrderModel.setOutTradeNo(outTradeNo);
        wechatPayCreateOrderModel.setNotifyUrl(wechatNotifyUrl);
        return wechatPayClient.preCreateOrder(wechatPayCreateOrderModel);
    }

}
