package com.huajie.application.service;

import com.huajie.application.api.response.statistic.*;
import com.huajie.domain.common.constants.EnterpriseApproveStateConstants;
import com.huajie.domain.common.constants.RoleCodeConstants;
import com.huajie.domain.common.enums.EnterpriseTypeEnum;
import com.huajie.domain.common.enums.SecurityLevelEnum;
import com.huajie.domain.common.utils.DateUtil;
import com.huajie.domain.common.utils.UserContext;
import com.huajie.domain.entity.*;
import com.huajie.domain.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class GovernmentIndexStatisticService {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private PlaceService placeService;

    @Autowired
    private ProblemDetailService problemDetailService;

    @Autowired
    private InspectDetailService inspectDetailService;

    @Autowired
    private GovermentOrganizationService govermentOrganizationService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    /**
     * 政府用户基本信息统计
     * @return
     */
    public GovIndexResponseVO getGovBaseInfo(){
        Tenant currentTenant = UserContext.getCurrentTenant();
        Date today = DateUtil.getCurrentDate();

        GovIndexResponseVO responseVO = new GovIndexResponseVO();

        Integer problemCount = problemDetailService.getProblemCountByGovernmentId(currentTenant.getId());
        Integer newProblemCount = problemDetailService.getNewProblemCountByGovernmentId(currentTenant.getId(), today);
        List<ProblemDetail> unFinishProblem = problemDetailService.getUnFinishProblemByGovernmentId(currentTenant.getId());
        int unFinishProblemCount = unFinishProblem.size();
        int newUnFinishProblemCount = (int)unFinishProblem.stream().filter(item -> item.getCreateTime().after(today)).count();
        BigDecimal reformRate = problemCount.equals(0) ? new BigDecimal("1") : new BigDecimal(String.valueOf(unFinishProblemCount/problemCount));

        // 管辖企业数量
        Integer adminEnterpriseCount = 0;
        List<Tenant> adminEnterpriseList = govermentOrganizationService.getAdminEnterpriseList(null);
        if(!CollectionUtils.isEmpty(adminEnterpriseList)){
            adminEnterpriseCount = adminEnterpriseList.size();
            BeanUtils.copyProperties(this.getEnterpriseTypeCountStatistic(adminEnterpriseList),responseVO);

            Calendar instance = Calendar.getInstance();
            instance.add(Calendar.DATE,-30);
            Date before30DaysDate = instance.getTime();

            int before30DaysNewCount = (int)adminEnterpriseList.stream().filter(item -> item.getCreateTime().after(before30DaysDate)).count();
            responseVO.setBefore30DaysNewEnterpriseCount(before30DaysNewCount);

        }
        // 待审核企业数量
        Integer adminUnApproveEnterpriseCount = govermentOrganizationService.getAdminEnterpriseCount(EnterpriseApproveStateConstants.UN_APPROVE);

        responseVO.setProblemCount(problemCount);
        responseVO.setTodayNewProblemCount(newProblemCount);
        responseVO.setUnfinishedProblemCount(unFinishProblemCount);
        responseVO.setTodayNewUnfinishedProblemCount(newUnFinishProblemCount);
        responseVO.setReformRate(reformRate.setScale(2,BigDecimal.ROUND_HALF_UP));
        responseVO.setAdminEnterpriseCount(adminEnterpriseCount);
        responseVO.setUnApproveEnterpriseCount(adminUnApproveEnterpriseCount);
        this.setGovUserInspectCountList(responseVO);

        return responseVO;
    }

    /**
     * 消防安全管理人检查统计数量
     * @param responseVO
     */
    private void setGovUserInspectCountList(GovIndexResponseVO responseVO){
        Tenant currentTenant = UserContext.getCurrentTenant();
        Date today = DateUtil.getCurrentDate();

        List<ProblemDetail> currentMonthProblems = problemDetailService.getCurrentMonthProblemsByGovernmentId(currentTenant.getId());

        List<InspectDetail> currentMonthInspects = inspectDetailService.getCurrentMonthInspectListByGovernment(currentTenant.getId());
        // 检查覆盖用户数
        int inspectEnterpriseCount = (int)currentMonthInspects.stream().map(InspectDetail::getEntTenantId).distinct().count();


        Map<Integer, List<InspectDetail>> userInspectMap = currentMonthInspects.stream().collect(Collectors.groupingBy(InspectDetail::getSubmitUserId));
        Map<Integer, List<ProblemDetail>> userProblemMap = currentMonthProblems.stream().collect(Collectors.groupingBy(ProblemDetail::getSubmitUserId));

        Role govOperatorCodeRole = roleService.getRoleByCode(RoleCodeConstants.GOV_OPERATOR_CODE);
        List<User> userList = userService.getUsersByTenantIdAndRoleId(currentTenant.getId(), govOperatorCodeRole.getId());
        List<GovUserInspectCountResponseVO> govUserInspectCountList = userList.stream().map(item -> {
            GovUserInspectCountResponseVO userInspectCount = new GovUserInspectCountResponseVO();
            userInspectCount.setUserId(item.getId());
            userInspectCount.setUserName(item.getUserName());
            userInspectCount.setHeadPic(item.getHeadPic());
            List<InspectDetail> inspectDetailList = userInspectMap.get(item.getId());
            if (!CollectionUtils.isEmpty(inspectDetailList)) {
                // 检查覆盖用户数
                int userInspectEntCount = (int) inspectDetailList.stream().map(InspectDetail::getEntTenantId).distinct().count();
                userInspectCount.setInspectEnterpriseCount(userInspectEntCount);
            }
            List<ProblemDetail> problemDetailList = userProblemMap.get(item.getId());
            if (!CollectionUtils.isEmpty(problemDetailList)) {
                // 发现问题数
                userInspectCount.setSubmitProblemCount(problemDetailList.size());
                userInspectCount.setTodaySubmitProblemCount((int) problemDetailList.stream().filter(problem -> problem.getSubmitTime().after(today)).count());
            }
            return userInspectCount;
        }).collect(Collectors.toList());

        responseVO.setInspectEnterpriseCountInCurrentMonth(inspectEnterpriseCount);
        responseVO.setGovUserInspectCountList(govUserInspectCountList);

    }

    /**
     * 新增企业集合信息
     */
    public GovIndexEntCountByTypeResponseVO getNewEnterpriseListInLastWeek(){
        List<Tenant> lastWeekNewAdminEnterprise = govermentOrganizationService.getLastWeekNewAdminEnterprise();
        return this.getEnterpriseTypeCountStatistic(lastWeekNewAdminEnterprise);
    }

    /**
     * 新增企业集合信息
     */
    public List<StatisticResponseVO> getNewDeviceCount(){
        List<StatisticResponseVO> responseVOS = new ArrayList<>();

        List<Tenant> adminEnterpriseList = govermentOrganizationService.getAdminEnterpriseList(null);
        if(CollectionUtils.isEmpty(adminEnterpriseList)){
            return responseVOS;
        }

        List<Integer> adminEnterpriseIds = adminEnterpriseList.stream().map(Tenant::getId).collect(Collectors.toList());
        List<Device> deviceList = deviceService.getDeviceListByEnterpriseIds(adminEnterpriseIds);
        if(CollectionUtils.isEmpty(deviceList)){
            return responseVOS;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,-7);
        Date lastWeek = calendar.getTime();
        calendar.add(Calendar.DATE, -7);
        Date lastTwoWeek = calendar.getTime();
        calendar.add(Calendar.DATE, -7);
        Date lastThreeWeek = calendar.getTime();

        AtomicInteger lastWeekCount = new AtomicInteger();
        AtomicInteger lastTwoWeekCount = new AtomicInteger();
        AtomicInteger lastThreeWeekCount = new AtomicInteger();

        deviceList.stream().forEach(device -> {
            if(device.getCreateTime().before(lastWeek)){
                lastWeekCount.addAndGet(1);
                if(device.getCreateTime().before(lastTwoWeek)){
                    lastTwoWeekCount.addAndGet(1);
                    if(device.getCreateTime().before(lastThreeWeek)){
                        lastThreeWeekCount.addAndGet(1);
                    }
                }
            }
        });

        responseVOS.add(new StatisticResponseVO("1","第一周",lastThreeWeekCount.get()));
        responseVOS.add(new StatisticResponseVO("2","第二周",lastTwoWeekCount.get()));
        responseVOS.add(new StatisticResponseVO("3","第三周",lastWeekCount.get()));
        responseVOS.add(new StatisticResponseVO("4","第四周",deviceList.size()));

        return responseVOS;
    }

    /**
     * 根据企业消防安全等级统计企业数量
     * @param city
     * @param region
     * @param street
     * @return
     */
    public GovIndexEntCountBySecurityLevelResponseVO getEnterpriseCountBySecurityLevel(Integer city, Integer region, Integer street){
        GovIndexEntCountBySecurityLevelResponseVO responseVO = new GovIndexEntCountBySecurityLevelResponseVO();
        List<Tenant> adminEnterpriseList = govermentOrganizationService.getAdminEnterpriseList(null);
        if(CollectionUtils.isEmpty(adminEnterpriseList)){
            return responseVO;
        }

        Predicate<Tenant> predicate = tenant -> true;
        if (street != null){
            predicate = tenant -> street.equals(tenant.getStreet());
        }else if(region != null){
            predicate = tenant -> region.equals(tenant.getRegion());
        }else if(city != null){
            predicate = tenant -> city.equals(tenant.getCity());
        }

        Map<String, List<Tenant>> securityLevelMap = adminEnterpriseList.stream().filter(predicate).collect(Collectors.groupingBy(Tenant::getSecurityLevel));

        responseVO.setLowLevelEnterpriseStatistic(this.getEnterpriseTypeCountStatistic(securityLevelMap.get(SecurityLevelEnum.LOW.getCode())));
        responseVO.setMiddleLevelEnterpriseStatistic(this.getEnterpriseTypeCountStatistic(securityLevelMap.get(SecurityLevelEnum.MIDDLE.getCode())));
        responseVO.setHighLevelEnterpriseStatistic(this.getEnterpriseTypeCountStatistic(securityLevelMap.get(SecurityLevelEnum.HIGH.getCode())));

        return responseVO;
    }

    /**
     * 获取企业类型数量统计数据
     * @param enterpriseList
     * @return
     */
    private GovIndexEntCountByTypeResponseVO getEnterpriseTypeCountStatistic(List<Tenant> enterpriseList){
        GovIndexEntCountByTypeResponseVO responseVO = new GovIndexEntCountByTypeResponseVO();
        if(CollectionUtils.isEmpty(enterpriseList)){
            return responseVO;
        }

        Map<String, List<Tenant>> typeEnterpriseMap = enterpriseList.stream().collect(Collectors.groupingBy(Tenant::getEnterpriseType));
        responseVO.setTotalCount(enterpriseList.size());
        List<Tenant> companys = typeEnterpriseMap.get(EnterpriseTypeEnum.Company.getCode());
        responseVO.setCompanyCount(CollectionUtils.isEmpty(companys) ? 0 : companys.size());
        List<Tenant> enterprise = typeEnterpriseMap.get(EnterpriseTypeEnum.Enterprise.getCode());
        responseVO.setEnterpriseCount(CollectionUtils.isEmpty(enterprise)? 0 : enterprise.size());
        List<Tenant> merchants = typeEnterpriseMap.get(EnterpriseTypeEnum.Merchant.getCode());
        responseVO.setMerchantCount(CollectionUtils.isEmpty(merchants)? 0 :merchants.size());
        List<Tenant> rentalHouses = typeEnterpriseMap.get(EnterpriseTypeEnum.RentalHouse.getCode());
        responseVO.setRentalHouseCount(CollectionUtils.isEmpty(rentalHouses) ? 0 : rentalHouses.size());
        return responseVO;
    }





}
