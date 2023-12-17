package com.huajie.application.service;

import com.huajie.application.api.response.statistic.DeviceStateCountResponseVO;
import com.huajie.application.api.response.statistic.EntCloudCheckResponseVO;
import com.huajie.application.api.response.statistic.EntIndexResponseVO;
import com.huajie.domain.common.oauth2.model.CustomizeGrantedAuthority;
import com.huajie.domain.common.utils.DateUtil;
import com.huajie.domain.common.utils.PeriodUtil;
import com.huajie.domain.common.utils.UserContext;
import com.huajie.domain.entity.InspectDetail;
import com.huajie.domain.entity.ProblemDetail;
import com.huajie.domain.entity.Tenant;
import com.huajie.domain.model.PeriodDTO;
import com.huajie.domain.service.DeviceService;
import com.huajie.domain.service.InspectDetailService;
import com.huajie.domain.service.PlaceService;
import com.huajie.domain.service.ProblemDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class EnterpriseIndexStatisticService {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private PlaceService placeService;

    @Autowired
    private ProblemDetailService problemDetailService;

    @Autowired
    private InspectDetailService inspectDetailService;

    public EntIndexResponseVO getEntBaseInfo(){
        EntIndexResponseVO responseVO = new EntIndexResponseVO();

        Tenant currentTenant = UserContext.getCurrentTenant();
        CustomizeGrantedAuthority currentUser = UserContext.getCustomizeGrantedAuthority();

        responseVO.setEnterpriseName(currentTenant.getTenantName());
        responseVO.setRoleName(currentUser.getRole().getRoleName());

        // 总点位数
        Integer placeCount = placeService.getPlaceCountByTenantId(currentTenant.getId());
        Integer newPlaceCount = placeService.getNewPlaceCountByTenantId(currentTenant.getId(), DateUtil.getCurrentDate());
        responseVO.setTotalPlaceCount(placeCount);
        responseVO.setNewPlaceCount(newPlaceCount);

        // 总设备数
        Integer deviceCount = deviceService.getDeviceCountByTenantId(currentTenant.getId());
        Integer newDeviceCount = deviceService.getNewDeviceCountByTenantId(currentTenant.getId(), DateUtil.getCurrentDate());
        responseVO.setTotalDeviceCount(deviceCount);
        responseVO.setNewDeviceCount(newDeviceCount);


        // 设备有效率
        Integer effectiveCount = deviceService.getEffectiveCountByTenantId(currentTenant.getId());
        BigDecimal effectiveRate = effectiveCount.equals(0) ? new BigDecimal("1") : new BigDecimal(String.valueOf(effectiveCount/deviceCount));
        responseVO.setDeviceEffectiveRate(effectiveRate);


        // 已检查设备数量
        PeriodDTO periodDTO = PeriodUtil.getPeriodByEnterprise(currentTenant.getEnterpriseType(), currentTenant.getEntFireType());
        LocalDate startDate = periodDTO.getStartDate();
        LocalDate endDate = periodDTO.getEndDate();
        List<InspectDetail> inspectList = inspectDetailService.getEnterpriseInspectList(currentTenant.getId(), startDate, endDate);
        int inspectPlaceCount = (int)inspectList.stream().map(InspectDetail::getPlaceId).distinct().count();
        responseVO.setInspectPlaceCount(inspectPlaceCount);
        Date today = DateUtil.getCurrentDate();
        int todayNewInspectDeviceCount = (int)inspectList.stream().filter(item -> item.getCreateTime().after(today)).map(InspectDetail::getPlaceId).distinct().count();
        responseVO.setTodayInspectPlaceCount(todayNewInspectDeviceCount);

        // 已解决隐患
        List<ProblemDetail> finishProblemList = problemDetailService.getFinishProblemByEnterpriseId(currentTenant.getId());
        int finishProblemCount = finishProblemList.size();
        responseVO.setFinishProblemCount(finishProblemCount);
        // 待整改隐患
        List<ProblemDetail> todoProblemList = problemDetailService.getUnFinishProblemByEnterpriseId(currentTenant.getId());
        int todoProblemCount = todoProblemList.size();
        int newTodoProblemCount = (int)todoProblemList.stream().filter(item -> item.getCreateTime().after(today)).count();
        responseVO.setTodoProblemCount(todoProblemCount);
        responseVO.setNewTodoProblemCount(newTodoProblemCount);
        // 超时隐患
        List<ProblemDetail> timeOutProblemList = problemDetailService.getTimeOutProblemByEnterpriseId(currentTenant.getId());
        int timeoutProblemCount = timeOutProblemList.size();
        int newTimeoutProblemCount = (int)timeOutProblemList.stream().filter(item -> item.getCreateTime().after(today)).count();
        responseVO.setTimeoutProblemCount(timeoutProblemCount);
        responseVO.setNewTimeoutProblemCount(newTimeoutProblemCount);

        // 安全等级
        responseVO.setSecurityLevel(currentTenant.getSecurityLevel());

        return responseVO;
    }

    public List<DeviceStateCountResponseVO> getDeviceStateCount(){
        Tenant currentTenant = UserContext.getCurrentTenant();
        return deviceService.getDeviceStateCount(currentTenant.getId());
    }

    public EntCloudCheckResponseVO getEntCloudCheck(){
        Tenant currentTenant = UserContext.getCurrentTenant();

        EntCloudCheckResponseVO cloudCheckResponseVO = new EntCloudCheckResponseVO();
        cloudCheckResponseVO.setSecurityLevel(currentTenant.getSecurityLevel());

        return cloudCheckResponseVO;
    }


}
