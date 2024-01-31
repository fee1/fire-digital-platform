package com.huajie.domain.service;

import com.alibaba.fastjson.JSONObject;
import com.huajie.application.api.common.exception.ApiException;
import com.huajie.application.api.request.UserAddRequestVO;
import com.huajie.application.api.response.GeneratePayRecordResponseVO;
import com.huajie.domain.common.constants.CacheKeyPrefixConstants;
import com.huajie.domain.common.constants.RoleCodeConstants;
import com.huajie.domain.common.constants.SystemConstants;
import com.huajie.domain.common.constants.TenantStatusConstants;
import com.huajie.domain.common.enums.PayRecordReasonEnum;
import com.huajie.domain.common.exception.ServerException;
import com.huajie.domain.common.utils.GuavaUtil;
import com.huajie.domain.entity.GovIndustryMap;
import com.huajie.domain.entity.Role;
import com.huajie.domain.entity.Tenant;
import com.huajie.domain.entity.User;
import com.huajie.domain.model.EnterpriseRegiestDTO;
import com.huajie.domain.model.VerifyCodeCacheModel;
import com.huajie.infrastructure.external.pay.CustomAlipayClient;
import com.huajie.infrastructure.external.pay.WechatPayClient;
import com.huajie.infrastructure.mapper.GovIndustryMapMapper;
import com.huajie.infrastructure.mapper.TenantPayRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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

    @Value("${wechat.pay.appId:}")
    private String wechatAppId;

    @Value("${wechat.pay.mchId:}")
    private String mchId;

//    @Value("${unit.price:5}")
//    private Double unitPrice;

    @Autowired
    private Environment environment;

    @Autowired
    private PayService payService;

    /**
     * 企业注册
     * @param tenant 企业租户
     * @param entAdminList 企业消防安全责任人
     * @param entOperatorList 企业消防安全管理人
     */
    @Transactional(rollbackFor = Exception.class)
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
        if (!CollectionUtils.isEmpty(entAdminList)) {
            for (UserAddRequestVO userAddRequestVO : entAdminList) {
                User user = new User();
                BeanUtils.copyProperties(userAddRequestVO, user);
                user.setRoleId(entAdminCodeRole.getId());
                user.setTenantId(tenant.getId());
                user.setCreateUser(SystemConstants.SYSTEM);
                userList.add(user);
            }
        }else {
            throw new ApiException("企业消防安全责任人 必须有一个以上");
        }
        if (!CollectionUtils.isEmpty(entOperatorList)) {
            for (UserAddRequestVO userAddRequestVO : entOperatorList) {
                User user = new User();
                BeanUtils.copyProperties(userAddRequestVO, user);
                user.setRoleId(entOperatorCodeRole.getId());
                user.setTenantId(tenant.getId());
                user.setCreateUser(SystemConstants.SYSTEM);
                userList.add(user);
            }
        }else {
            throw new ApiException("企业消防安全管理人 必须有一个以上");
        }
        //用户信息保存
        // todo 校验验证码是否已经验证过了
//        this.checkUserPhoneIsVerify(userList);

        userService.addUsers(userList);

        GeneratePayRecordResponseVO responseVO = payService.generatePayRecord(PayRecordReasonEnum.ENTERPRISE_USER_REGIST, tenant);
        return EnterpriseRegiestDTO.builder().alipayOrderId(responseVO.getOrderId()).build();
    }

    private void checkUserPhoneIsVerify(List<User> userList){
        for (User user : userList) {
            VerifyCodeCacheModel verifyCodeCacheModel = GuavaUtil.get(CacheKeyPrefixConstants.REGISTER_VERIFICATION + user.getPhone());
            if (verifyCodeCacheModel == null){
                throw new ServerException(user.getPhone() + ":该手机号验证已过期，请重新验证");
            }
            if (!verifyCodeCacheModel.getVerifyStatus()){
                throw new ServerException(user.getPhone() + ":该手机号未验证，请重新输入正确的验证码进行验证");
            }
        }
    }

    /**
     * 政府注册
     *
     * @param tenant 政府租户
     * @param govAdminList 负责人
     * @param govOperatorList 管理人
     * @param entIndustryClassification 政府管理的行业
     */
    @Transactional
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
        tenantService.add(tenant);

        List<User> userList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(govAdminList)) {
            for (UserAddRequestVO userAddRequestVO : govAdminList) {
                User user = new User();
                BeanUtils.copyProperties(userAddRequestVO, user);
                user.setRoleId(govAdminCodeRole.getId());
                user.setTenantId(tenant.getId());
                user.setCreateUser(SystemConstants.SYSTEM);
                userList.add(user);
            }
        }else {
            throw new ApiException("政府消防安全责任人 必须有一个以上");
        }
        if (!CollectionUtils.isEmpty(govOperatorList)) {
            for (UserAddRequestVO userAddRequestVO : govOperatorList) {
                User user = new User();
                BeanUtils.copyProperties(userAddRequestVO, user);
                user.setRoleId(govOperatorCodeRole.getId());
                user.setTenantId(tenant.getId());
                user.setCreateUser(SystemConstants.SYSTEM);
                userList.add(user);
            }
        }else {
            throw new ApiException("政府消防安全管理人 必须有一个以上");
        }
        // todo 校验验证码是否已经验证过了
//        this.checkUserPhoneIsVerify(userList);

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

    public void sendVerificationCode(String phone) {
        //生成六位数字验证码
        String checkCode = RandomStringUtils.randomNumeric(6);
        VerifyCodeCacheModel verifyCodeCacheModel = new VerifyCodeCacheModel();
        verifyCodeCacheModel.setCode(checkCode);
        verifyCodeCacheModel.setVerifyStatus(false);
        GuavaUtil.set(CacheKeyPrefixConstants.REGISTER_VERIFICATION + phone, verifyCodeCacheModel, 600);

        JSONObject param = new JSONObject();
        param.put("code", checkCode);
        this.commonService.sendSms(phone, param);
    }

    public void sendVerificationCodeTest(String phone){
        //生成六位数字验证码
        String checkCode = "000000";
        VerifyCodeCacheModel verifyCodeCacheModel = new VerifyCodeCacheModel();
        verifyCodeCacheModel.setCode(checkCode);
        verifyCodeCacheModel.setVerifyStatus(false);
        GuavaUtil.set(CacheKeyPrefixConstants.REGISTER_VERIFICATION + phone, verifyCodeCacheModel, 600);

    }

    public void verify(String phone, String code) {
        VerifyCodeCacheModel verifyCodeCacheModel = GuavaUtil.get(CacheKeyPrefixConstants.REGISTER_VERIFICATION + phone);
        if (verifyCodeCacheModel == null){
            throw new ServerException("验证码已经过期");
        }
        if (StringUtils.equals(code, verifyCodeCacheModel.getCode())){
            verifyCodeCacheModel.setVerifyStatus(true);
        }else {
            throw new ServerException("验证码不正确");
        }
    }
}
