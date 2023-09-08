package com.huajie.domain.service;

import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.huajie.application.api.request.UserAddRequestVO;
import com.huajie.domain.common.constants.PayRecordStatusConstants;
import com.huajie.domain.common.constants.RoleCodeConstants;
import com.huajie.domain.common.constants.SystemConstants;
import com.huajie.domain.common.constants.TenantStatusConstants;
import com.huajie.domain.common.exception.ServerException;
import com.huajie.domain.entity.GovIndustryMap;
import com.huajie.domain.entity.Role;
import com.huajie.domain.entity.Tenant;
import com.huajie.domain.entity.TenantPayRecord;
import com.huajie.domain.entity.User;
import com.huajie.domain.model.EnterpriseRegiestDTO;
import com.huajie.infrastructure.external.pay.CustomAlipayClient;
import com.huajie.infrastructure.external.pay.WechatPayClient;
import com.huajie.infrastructure.external.pay.model.AmountModel;
import com.huajie.infrastructure.external.pay.model.WechatPayCreateOrderModel;
import com.huajie.infrastructure.external.pay.model.WechatPayCreateRespModel;
import com.huajie.infrastructure.mapper.GovIndustryMapMapper;
import com.huajie.infrastructure.mapper.TenantPayRecordMapper;
import com.zxf.method.trace.util.TraceFatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhuxiaofeng
 * @date 2023/8/27
 */
@Service
@Slf4j
public class RegisterService {

    @Autowired
    private UserService userService;

    @Autowired
    private TenantService tenantService;

    @Autowired
    private GovIndustryMapMapper govIndustryMapMapper;

    @Autowired
    private RoleService roleService;

    @Autowired
    private CustomAlipayClient customAlipayClient;

    @Autowired
    private WechatPayClient wechatPayClient;

    @Autowired
    private CommonService commonService;

    @Autowired
    private TenantPayRecordMapper tenantPayRecordMapper;

    @Value("${unit.price:5}")
    private Integer unitPrice;


    @Value("${wechat.pay.notify-url}")
    private String wechatNotifyUrl;


