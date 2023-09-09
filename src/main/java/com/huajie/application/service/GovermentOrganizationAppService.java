package com.huajie.application.service;

import com.huajie.application.api.request.AddGovermentAdminRequestVO;
import com.huajie.application.api.request.AddGovermentOperatorRequestVO;
import com.huajie.application.api.request.EditEnterpriseRequestVO;
import com.huajie.application.api.request.EditGovermentInfoRequestVO;
import com.huajie.application.api.request.UserAddRequestVO;
import com.huajie.application.api.response.EnterpriseResponseVO;
import com.huajie.domain.common.oauth2.model.TenantModel;
import com.huajie.domain.common.utils.UserContext;
import com.huajie.domain.entity.Region;
import com.huajie.domain.entity.Tenant;
import com.huajie.domain.entity.User;
import com.huajie.domain.service.GovermentOrganizationService;
import com.huajie.domain.service.RegionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhuxiaofeng
 * @date 2023/9/3
 */
@Service
public class GovermentOrganizationAppService {

    @Autowired
    private GovermentOrganizationService govermentOrganizationService;

    @Autowired
    private RegionService regionService;

    public void editGovermentInfo(EditGovermentInfoRequestVO requestVO) {
        Tenant tenant = new Tenant();
        BeanUtils.copyProperties(requestVO, tenant);
        if (StringUtils.isNotBlank(requestVO.getGovernmentName())){
            tenant.setTenantName(requestVO.getGovernmentName());
        }
        TenantModel currentTenant = UserContext.getCurrentTenant();
        tenant.setId(currentTenant.getId());
        govermentOrganizationService.editGovermentInfo(tenant, requestVO.getEntIndustryClassification());
    }

    public void addAdminUser(AddGovermentAdminRequestVO requestVO) {
        List<UserAddRequestVO> govAdminList = requestVO.getGovAdminList();
        for (UserAddRequestVO userAddRequestVO : govAdminList) {
            User user =new User();
            BeanUtils.copyProperties(userAddRequestVO, user);
            govermentOrganizationService.addAdminUser(user);

        }
    }

    public void addOperatorUser(AddGovermentOperatorRequestVO requestVO) {
        List<UserAddRequestVO> govOperatorList = requestVO.getGovOperatorList();
        for (UserAddRequestVO userAddRequestVO : govOperatorList) {
            User user =new User();
            BeanUtils.copyProperties(userAddRequestVO, user);
            govermentOrganizationService.addOperatorUser(user);
        }
    }

    public List<EnterpriseResponseVO> getEnterpriseVerifyList(String enterpriseName) {
        List<Tenant> tenants = govermentOrganizationService.getEnterpriseVerifyList(enterpriseName);
        List<EnterpriseResponseVO> enterpriseResponseVOList = new ArrayList<>();

        List<Integer> regionIds = new ArrayList<>();
        for (Tenant tenant : tenants) {
            regionIds.add(tenant.getProvince());
            regionIds.add(tenant.getCity());
            regionIds.add(tenant.getRegion());
            regionIds.add(tenant.getStreet());
        }

        List<Region> regionByIds = regionService.getRegionByIds(regionIds);
        Map<Integer, List<Region>> id2Region = regionByIds.stream().collect(Collectors.groupingBy(Region::getId));

        for (Tenant tenant : tenants) {
            EnterpriseResponseVO enterpriseResponseVO = new EnterpriseResponseVO();
            BeanUtils.copyProperties(tenant, enterpriseResponseVO);
            enterpriseResponseVO.setEnterpriseName(tenant.getTenantName());

            List<Region> regionList = id2Region.get(tenant.getProvince());
            enterpriseResponseVO.setProvinceId(tenant.getProvince());
            if (!CollectionUtils.isEmpty(regionList)) {
                enterpriseResponseVO.setProvinceName(regionList.get(0).getRegionName());
            }

            regionList = id2Region.get(tenant.getCity());
            enterpriseResponseVO.setCityId(tenant.getCity());
            if (!CollectionUtils.isEmpty(regionList)) {
                enterpriseResponseVO.setCityName(regionList.get(0).getRegionName());
            }

            regionList = id2Region.get(tenant.getRegion());
            enterpriseResponseVO.setRegionId(tenant.getRegion());
            if (!CollectionUtils.isEmpty(regionList)) {
                enterpriseResponseVO.setRegionName(regionList.get(0).getRegionName());
            }

            regionList = id2Region.get(tenant.getStreet());
            enterpriseResponseVO.setStreetId(tenant.getStreet());
            if (!CollectionUtils.isEmpty(regionList)) {
                enterpriseResponseVO.setStreetName(regionList.get(0).getRegionName());
            }
            enterpriseResponseVOList.add(enterpriseResponseVO);
        }
        return enterpriseResponseVOList;
    }

    public void enterpriseVerifyPass(Integer enterpriseId) {
        govermentOrganizationService.enterpriseVerifyPass(enterpriseId);
    }

    public void editEnterprise(EditEnterpriseRequestVO requestVO) {
        govermentOrganizationService.editEnterprise(requestVO);
    }

    public List<EnterpriseResponseVO> getEnterpriseList(String enterpriseName) {
        List<Tenant> tenants = govermentOrganizationService.getEnterpriseList(enterpriseName);
        List<EnterpriseResponseVO> enterpriseResponseVOList = new ArrayList<>();

        List<Integer> regionIds = new ArrayList<>();
        for (Tenant tenant : tenants) {
            regionIds.add(tenant.getProvince());
            regionIds.add(tenant.getCity());
            regionIds.add(tenant.getRegion());
            regionIds.add(tenant.getStreet());
        }

        List<Region> regionByIds = regionService.getRegionByIds(regionIds);
        Map<Integer, List<Region>> id2Region = regionByIds.stream().collect(Collectors.groupingBy(Region::getId));

        for (Tenant tenant : tenants) {
            EnterpriseResponseVO enterpriseResponseVO = new EnterpriseResponseVO();
            BeanUtils.copyProperties(tenant, enterpriseResponseVO);
            enterpriseResponseVO.setEnterpriseName(tenant.getTenantName());

            List<Region> regionList = id2Region.get(tenant.getProvince());
            enterpriseResponseVO.setProvinceId(tenant.getProvince());
            if (!CollectionUtils.isEmpty(regionList)) {
                enterpriseResponseVO.setProvinceName(regionList.get(0).getRegionName());
            }

            regionList = id2Region.get(tenant.getCity());
            enterpriseResponseVO.setCityId(tenant.getCity());
            if (!CollectionUtils.isEmpty(regionList)) {
                enterpriseResponseVO.setCityName(regionList.get(0).getRegionName());
            }

            regionList = id2Region.get(tenant.getRegion());
            enterpriseResponseVO.setRegionId(tenant.getRegion());
            if (!CollectionUtils.isEmpty(regionList)) {
                enterpriseResponseVO.setRegionName(regionList.get(0).getRegionName());
            }

            regionList = id2Region.get(tenant.getStreet());
            enterpriseResponseVO.setStreetId(tenant.getStreet());
            if (!CollectionUtils.isEmpty(regionList)) {
                enterpriseResponseVO.setStreetName(regionList.get(0).getRegionName());
            }
            enterpriseResponseVOList.add(enterpriseResponseVO);
        }
        return enterpriseResponseVOList;
    }
}
