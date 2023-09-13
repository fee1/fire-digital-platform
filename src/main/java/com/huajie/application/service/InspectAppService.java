package com.huajie.application.service;

import com.github.pagehelper.Page;
import com.huajie.application.api.common.exception.ApiException;
import com.huajie.application.api.request.AddInspectRequestVO;
import com.huajie.application.api.request.PatrolQueryRequestVO;
import com.huajie.application.api.request.SelfCheckQueryRequestVO;
import com.huajie.application.api.response.DeviceResponseVO;
import com.huajie.application.api.response.InspectDetailResponseVO;
import com.huajie.application.api.response.PlaceResponseVO;
import com.huajie.domain.common.constants.InspectTypeConstants;
import com.huajie.domain.common.constants.TenantTypeConstants;
import com.huajie.domain.common.enums.DeviceTypeEnum;
import com.huajie.domain.common.enums.ExtinguisherTypeEnum;
import com.huajie.domain.common.enums.PowerTypeEnum;
import com.huajie.domain.common.oauth2.model.CustomizeGrantedAuthority;
import com.huajie.domain.common.utils.PeriodUtil;
import com.huajie.domain.common.utils.UserContext;
import com.huajie.domain.entity.*;
import com.huajie.domain.model.PeriodDTO;
import com.huajie.domain.service.*;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InspectAppService {

    @Autowired
    private InspectDetailService inspectDetailService;

    @Autowired
    private ProblemDetailService problemDetailService;

    @Autowired
    private PlaceService placeService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private TenantService tenantService;

    @Autowired
    private GovermentOrganizationService govermentOrganizationService;

    public Page<InspectDetailResponseVO> getPageSelfCheckList(Integer pageNum, Integer pageSize, SelfCheckQueryRequestVO requestVO){
        Page<InspectDetail> inspectDetails = inspectDetailService.getPageSelfCheckList(pageNum, pageSize, requestVO.getStartTime(), requestVO.getEndTime());
        Page<InspectDetailResponseVO> result = new Page<>();
        for (InspectDetail inspectDetail : inspectDetails){
            InspectDetailResponseVO inspectDetailResponseVO = new InspectDetailResponseVO();
            BeanUtils.copyProperties(inspectDetail,inspectDetailResponseVO);
            result.add(inspectDetailResponseVO);
        }
        return result;
    }


    public Page<InspectDetailResponseVO> getPagePatrolList(Integer pageNum, Integer pageSize, PatrolQueryRequestVO requestVO){
        Page<InspectDetail> inspectDetails = inspectDetailService.getPagePatrolList(pageNum, pageSize, requestVO.getPlaceId(), requestVO.getPlaceName(), requestVO.getDeviceId(), requestVO.getDeviceName(), requestVO.getStartTime(), requestVO.getEndTime());
        Page<InspectDetailResponseVO> result = new Page<>();
        for (InspectDetail inspectDetail : inspectDetails){
            InspectDetailResponseVO inspectDetailResponseVO = new InspectDetailResponseVO();
            BeanUtils.copyProperties(inspectDetail,inspectDetailResponseVO);
            result.add(inspectDetailResponseVO);
        }
        return result;
    }

    public List<DeviceResponseVO>  getDeviceListWithInspectByNFC(String nfcCode,String inspectType){
        Place place = placeService.getPlaceByNFCCode(nfcCode);
        if(place == null){
            throw new ApiException("点位信息不存在");
        }
        List<Device> deviceList = deviceService.getDeviceListByPlaceId(place.getId());

        LocalDateTime startDate = null;
        LocalDateTime endDate = null;
        if(InspectTypeConstants.PATROL.equals(inspectType)){
            // 巡查, 查询近15天检查记录
            startDate = PeriodUtil.getLast15Days();
        }else if(InspectTypeConstants.INSPECT.equals(inspectType)){
            // 检查，根据点位对应企业获取检查期段
            Tenant entTenant = tenantService.getTenantByTenantId(place.getTenantId());
            PeriodDTO periodDTO = PeriodUtil.getPeriodByEnterprise(entTenant.getEnterpriseType(), entTenant.getEntFireType());
            startDate = periodDTO.getStartDateTime();
            endDate = periodDTO.getEndDateTime();
        }

        List<InspectDetail> inspectDetailList = inspectDetailService.getInspectListByPlaceId(place.getId(),startDate,endDate,inspectType);

        List<DeviceResponseVO> deviceResponseVOS = new ArrayList<>(5);
        for (Device device: deviceList) {
            DeviceResponseVO deviceResponseVO = new DeviceResponseVO();
            BeanUtils.copyProperties(device,deviceResponseVO);
            deviceResponseVO.setDeviceTypeDesc(DeviceTypeEnum.valueOf(device.getDeviceType()).getName());

            // 设置检查记录
            List<InspectDetail> inspectDetails = inspectDetailList.stream().filter(item -> device.getId() == item.getDeviceId()).collect(Collectors.toList());
            List<InspectDetailResponseVO>  inspectDetailResponseVOS = new ArrayList<>();
            for (InspectDetail inspectDetail : inspectDetails){
                InspectDetailResponseVO inspectDetailResponseVO = new InspectDetailResponseVO();
                BeanUtils.copyProperties(inspectDetail,inspectDetailResponseVO);
                inspectDetailResponseVOS.add(inspectDetailResponseVO);
            }
            deviceResponseVO.setInspectDetailList(inspectDetailResponseVOS);
            deviceResponseVOS.add(deviceResponseVO);
        }
        return deviceResponseVOS;
    }


    @Transactional
    public void save(AddInspectRequestVO inspectRequestVO){
        String inspectType = inspectRequestVO.getInspectType();
        switch (inspectType) {
            case "patrol":
            case "inspect":
                checkInspectResult(inspectRequestVO);
                checkPlaceAndDevice(inspectRequestVO);break;
            case "selfCheck":checkInspectResult(inspectRequestVO);break;
        }

        InspectDetail inspectDetail = createInspectDetail(inspectRequestVO);
        inspectDetailService.insert(inspectDetail);
        if("error".equals(inspectRequestVO.getInspectResult())){
            ProblemDetail problemDetail = createProblemDetail(inspectRequestVO);
            problemDetail.setRelationId(inspectDetail.getId());
            problemDetailService.insert(problemDetail);
        }
    }

    public List<DeviceInspectInfo> getDeviceInspectInfos(String deviceType){
        return inspectDetailService.getDeviceInspectInfos(deviceType);
    }

    private void checkInspectResult(AddInspectRequestVO inspectRequestVO){
        if("error".equals(inspectRequestVO.getInspectResult())){
            // 检查不通过
            if(StringUtils.isEmpty(inspectRequestVO.getProblemDesc())){
                throw new ApiException("问题描述不可为空");
            }
        }
    }

    private void checkPlaceAndDevice(AddInspectRequestVO addInspectRequestVO){
        if(addInspectRequestVO.getDeviceId() == null || addInspectRequestVO.getPlaceId() == null){
            throw new ApiException("点位或设备不可为空");
        }
        Place place = placeService.getPlaceById(addInspectRequestVO.getPlaceId());
        if(place == null){
            throw new ApiException("点位信息不存在");
        }
        Device device = deviceService.getDeviceById(addInspectRequestVO.getDeviceId());
        if(device == null){
            throw new ApiException("设备信息不存在");
        }

        addInspectRequestVO.setPlaceName(place.getPlaceName());
        addInspectRequestVO.setPlaceAddress(place.getPlaceAddress());
        addInspectRequestVO.setDeviceName(device.getDeviceName());
        addInspectRequestVO.setEntTenantId(device.getTenantId());
    }

    private InspectDetail createInspectDetail(AddInspectRequestVO inspectRequestVO){
        CustomizeGrantedAuthority authority = UserContext.getCustomizeGrantedAuthority();

        InspectDetail inspectDetail = new InspectDetail();
        BeanUtils.copyProperties(inspectRequestVO,inspectDetail);

        inspectDetail.setSubmitUserId(authority.getUserId());
        inspectDetail.setSubmitUserName(authority.getUserName());
        inspectDetail.setSubmitUserPhone(authority.getPhone());

        if(TenantTypeConstants.GOVERMENT.equals(authority.getTenant().getTenantType())){
            inspectDetail.setGovTenantId(authority.getTenant().getId());
        }

        return inspectDetail;
    }

    private ProblemDetail createProblemDetail(AddInspectRequestVO inspectRequestVO){
        CustomizeGrantedAuthority authority = UserContext.getCustomizeGrantedAuthority();

        ProblemDetail problemDetail = new ProblemDetail();
        BeanUtils.copyProperties(inspectRequestVO,problemDetail);

        problemDetail.setTenantId(inspectRequestVO.getEntTenantId());
        problemDetail.setProblemSource(authority.getTenant().getTenantType());
        problemDetail.setState("submit");
        problemDetail.setPorblemType(inspectRequestVO.getInspectType());

        problemDetail.setSubmitUserId(authority.getUserId());
        problemDetail.setSubmitUserName(authority.getUserName());
        problemDetail.setSubmitUserPhone(authority.getPhone());

        return problemDetail;
    }



}
