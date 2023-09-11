package com.huajie.application.service;

import com.github.pagehelper.Page;
import com.huajie.application.api.request.AddGovermentAdminRequestVO;
import com.huajie.application.api.request.AddGovermentOperatorRequestVO;
import com.huajie.application.api.request.EditEnterpriseRequestVO;
import com.huajie.application.api.request.EditGovermentInfoRequestVO;
import com.huajie.application.api.request.UserAddRequestVO;
import com.huajie.application.api.response.DicResponseVO;
import com.huajie.application.api.response.DicValueResponseVO;
import com.huajie.application.api.response.EntIndustryClassificationResponseVO;
import com.huajie.application.api.response.EnterpriseResponseVO;
import com.huajie.application.api.response.GovermentInfoResponseVO;
import com.huajie.application.api.response.TenantResponseVO;
import com.huajie.application.api.response.UserDetailResponseVO;
import com.huajie.domain.common.constants.RoleCodeConstants;
import com.huajie.domain.common.oauth2.model.TenantModel;
import com.huajie.domain.common.utils.UserContext;
import com.huajie.domain.entity.GovIndustryMap;
import com.huajie.domain.entity.Region;
import com.huajie.domain.entity.Role;
import com.huajie.domain.entity.SysDicValue;
import com.huajie.domain.entity.Tenant;
import com.huajie.domain.entity.User;
import com.huajie.domain.service.GovIndustryMapService;
import com.huajie.domain.service.GovermentOrganizationService;
import com.huajie.domain.service.RegionService;
import com.huajie.domain.service.RoleService;
import com.huajie.domain.service.SysDicService;
import com.huajie.domain.service.TenantService;
import com.huajie.domain.service.UserService;
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

    @Autowired
    private TenantService tenantService;

    @Autowired
    private GovIndustryMapService govIndustryMapService;

    @Autowired
    private SysDicService sysDicService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    public void editGovermentInfo(EditGovermentInfoRequestVO requestVO) {
        Tenant tenant = new Tenant();
        BeanUtils.copyProperties(requestVO, tenant);
        tenant.setProvince(requestVO.getProvinceId());
        tenant.setCity(requestVO.getCityId());
        tenant.setRegion(requestVO.getRegionId());
        tenant.setStreet(requestVO.getStreetId());
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

    public Page<EnterpriseResponseVO> getEnterpriseVerifyList(Integer pageNum, Integer pageSize, String enterpriseName) {
        Page<Tenant> tenants = govermentOrganizationService.getEnterpriseVerifyList(pageNum, pageSize, enterpriseName);
        if (CollectionUtils.isEmpty(tenants)){
            return new Page<>();
        }

        Page<EnterpriseResponseVO> enterpriseResponseVOList = new Page<>();
        BeanUtils.copyProperties(tenants, enterpriseResponseVOList);

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

            SysDicValue enterpriseType = this.sysDicService.getDicValueByValueCode(tenant.getEnterpriseType());
            enterpriseResponseVO.setEnterpriseTypeName(enterpriseType.getValueName());

            SysDicValue entIndustryClassification = this.sysDicService.getDicValueByValueCode(tenant.getEntIndustryClassification());
            enterpriseResponseVO.setEntIndustryClassificationName(entIndustryClassification.getValueName());

            SysDicValue entFireType= this.sysDicService.getDicValueByValueCode(tenant.getEntFireType());
            enterpriseResponseVO.setEntFireTypeName(entFireType.getValueName());

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

    public Page<EnterpriseResponseVO> getEnterpriseList(Integer pageNum, Integer pageSize, String enterpriseName) {
        Page<Tenant> tenants = govermentOrganizationService.getEnterpriseList(pageNum, pageSize, enterpriseName);
        if (CollectionUtils.isEmpty(tenants)){
            return new Page<>();
        }
        Page<EnterpriseResponseVO> enterpriseResponseVOList = new Page<>();
        BeanUtils.copyProperties(tenants, enterpriseResponseVOList);

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

            SysDicValue enterpriseType = this.sysDicService.getDicValueByValueCode(tenant.getEnterpriseType());
            enterpriseResponseVO.setEnterpriseTypeName(enterpriseType.getValueName());

            SysDicValue entIndustryClassification = this.sysDicService.getDicValueByValueCode(tenant.getEntIndustryClassification());
            enterpriseResponseVO.setEntIndustryClassificationName(entIndustryClassification.getValueName());

            SysDicValue entFireType= this.sysDicService.getDicValueByValueCode(tenant.getEntFireType());
            enterpriseResponseVO.setEntFireTypeName(entFireType.getValueName());

            enterpriseResponseVOList.add(enterpriseResponseVO);
        }
        return enterpriseResponseVOList;
    }

    public GovermentInfoResponseVO getInfo() {
        TenantModel currentTenant = UserContext.getCurrentTenant();

        Tenant tenantByTenantId = tenantService.getTenantByTenantId(currentTenant.getId());
        GovermentInfoResponseVO govermentInfoResponseVO = new GovermentInfoResponseVO();

        govermentInfoResponseVO.setGovernmentName(tenantByTenantId.getTenantName());
        govermentInfoResponseVO.setProvinceId(tenantByTenantId.getProvince());
        govermentInfoResponseVO.setCityId(tenantByTenantId.getCity());
        govermentInfoResponseVO.setRegionId(tenantByTenantId.getRegion());
        govermentInfoResponseVO.setStreetId(tenantByTenantId.getStreet());
        govermentInfoResponseVO.setAddress(tenantByTenantId.getAddress());
        govermentInfoResponseVO.setGovernmentType(tenantByTenantId.getGovernmentType());
        govermentInfoResponseVO.setGovIndustrySector(tenantByTenantId.getGovIndustrySector());
        govermentInfoResponseVO.setEntFireType(tenantByTenantId.getEntFireType());

        List<GovIndustryMap> govIndustryMapByTenantId = govIndustryMapService.getGovIndustryMapByTenantId(currentTenant.getId());
        List<String> valueCodes = govIndustryMapByTenantId.stream().map(GovIndustryMap::getIndustryClassification).collect(Collectors.toList());
//        List<SysDicValue> dicValueList = sysDicService.getDicValueList(valueCodes);

//        List<DicValueResponseVO> entIndustryClassification = new ArrayList<>();
//        for (SysDicValue sysDicValue : dicValueList) {
//            DicValueResponseVO dicValueResponseVO = new DicValueResponseVO();
//            BeanUtils.copyProperties(sysDicValue, dicValueResponseVO);
//            entIndustryClassification.add(dicValueResponseVO);
//        }
        govermentInfoResponseVO.setEntIndustryClassification(valueCodes);

        Role govAdminCodeRole = roleService.getRoleByCode(RoleCodeConstants.GOV_ADMIN_CODE);
        Role govOperatorCodeRole = roleService.getRoleByCode(RoleCodeConstants.GOV_OPERATOR_CODE);
        List<User> govAdminList = userService.getUsersByTenantIdAndRoleId(currentTenant.getId(), govAdminCodeRole.getId());
        List<User> govOperatorList = userService.getUsersByTenantIdAndRoleId(currentTenant.getId(), govOperatorCodeRole.getId());

        List<UserDetailResponseVO> govAdminListResponse = new ArrayList<>();
        List<UserDetailResponseVO> govOperatorListResponse = new ArrayList<>();

        for (User user : govAdminList) {
            UserDetailResponseVO userDetailResponseVO = new UserDetailResponseVO();
            BeanUtils.copyProperties(user, userDetailResponseVO);
            govAdminListResponse.add(userDetailResponseVO);
        }

        for (User user : govOperatorList) {
            UserDetailResponseVO userDetailResponseVO = new UserDetailResponseVO();
            BeanUtils.copyProperties(user, userDetailResponseVO);
            govOperatorListResponse.add(userDetailResponseVO);
        }

        govermentInfoResponseVO.setGovAdminList(govAdminListResponse);
        govermentInfoResponseVO.setGovOperatorList(govOperatorListResponse);
        return govermentInfoResponseVO;
    }
}
