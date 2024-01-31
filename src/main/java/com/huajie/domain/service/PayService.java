package com.huajie.domain.service;

import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.huajie.application.api.response.GeneratePayRecordResponseVO;
import com.huajie.domain.common.constants.CommonConstants;
import com.huajie.domain.common.constants.PayRecordStatusConstants;
import com.huajie.domain.common.constants.SystemConstants;
import com.huajie.domain.common.enums.PayRecordReasonEnum;
import com.huajie.domain.common.utils.AssertUtil;
import com.huajie.domain.entity.Tenant;
import com.huajie.domain.entity.TenantPayRecord;
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
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

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

    @Autowired
    private TenantService tenantService;

//    @Value("${unit.price:5}")
//    private Double unitPrice;

    @Autowired
    private Environment environment;

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

    @Transactional(rollbackFor = Exception.class)
    public EnterpriseRegiestDTO generatePayQrcodeImage(String orderId) {
        TenantPayRecord payRecordByOrderId = this.getPayRecordByOrderId(orderId);
        Tenant currentTenant = tenantService.getTenantByTenantId(payRecordByOrderId.getTenantId());

        String priceStr = environment.getProperty(CommonConstants.ENTERPRISE_TYPE_PRE + currentTenant.getEnterpriseType());
        BigDecimal amount = new BigDecimal(priceStr);

        String outTradeNo = "";
        if (StringUtils.equals(payRecordByOrderId.getStatus(), PayRecordStatusConstants.ALIPAY_TRADE_CLOSED)){
            outTradeNo = TraceFatch.getTraceId();

            //生成预缴费记录
            TenantPayRecord tenantPayRecord = new TenantPayRecord();
//        tenantPayRecord.setPayChannel(PayChannelConstants.ALIPAY_CHANNEL);
            tenantPayRecord.setStatus(PayRecordStatusConstants.ALIPAY_WAIT_BUYER_PAY);
            tenantPayRecord.setTenantId(currentTenant.getId());
            tenantPayRecord.setOutTradeNo(outTradeNo);
            tenantPayRecord.setTotalAmount(amount.setScale(2, BigDecimal.ROUND_HALF_UP));
            tenantPayRecord.setCreateUser(SystemConstants.SYSTEM);

            EnterpriseRegiestDTO enterpriseRegiestDTO = new EnterpriseRegiestDTO();
            enterpriseRegiestDTO.setAmount(amount.setScale(2, BigDecimal.ROUND_HALF_UP));
            String subject = "企业用户注册: " + currentTenant.getTenantName();
            String alipayQrcodeFileName = "alipay_" + outTradeNo + ".png";
            AlipayTradePrecreateResponse response = this.alipayPreCreateOrder(outTradeNo, subject, amount.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
            String alipayQrcodeUrl = commonService.generateQrImageAndUpLoadAliyun(response.getQrCode(), alipayQrcodeFileName);
            tenantPayRecord.setAlipayQrcodeUrl(alipayQrcodeUrl);


            //设置 alipay 缴费订单号和二维码地址
            enterpriseRegiestDTO.setAlipayOrderId(outTradeNo);
            enterpriseRegiestDTO.setAlipayQrcodeUrl(alipayQrcodeUrl);


            String wechatpayQrcodeFileName = "wechatpay_" + outTradeNo + ".png";
            WechatPayCreateRespModel wechatPayCreateRespModel = this.wechatPayCreateOrder(outTradeNo, subject, amount);
            String wechatpayQrcodeUrl = commonService.generateQrImageAndUpLoadAliyun(wechatPayCreateRespModel.getCodeUrl(), wechatpayQrcodeFileName);
            tenantPayRecord.setWechatPayQrcodeUrl(wechatpayQrcodeUrl);
            //设置订单号和二维码地址
            enterpriseRegiestDTO.setWechatPayOrderId(outTradeNo);
            enterpriseRegiestDTO.setWechatPayQrcodeUrl(wechatpayQrcodeUrl);

            tenantPayRecordMapper.insert(tenantPayRecord);
            return enterpriseRegiestDTO;
        }else {
            outTradeNo = payRecordByOrderId.getOutTradeNo();
            EnterpriseRegiestDTO enterpriseRegiestDTO = new EnterpriseRegiestDTO();
            enterpriseRegiestDTO.setAmount(amount.setScale(2, BigDecimal.ROUND_HALF_UP));
            enterpriseRegiestDTO.setWechatPayQrcodeUrl(payRecordByOrderId.getWechatPayQrcodeUrl());
            enterpriseRegiestDTO.setAlipayQrcodeUrl(payRecordByOrderId.getAlipayQrcodeUrl());
            enterpriseRegiestDTO.setWechatPayOrderId(outTradeNo);
            enterpriseRegiestDTO.setAlipayOrderId(outTradeNo);
            return enterpriseRegiestDTO;
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

    @Transactional(rollbackFor = Exception.class)
    public GeneratePayRecordResponseVO generatePayRecord(PayRecordReasonEnum payRecordReasonEnum , Tenant tenant) {

        TenantPayRecord tenantPayRecord = this.tenantPayRecordService.getAlipayRecordIsNotSuccessByTenantId();
        if (tenantPayRecord != null){
            return GeneratePayRecordResponseVO.builder().orderId(tenantPayRecord.getOutTradeNo()).build();
        }

        //支付宝预下单，生成付款二维码
        String priceStr = environment.getProperty(CommonConstants.ENTERPRISE_TYPE_PRE + tenant.getEnterpriseType());
        BigDecimal amount = new BigDecimal(priceStr);

        EnterpriseRegiestDTO enterpriseRegiestDTO = new EnterpriseRegiestDTO();
        enterpriseRegiestDTO.setAmount(amount.setScale(2, BigDecimal.ROUND_HALF_UP));

        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        String outTradeNo = TraceFatch.getTraceId();
        model.setOutTradeNo(outTradeNo);
        model.setTotalAmount(amount.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
        model.setSubject(payRecordReasonEnum.getDescription() + ":" + tenant.getTenantName());
        AlipayTradePrecreateResponse response = customAlipayClient.preCreateOrder(model);

        String alipayQrcodeFileName = "alipay_" + outTradeNo+".png";
        String alipayQrcodeUrl = commonService.generateQrImageAndUpLoadAliyun(response.getQrCode(), alipayQrcodeFileName);

        //设置 alipay 缴费订单号和二维码地址
        enterpriseRegiestDTO.setAlipayOrderId(outTradeNo);
        enterpriseRegiestDTO.setAlipayQrcodeUrl(alipayQrcodeUrl);

        //设置 wechat 缴费订单号和二维码地址
        WechatPayCreateOrderModel wechatPayCreateOrderModel = new WechatPayCreateOrderModel();
        wechatPayCreateOrderModel.setAppId(wechatAppId);
        wechatPayCreateOrderModel.setDescription(payRecordReasonEnum+ ":"+ tenant.getTenantName());
        wechatPayCreateOrderModel.setMchId(mchId);
        AmountModel amountModel = new AmountModel();
        amountModel.setTotal(amount.intValue());
        wechatPayCreateOrderModel.setAmount(amountModel);
        wechatPayCreateOrderModel.setOutTradeNo(outTradeNo);
        wechatPayCreateOrderModel.setNotifyUrl(wechatNotifyUrl);
        WechatPayCreateRespModel wechatPayCreateRespModel = wechatPayClient.preCreateOrder(wechatPayCreateOrderModel);
        String wechatpayQrcodeFileName = "wechatpay_" + outTradeNo + ".png";
        String wechatpayQrcodeUrl = commonService.generateQrImageAndUpLoadAliyun(wechatPayCreateRespModel.getCodeUrl(), wechatpayQrcodeFileName);

        enterpriseRegiestDTO.setWechatPayOrderId(outTradeNo);
        enterpriseRegiestDTO.setWechatPayQrcodeUrl(wechatpayQrcodeUrl);

        //生成预缴费记录
        tenantPayRecord = new TenantPayRecord();
//        tenantPayRecord.setPayChannel(PayChannelConstants.ALIPAY_CHANNEL);
        tenantPayRecord.setStatus(PayRecordStatusConstants.ALIPAY_WAIT_BUYER_PAY);
        tenantPayRecord.setTenantId(tenant.getId());
        tenantPayRecord.setOutTradeNo(outTradeNo);
        tenantPayRecord.setTotalAmount(amount.setScale(2, BigDecimal.ROUND_HALF_UP));
        tenantPayRecord.setAlipayQrcodeUrl(alipayQrcodeUrl);
        tenantPayRecord.setWechatPayQrcodeUrl(wechatpayQrcodeUrl);
        tenantPayRecord.setCreateUser(SystemConstants.SYSTEM);
        tenantPayRecord.setCreateTime(new Date());
        tenantPayRecordMapper.insert(tenantPayRecord);

        return GeneratePayRecordResponseVO.builder().orderId(outTradeNo).build();
    }
}
