package com.huajie.application.service;

import com.github.pagehelper.Page;
import com.huajie.application.api.common.ApiPage;
import com.huajie.application.api.common.exception.ApiException;
import com.huajie.application.api.request.AddInspectRequestVO;
import com.huajie.application.api.request.InspectQueryRequestVO;
import com.huajie.application.api.request.PatrolQueryRequestVO;
import com.huajie.application.api.request.SelfCheckQueryRequestVO;
import com.huajie.application.api.response.*;
import com.huajie.domain.common.constants.InspectTypeConstants;
import com.huajie.domain.common.constants.TenantTypeConstants;
import com.huajie.domain.common.enums.DeviceTypeEnum;
import com.huajie.domain.common.enums.ProblemStateEnum;
import com.huajie.domain.common.oauth2.model.CustomizeGrantedAuthority;
import com.huajie.domain.common.utils.PeriodUtil;
import com.huajie.domain.common.utils.UserContext;
import com.huajie.domain.entity.*;
import com.huajie.domain.model.PeriodDTO;
import com.huajie.domain.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.*;
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

    public InspectRecordResponseVO getInspectRecord(Integer pageNum, Integer pageSize,InspectQueryRequestVO requestVO){
        InspectRecordResponseVO responseVO = new InspectRecordResponseVO();
        Tenant enterprise = tenantService.getTenantByTenantId(requestVO.getEnterpriseId());
        if(enterprise == null || !TenantTypeConstants.ENTERPRISE.equals(enterprise.getTenantType())){
            throw new ApiException("企业不存在");
        }

        LocalDate startDate = requestVO.getStartDate();
        LocalDate endDate = requestVO.getEndDate();
        if(startDate == null || endDate == null){
            PeriodDTO periodDTO = PeriodUtil.getPeriodByEnterprise(enterprise.getEnterpriseType(), enterprise.getEntFireType());
            startDate = periodDTO.getStartDate();
            endDate = periodDTO.getEndDate();
        }

        List<InspectDetail> inspectList;
        Tenant currentTenant = UserContext.getCurrentTenant();
        if(TenantTypeConstants.GOVERNMENT.equals(currentTenant.getTenantType())){
            // 政府租户只能查询 当前政府及管辖下一级政府的检查记录
            Page<Tenant> adminGovernmentList = govermentOrganizationService.getAdminGovernmentList(1, 10000, null);
            List<Integer> adminGovernmentIds = adminGovernmentList.stream().map(Tenant::getId).distinct().collect(Collectors.toList());
            adminGovernmentIds.add(currentTenant.getId());
            inspectList = inspectDetailService.getGovernmentInspectList(enterprise.getId(), adminGovernmentIds, startDate, endDate);
        }else if(TenantTypeConstants.ENTERPRISE.equals(currentTenant.getTenantType())){
            // 企业用户只能查询 本企业的检查记录
            inspectList = inspectDetailService.getEnterpriseInspectList(enterprise.getId(), startDate,endDate);
        }else{
            throw new ApiException("当前租户无法访问此功能");
        }

        Page<Place> placePage = placeService.getPagePlaceList(pageNum, pageSize, requestVO.getPlaceId(), requestVO.getPlaceName(), null, enterprise.getId());
        responseVO.setPlaceCount(placePage.size());
        // 已检查点位数
        responseVO.setInspectPlaceCount(CollectionUtils.isEmpty(inspectList) ? 0 : inspectList.stream().map(InspectDetail::getPlaceId).distinct().count());

        Page<PlaceInspectRecordResponseVO> placeList = new Page<>();
        BeanUtils.copyProperties(placePage,placeList);

        // 点位对应设备列表Map
        List<Integer> placeIds = placePage.stream().map(Place::getId).collect(Collectors.toList());
        Map<Integer, List<Device>> placeDeviceMap = deviceService.getPlaceDeviceMap(placeIds);

        // 点位对应检查列表Map
        Map<Integer, List<InspectDetail>> placeInspcetMap = inspectList.stream().collect(Collectors.groupingBy(InspectDetail::getPlaceId));

        Map<Integer, Long> inspectProblemMap = problemDetailService.getInspectProblemMapByInspectIds(inspectList.stream().map(InspectDetail::getId).collect(Collectors.toList()));
        // 设置点位信息
        for(Place place: placePage){
            List<Device> devices = placeDeviceMap.get(place.getId());
            List<InspectDetail> placeInspectDetailList = placeInspcetMap.get(place.getId());

            PlaceInspectRecordResponseVO placeInspectRecord = new PlaceInspectRecordResponseVO();
            placeInspectRecord.setId(place.getId());
            placeInspectRecord.setPlaceName(place.getPlaceName());
            placeInspectRecord.setPlaceAddress(place.getPlaceAddress());
            placeInspectRecord.setInspectDeviceCount(CollectionUtils.isEmpty(placeInspectDetailList) ? 0 : placeInspectDetailList.stream().map(InspectDetail::getDeviceId).distinct().count());

            if(CollectionUtils.isEmpty(devices)){
                placeInspectRecord.setDeviceCount(0);
                placeList.add(placeInspectRecord);
                continue;
            }
            placeInspectRecord.setDeviceCount(devices.size());
            Map<Integer, List<InspectDetail>> deviceInspcetMap = CollectionUtils.isEmpty(placeInspectDetailList) ? new HashMap<>() :  placeInspectDetailList.stream().collect(Collectors.groupingBy(InspectDetail::getDeviceId));
            // 设置点位下设备信息
            List<DeviceInspectRecordResponseVO> deviceList = new ArrayList<>();
            for (Device device:devices){
                DeviceInspectRecordResponseVO deviceInspectRecord = new DeviceInspectRecordResponseVO();
                deviceInspectRecord.setId(device.getId());
                deviceInspectRecord.setDeviceType(device.getDeviceType());
                deviceInspectRecord.setDeviceTypeDesc(DeviceTypeEnum.valueOf(device.getDeviceType()).getName());
                deviceInspectRecord.setDeviceName(device.getDeviceName());
                deviceInspectRecord.setDeviceNo(device.getDeviceNo());
                deviceInspectRecord.setProductionDate(device.getProductionDate());

                List<InspectDetail> deviceInspcetDetailList = deviceInspcetMap.get(device.getId());
                if(!CollectionUtils.isEmpty(deviceInspcetDetailList)){
                    deviceInspectRecord.setInspectCount(deviceInspcetDetailList.size());

                    // 设置设备对应检查记录
                    List<InspectDetailResponseVO> inspectDetailResponseVOS = new ArrayList<>();
                    for (InspectDetail inspectDetail : deviceInspcetDetailList){
                        InspectDetailResponseVO inspectDetailResponseVO = new InspectDetailResponseVO();
                        BeanUtils.copyProperties(inspectDetail,inspectDetailResponseVO);
                        if("error".equals(inspectDetail.getInspectResult())){
                            inspectDetailResponseVO.setRelationProblemId(inspectProblemMap.get(inspectDetail.getId()));
                        }
                        inspectDetailResponseVOS.add(inspectDetailResponseVO);
                    }
                    deviceInspectRecord.setInspectDetailResponseVOS(inspectDetailResponseVOS);
                }

                deviceList.add(deviceInspectRecord);
            }
            placeInspectRecord.setDeviceList(deviceList);
            placeList.add(placeInspectRecord);
        }
        responseVO.setPlacePage(ApiPage.restPage(placeList));
        return responseVO;
    }

    public Page<InspectDetailResponseVO> getPageSelfCheckList(Integer pageNum, Integer pageSize, SelfCheckQueryRequestVO requestVO){

        Tenant currentTenant = UserContext.getCurrentTenant();
        if(currentTenant == null || !TenantTypeConstants.ENTERPRISE.equals(currentTenant.getTenantType())){
            throw new ApiException("企业不存在");
        }
        LocalDate startDate = requestVO.getStartDate();
        LocalDate endDate = requestVO.getEndDate();
        if(startDate == null || endDate == null){
            PeriodDTO periodDTO = PeriodUtil.getPeriodByEnterprise(currentTenant.getEnterpriseType(), currentTenant.getEntFireType());
            startDate = periodDTO.getStartDate();
            endDate = periodDTO.getEndDate();
        }

        Page<InspectDetail> inspectDetails = inspectDetailService.getPageSelfCheckList(pageNum, pageSize, startDate, endDate);

        Page<InspectDetailResponseVO> result = new Page<>();
        BeanUtils.copyProperties(inspectDetails,result);

        for (InspectDetail inspectDetail : inspectDetails){
            InspectDetailResponseVO inspectDetailResponseVO = new InspectDetailResponseVO();
            BeanUtils.copyProperties(inspectDetail,inspectDetailResponseVO);
            result.add(inspectDetailResponseVO);
        }
        return result;
    }

    public Page<InspectDetailResponseVO> getPagePatrolList(Integer pageNum, Integer pageSize, PatrolQueryRequestVO requestVO){
        Page<InspectDetail> inspectDetails = inspectDetailService.getPagePatrolList(pageNum, pageSize, requestVO.getPlaceId(), requestVO.getPlaceName(), requestVO.getDeviceId(), requestVO.getDeviceName(), requestVO.getStartDate(), requestVO.getEndDate());

        Page<InspectDetailResponseVO> result = new Page<>();
        BeanUtils.copyProperties(inspectDetails,result);
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

        LocalDate startDate = null;
        LocalDate endDate = null;
        if(InspectTypeConstants.PATROL.equals(inspectType)){
            // 巡查, 查询近15天检查记录
            startDate = PeriodUtil.getLast15Days();
        }else if(InspectTypeConstants.INSPECT.equals(inspectType)){
            // 检查，根据点位对应企业获取检查期段
            Tenant entTenant = tenantService.getTenantByTenantId(place.getTenantId());
            PeriodDTO periodDTO = PeriodUtil.getPeriodByEnterprise(entTenant.getEnterpriseType(), entTenant.getEntFireType());
            startDate = periodDTO.getStartDate();
            endDate = periodDTO.getEndDate();
        }

        List<InspectDetail> inspectDetailList = inspectDetailService.getInspectListByPlaceId(place.getId(),startDate,endDate,inspectType);

        List<DeviceResponseVO> deviceResponseVOS = new ArrayList<>(5);
        for (Device device: deviceList) {
            DeviceResponseVO deviceResponseVO = new DeviceResponseVO();
            BeanUtils.copyProperties(device,deviceResponseVO);
            deviceResponseVO.setDeviceTypeDesc(DeviceTypeEnum.valueOf(device.getDeviceType()).getName());

            // 设置检查记录
            List<InspectDetail> inspectDetails = inspectDetailList.stream().filter(item -> item.getDeviceId().equals(device.getId())).collect(Collectors.toList());
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
                inspectSaveInfoCheck(inspectRequestVO);break;
            case "selfCheck":
                selfCheckSaveInfoCheck(inspectRequestVO);break;
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

    private void inspectSaveInfoCheck(AddInspectRequestVO addInspectRequestVO){
        if(addInspectRequestVO.getDeviceId() == null){
            throw new ApiException("点位或设备不可为空");
        }

        Device device = deviceService.getDeviceById(addInspectRequestVO.getDeviceId());
        if(device == null){
            throw new ApiException("设备信息不存在");
        }
        Place place = placeService.getPlaceById(device.getPlaceId());
        if(place == null){
            throw new ApiException("点位信息不存在");
        }
        CustomizeGrantedAuthority authority = UserContext.getCustomizeGrantedAuthority();

        addInspectRequestVO.setPlaceId(place.getId());
        addInspectRequestVO.setPlaceName(place.getPlaceName());
        addInspectRequestVO.setPlaceAddress(place.getPlaceAddress());
        addInspectRequestVO.setDeviceName(device.getDeviceName());
        addInspectRequestVO.setEntTenantId(device.getTenantId());

        if(TenantTypeConstants.GOVERNMENT.equals(authority.getTenant().getTenantType())){
            // 政府检查
            addInspectRequestVO.setGovTenantId(authority.getTenant().getId());
        }else{
            // 企业检查
            if (!place.getTenantId().equals(authority.getTenant().getId())){
                // 点位租户与实际登录用户不符
                throw new ApiException("系统异常，请联系管理员");
            }
        }

        checkInspectResult(addInspectRequestVO);
    }

    private void selfCheckSaveInfoCheck(AddInspectRequestVO addInspectRequestVO){
        CustomizeGrantedAuthority authority = UserContext.getCustomizeGrantedAuthority();

        if(TenantTypeConstants.GOVERNMENT.equals(authority.getTenant().getTenantType())){
            throw new ApiException("政府租户无权限进行自查操作");
        }
        addInspectRequestVO.setEntTenantId(authority.getTenant().getId());

        checkInspectResult(addInspectRequestVO);
    }

    private InspectDetail createInspectDetail(AddInspectRequestVO inspectRequestVO){
        CustomizeGrantedAuthority authority = UserContext.getCustomizeGrantedAuthority();

        InspectDetail inspectDetail = new InspectDetail();
        BeanUtils.copyProperties(inspectRequestVO,inspectDetail);

        inspectDetail.setSubmitUserId(authority.getUserId());
        inspectDetail.setSubmitUserName(authority.getUserName());
        inspectDetail.setSubmitUserPhone(authority.getPhone());


        return inspectDetail;
    }

    private ProblemDetail createProblemDetail(AddInspectRequestVO inspectRequestVO){
        CustomizeGrantedAuthority authority = UserContext.getCustomizeGrantedAuthority();

        ProblemDetail problemDetail = new ProblemDetail();
        BeanUtils.copyProperties(inspectRequestVO,problemDetail);

        problemDetail.setEntTenantId(inspectRequestVO.getEntTenantId());
        problemDetail.setGovTenantId(inspectRequestVO.getGovTenantId());
        problemDetail.setState(ProblemStateEnum.SUBMIT.getStateCode());
        problemDetail.setProblemType(inspectRequestVO.getInspectType());

        problemDetail.setSubmitUserId(authority.getUserId());
        problemDetail.setSubmitUserName(authority.getUserName());
        problemDetail.setSubmitUserPhone(authority.getPhone());
        problemDetail.setSubmitTime(new Date());

        return problemDetail;
    }



}