    /**
     * 企业注册
     * @param tenant 企业租户
     * @param entAdminList 企业消防安全责任人
     * @param entOperatorList 企业消防安全管理人
     */
    @Transactional
    public EnterpriseRegiestDTO regiestEnterprise(Tenant tenant, List<UserAddRequestVO> entAdminList, List<UserAddRequestVO> entOperatorList) {
        Role entAdminCodeRole = roleService.getRoleByCode(RoleCodeConstants.ENT_ADMIN_CODE);
        if (entAdminCodeRole == null){
            throw new ServerException("企业消防安全责任人 权限不存在");
        }
        Role entOperatorCodeRole = roleService.getRoleByCode(RoleCodeConstants.ENT_OPERATOR_CODE);
        if (entOperatorCodeRole == null){
            throw new ServerException("企业消防安全管理人 权限不存在");
        }

        //租户信息保存
        tenant.setStatus(TenantStatusConstants.DISABLE);
        tenantService.add(tenant);
        List<User> userList = new ArrayList<>();
        for (UserAddRequestVO userAddRequestVO : entAdminList) {
            User user = new User();
            BeanUtils.copyProperties(userAddRequestVO, user);
            user.setRoleId(entAdminCodeRole.getId());
            user.setTenantId(tenant.getId());
            user.setCreateUser(SystemConstants.SYSTEM);
            userList.add(user);
        }
        for (UserAddRequestVO userAddRequestVO : entOperatorList) {
            User user = new User();
            BeanUtils.copyProperties(userAddRequestVO, user);
            user.setRoleId(entOperatorCodeRole.getId());
            user.setTenantId(tenant.getId());
            user.setCreateUser(SystemConstants.SYSTEM);
            userList.add(user);
        }
        //用户信息保存
        userService.addUsers(userList);

        //支付宝预下单，生成付款二维码
        BigDecimal amount = new BigDecimal(unitPrice * userList.size());

        EnterpriseRegiestDTO enterpriseRegiestDTO = new EnterpriseRegiestDTO();
        enterpriseRegiestDTO.setAmount(amount.setScale(2, BigDecimal.ROUND_HALF_UP));

        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        String outTradeNo = TraceFatch.getTraceId();
        model.setOutTradeNo(outTradeNo);
        model.setTotalAmount(amount.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
        model.setSubject("企业用户注册: " + tenant.getTenantName());
        AlipayTradePrecreateResponse response = customAlipayClient.preCreateOrder(model);

        String alipayQrcodeFileName = "alipay_" + outTradeNo+".png";
        String alipayQrcodeUrl = commonService.generateQrImageAndUpLoadAliyun(response.getQrCode(), alipayQrcodeFileName);

        //设置 alipay 缴费订单号和二维码地址
        enterpriseRegiestDTO.setAlipayOrderId(outTradeNo);
        enterpriseRegiestDTO.setAlipayQrcodeUrl(alipayQrcodeUrl);

        //设置 wechat 缴费订单号和二维码地址
        WechatPayCreateOrderModel wechatPayCreateOrderModel = new WechatPayCreateOrderModel();
        wechatPayCreateOrderModel.setAppId("");
        wechatPayCreateOrderModel.setDescription("测试使用");
        wechatPayCreateOrderModel.setMchId("1651245161");
        AmountModel amountModel = new AmountModel();
        amountModel.setTotal(1);
        wechatPayCreateOrderModel.setAmount(amountModel);
        wechatPayCreateOrderModel.setOutTradeNo(outTradeNo);
        wechatPayCreateOrderModel.setNotifyUrl(wechatNotifyUrl);
        WechatPayCreateRespModel wechatPayCreateRespModel = wechatPayClient.preCreateOrder(wechatPayCreateOrderModel);
        String wechatpayQrcodeFileName = "wechatpay_" + outTradeNo + ".png";
        String wechatpayQrcodeUrl = commonService.generateQrImageAndUpLoadAliyun(wechatPayCreateRespModel.getCode_url(), wechatpayQrcodeFileName);

        enterpriseRegiestDTO.setWechatPayOrderId(outTradeNo);
        enterpriseRegiestDTO.setWechatPayQrcodeUrl(wechatpayQrcodeUrl);

        //生成预缴费记录
        TenantPayRecord tenantPayRecord = new TenantPayRecord();
//        tenantPayRecord.setPayChannel(PayChannelConstants.ALIPAY_CHANNEL);
        tenantPayRecord.setStatus(PayRecordStatusConstants.ALIPAY_WAIT_BUYER_PAY);
        tenantPayRecord.setTenantId(tenant.getId());
        tenantPayRecord.setOutTradeNo(outTradeNo);
        tenantPayRecord.setTotalAmount(amount.setScale(2, BigDecimal.ROUND_HALF_UP));
        tenantPayRecord.setCreateUser(SystemConstants.SYSTEM);
        tenantPayRecordMapper.insert(tenantPayRecord);


        return enterpriseRegiestDTO;
    }

    /**
     * 政府注册
     *
     * @param tenant 政府租户
     * @param govAdminList 负责人
     * @param govOperatorList 管理人
     * @param entIndustryClassification 政府管理的行业
     */
    public void regiestGoverment(Tenant tenant, List<UserAddRequestVO> govAdminList,
                                 List<UserAddRequestVO> govOperatorList, List<String> entIndustryClassification) {
        Role govAdminCodeRole = roleService.getRoleByCode(RoleCodeConstants.GOV_ADMIN_CODE);
        if (govAdminCodeRole == null){
            throw new ServerException("政府消防安全责任人 权限不存在");
        }
        Role govOperatorCodeRole = roleService.getRoleByCode(RoleCodeConstants.GOV_OPERATOR_CODE);
        if (govOperatorCodeRole == null){
            throw new ServerException("政府消防安全管理人 权限不存在");
        }

        List<User> userList = new ArrayList<>();
        for (UserAddRequestVO userAddRequestVO : govAdminList) {
            User user = new User();
            BeanUtils.copyProperties(userAddRequestVO, user);
            user.setRoleId(govAdminCodeRole.getId());
            userList.add(user);
        }
        for (UserAddRequestVO userAddRequestVO : govOperatorList) {
            User user = new User();
            BeanUtils.copyProperties(userAddRequestVO, user);
            user.setRoleId(govOperatorCodeRole.getId());
            userList.add(user);
        }
        tenantService.add(tenant);
        userService.addUsers(userList);
        if (!CollectionUtils.isEmpty(entIndustryClassification)){
            for (String code : entIndustryClassification) {
                GovIndustryMap govIndustryMap = new GovIndustryMap();
                govIndustryMap.setTenantId(tenant.getId());
                govIndustryMap.setIndustryClassification(code);
                govIndustryMapMapper.insert(govIndustryMap);
            }
        }
    }

}
