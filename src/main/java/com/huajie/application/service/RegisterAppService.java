package com.huajie.application.service;

import com.huajie.application.api.common.exception.ApiException;
import com.huajie.application.api.request.EnterpriseRegiestRequestVO;
import com.huajie.application.api.request.GovermentRegiestRequestVO;
import com.huajie.application.api.response.EnterpriseRegiestResponseVO;
import com.huajie.domain.common.constants.TenantStatusConstants;
import com.huajie.domain.common.constants.TenantTypeConstants;
import com.huajie.domain.entity.Tenant;
import com.huajie.domain.model.EnterpriseRegiestDTO;
import com.huajie.domain.service.RegisterService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zhuxiaofeng
 * @date 2023/8/27
 */
@Service
public class RegisterAppService {

    @Autowired
    private RegisterService registerService;


    public EnterpriseRegiestResponseVO regiestEnterprise(EnterpriseRegiestRequestVO regiestRequestVO) {
        Tenant tenant = new Tenant();
        tenant.setTenantName(regiestRequestVO.getEnterpriseName());
        tenant.setTenantType(TenantTypeConstants.ENTERPRISE);
        tenant.setStatus(TenantStatusConstants.ENABLE);
        //todo 可用截至时间未确定
        tenant.setEffectiveEndDate(new Date());
        tenant.setProvince(regiestRequestVO.getProvinceId());
        tenant.setCity(regiestRequestVO.getCityId());
        tenant.setRegion(regiestRequestVO.getRegionId());
        tenant.setStreet(regiestRequestVO.getStreetId());
        tenant.setAddress(regiestRequestVO.getAddress());
        tenant.setEnterpriseType(regiestRequestVO.getEnterpriseType());
        tenant.setEntIndustryClassification(regiestRequestVO.getEntIndustryClassification());
        tenant.setEntFireType(regiestRequestVO.getEntFireType());

        EnterpriseRegiestDTO regiestDTO = registerService.regiestEnterprise(tenant, regiestRequestVO.getEntAdminList(), regiestRequestVO.getEntOperatorList());


        EnterpriseRegiestResponseVO enterpriseRegiestResponseVO = new EnterpriseRegiestResponseVO();
        BeanUtils.copyProperties(regiestDTO, enterpriseRegiestResponseVO);
        enterpriseRegiestResponseVO.setAmount(regiestDTO.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
        return enterpriseRegiestResponseVO;
    }

    public void regiestGoverment(GovermentRegiestRequestVO regiestRequestVO) {
        Tenant tenant = new Tenant();
        tenant.setTenantName(regiestRequestVO.getGovernmentName());
        tenant.setTenantType(TenantTypeConstants.ENTERPRISE);
        tenant.setStatus(TenantStatusConstants.ENABLE);
        //todo 可用截至时间未确定
        tenant.setEffectiveEndDate(new Date());
        tenant.setProvince(regiestRequestVO.getProvinceId());
        tenant.setCity(regiestRequestVO.getCityId());
        tenant.setRegion(regiestRequestVO.getRegionId());
        tenant.setStreet(regiestRequestVO.getStreetId());
        tenant.setAddress(regiestRequestVO.getAddress());
        tenant.setGovernmentType(regiestRequestVO.getGovernmentType());
        if (StringUtils.equals(regiestRequestVO.getGovernmentType(), "IndustrySector")){
            if (StringUtils.isNotBlank(regiestRequestVO.getGovIndustrySector())) {
                tenant.setGovIndustrySector(regiestRequestVO.getGovIndustrySector());
            }else {
                throw new ApiException("政府行业部门 不能为空");
            }
        }
        tenant.setEntFireType(regiestRequestVO.getEntFireType());
//        tenant.setEntIndustryClassification(JSONObject.toJSONString(regiestRequestVO.getEntIndustryClassification()));
        tenant.setEntFireType(regiestRequestVO.getEntFireType());
        this.registerService.regiestGoverment(tenant, regiestRequestVO.getGovAdminList(),
                regiestRequestVO.getGovOperatorList(), regiestRequestVO.getEntIndustryClassification());
    }
}
