package com.huajie.application.service;

import com.huajie.application.api.common.exception.ApiException;
import com.huajie.application.api.request.EnterpriseRegiestRequestVO;
import com.huajie.application.api.request.GovermentRegiestRequestVO;
import com.huajie.application.api.response.EnterpriseRegiestResponseVO;
import com.huajie.domain.common.constants.TenantStatusConstants;
import com.huajie.domain.common.constants.TenantTypeConstants;
import com.huajie.domain.common.enums.GovernmentTypeEnum;
import com.huajie.domain.common.utils.LocalDateUtil;
import com.huajie.domain.entity.Tenant;
import com.huajie.domain.model.EnterpriseRegiestDTO;
import com.huajie.domain.service.RegisterService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
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
        tenant.setEffectiveEndDate(LocalDateUtil.addYears(new Date(), 1));
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
        tenant.setTenantType(TenantTypeConstants.GOVERMENT);
        tenant.setStatus(TenantStatusConstants.ENABLE);
        tenant.setEffectiveEndDate(LocalDateUtil.addYears(new Date(), 50));
        tenant.setProvince(regiestRequestVO.getProvinceId());
        tenant.setCity(regiestRequestVO.getCityId());
        tenant.setRegion(regiestRequestVO.getRegionId());
        tenant.setStreet(regiestRequestVO.getStreetId());
        tenant.setAddress(regiestRequestVO.getAddress());
        tenant.setGovernmentType(regiestRequestVO.getGovernmentType());
        if (StringUtils.equals(regiestRequestVO.getGovernmentType(), GovernmentTypeEnum.IndustrySector.getCode())){
            if (StringUtils.isNotBlank(regiestRequestVO.getGovIndustrySector())) {
                tenant.setGovIndustrySector(regiestRequestVO.getGovIndustrySector());
            }else {
                throw new ApiException("政府行业部门 不能为空");
            }
        }

        if (!GovernmentTypeEnum.SubLeader.getCode().equals(regiestRequestVO.getGovernmentType())
                && !GovernmentTypeEnum.FireDepartment.getCode().equals(regiestRequestVO.getGovernmentType())
                && CollectionUtils.isEmpty(regiestRequestVO.getEntIndustryClassification())){
            throw new ApiException("当前政府管理类型 管理行业不可为空");
        }

//        tenant.setEntIndustryClassification(JSONObject.toJSONString(regiestRequestVO.getEntIndustryClassification()));
        this.registerService.regiestGoverment(tenant, regiestRequestVO.getGovAdminList(),
                regiestRequestVO.getGovOperatorList(), regiestRequestVO.getEntIndustryClassification());
    }
}
