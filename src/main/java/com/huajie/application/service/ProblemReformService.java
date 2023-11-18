package com.huajie.application.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.huajie.application.api.common.exception.ApiException;
import com.huajie.application.api.request.ProblemQueryRequestVO;
import com.huajie.application.api.request.ProblemReformActionVO;
import com.huajie.application.api.response.ProblemDetailResponseVO;
import com.huajie.application.api.response.ProblemReformHistoryResponseVO;
import com.huajie.domain.common.constants.RoleCodeConstants;
import com.huajie.domain.common.constants.TenantTypeConstants;
import com.huajie.domain.common.enums.ProblemActionTypeEnum;
import com.huajie.domain.common.enums.ProblemStateEnum;
import com.huajie.domain.common.oauth2.model.CustomizeGrantedAuthority;
import com.huajie.domain.common.utils.UserContext;
import com.huajie.domain.entity.ProblemDetail;
import com.huajie.domain.entity.ProblemReformHistory;
import com.huajie.domain.entity.Tenant;
import com.huajie.domain.service.ProblemDetailService;
import com.huajie.domain.service.ProblemReformHistoryService;
import com.huajie.domain.service.TenantService;
import com.huajie.domain.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProblemReformService {

    @Autowired
    private ProblemDetailService problemDetailService;

    @Autowired
    private ProblemReformHistoryService problemReformHistoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private TenantService tenantService;

    public Page<ProblemDetailResponseVO> pageEnterpriseProblemList(ProblemQueryRequestVO requestVO,Integer pageNum,Integer pageSize){
        Tenant currentTenant = UserContext.getCurrentTenant();

        QueryWrapper<ProblemDetail> queryWrapper = new QueryWrapper<>();
        if("enterprise".equals(currentTenant.getTenantType())){
            queryWrapper.lambda().eq(ProblemDetail::getEntTenantId,requestVO.getEntTenantId() == null ? currentTenant.getId() : requestVO.getEntTenantId());
        }else{
            if(requestVO.getEntTenantId() == null) {
                throw new ApiException("系统异常，企业id不可为空！");
            }
            // 政府查询时，仅可查看企业自查的隐患
            queryWrapper.lambda().isNull(ProblemDetail::getGovTenantId);
        }

        if(StringUtils.isNotBlank(requestVO.getState())){
            queryWrapper.lambda().eq(ProblemDetail::getState,requestVO.getState());
        }
        if(StringUtils.isNotBlank(requestVO.getProblemType())){
            queryWrapper.lambda().eq(ProblemDetail::getProblemType,requestVO.getProblemType());
        }
        if(requestVO.getPlaceId() != null){
            queryWrapper.lambda().eq(ProblemDetail::getPlaceId,requestVO.getPlaceId());
        }
        if(StringUtils.isNotBlank(requestVO.getPlaceName())){
            queryWrapper.lambda().like(ProblemDetail::getPlaceName,requestVO.getPlaceName());
        }
        if(requestVO.getDeviceId() != null){
            queryWrapper.lambda().eq(ProblemDetail::getDeviceId,requestVO.getDeviceId());
        }
        if(StringUtils.isNotBlank(requestVO.getDeviceName())){
            queryWrapper.lambda().like(ProblemDetail::getDeviceName,requestVO.getDeviceName());
        }
        queryWrapper.lambda().orderByAsc(ProblemDetail::getReformTimeoutTime).orderByAsc(ProblemDetail::getSubmitTime);

        Page<ProblemDetail> problemList = problemDetailService.getProblemList(queryWrapper, pageNum, pageSize);

        Map<Integer, String> userHeadPicMap = userService.getUserHeadPicMap(problemList.stream().map(ProblemDetail::getSubmitUserId).distinct().collect(Collectors.toList()));


        Page<ProblemDetailResponseVO> result = new Page<>();
        BeanUtils.copyProperties(problemList,result);
        if(!CollectionUtils.isEmpty(problemList)){
            for (ProblemDetail problemDetail: problemList){
                ProblemDetailResponseVO problemDetailResponseVO = new ProblemDetailResponseVO();
                BeanUtils.copyProperties(problemDetail,problemDetailResponseVO);
                problemDetailResponseVO.setSubmitUserHeadPic(userHeadPicMap.get(problemDetail.getSubmitUserId()));
                problemDetailResponseVO.setStateName(ProblemStateEnum.valueOf(problemDetail.getState()).getStateName());
                result.add(problemDetailResponseVO);
            }
        }
        return result;
    }

    public Page<ProblemDetailResponseVO> pageGovInspectProblemList(ProblemQueryRequestVO requestVO,Integer pageNum,Integer pageSize){
        Tenant currentTenant = UserContext.getCurrentTenant();

        QueryWrapper<ProblemDetail> queryWrapper = new QueryWrapper<>();
        if("enterprise".equals(currentTenant.getTenantType())){
            throw new ApiException("当前用户无法访问该功能");
        }else{
            queryWrapper.lambda().eq(ProblemDetail::getGovTenantId,currentTenant.getId());
        }
        if(requestVO.getEntTenantId() != null){
            queryWrapper.lambda().eq(ProblemDetail::getEntTenantId,requestVO.getEntTenantId());
        }
        if(StringUtils.isNotBlank(requestVO.getState())){
            queryWrapper.lambda().eq(ProblemDetail::getState,requestVO.getState());
        }
        if(StringUtils.isNotBlank(requestVO.getProblemType())){
            queryWrapper.lambda().eq(ProblemDetail::getProblemType,requestVO.getProblemType());
        }
        if(requestVO.getPlaceId() != null){
            queryWrapper.lambda().eq(ProblemDetail::getPlaceId,requestVO.getPlaceId());
        }
        if(StringUtils.isNotBlank(requestVO.getPlaceName())){
            queryWrapper.lambda().like(ProblemDetail::getPlaceName,requestVO.getPlaceName());
        }
        if(requestVO.getDeviceId() != null){
            queryWrapper.lambda().eq(ProblemDetail::getDeviceId,requestVO.getDeviceId());
        }
        if(StringUtils.isNotBlank(requestVO.getDeviceName())){
            queryWrapper.lambda().like(ProblemDetail::getDeviceName,requestVO.getDeviceName());
        }
        queryWrapper.lambda().orderByAsc(ProblemDetail::getReformTimeoutTime).orderByAsc(ProblemDetail::getSubmitTime);
        Page<ProblemDetail> problemList = problemDetailService.getProblemList(queryWrapper, pageNum, pageSize);
        Map<Integer, String> userHeadPicMap = userService.getUserHeadPicMap(problemList.stream().map(ProblemDetail::getSubmitUserId).distinct().collect(Collectors.toList()));
        Page<ProblemDetailResponseVO> result = new Page<>();
        if(!CollectionUtils.isEmpty(problemList)){
            Map<Integer, String> tenantNameMap = tenantService.getTenantNameMap(problemList.stream().map(ProblemDetail::getEntTenantId).collect(Collectors.toList()));
            for (ProblemDetail problemDetail: problemList){
                ProblemDetailResponseVO problemDetailResponseVO = new ProblemDetailResponseVO();
                BeanUtils.copyProperties(problemDetail,result);
                problemDetailResponseVO.setStateName(ProblemStateEnum.valueOf(problemDetail.getState()).getStateName());
                problemDetailResponseVO.setEntTenantName(tenantNameMap.get(problemDetail.getGovTenantId()));
                problemDetailResponseVO.setSubmitUserHeadPic(userHeadPicMap.get(problemDetail.getSubmitUserId()));
                result.add(problemDetailResponseVO);
            }
        }
        return result;
    }


    public ProblemDetailResponseVO getProblemDetailById(Long problemId){
        ProblemDetailResponseVO result = new ProblemDetailResponseVO();
        ProblemDetail problemDetail = problemDetailService.getById(problemId);
        BeanUtils.copyProperties(problemDetail,result);
        result.setStateName(ProblemStateEnum.valueOf(problemDetail.getState()).getStateName());

        ProblemReformHistory lastGovernmentReform = problemReformHistoryService.getLastGovernmentReform(problemId);
        if(lastGovernmentReform != null){
            ProblemReformHistoryResponseVO lastGovernmentReformVO  = new ProblemReformHistoryResponseVO();
            BeanUtils.copyProperties(lastGovernmentReform, lastGovernmentReformVO);
            lastGovernmentReformVO.setActionName(ProblemActionTypeEnum.valueOf(lastGovernmentReformVO.getActionType()).getActionName());
            result.setGovernmentReformReply(lastGovernmentReformVO);
        }

        ProblemReformHistory lastEnterpriseReform = problemReformHistoryService.getLastEnterpriseReform(problemId);
        if(lastEnterpriseReform != null){
            ProblemReformHistoryResponseVO lastEnterpriseReformVO  = new ProblemReformHistoryResponseVO();
            BeanUtils.copyProperties(lastEnterpriseReform, lastEnterpriseReformVO);
            lastEnterpriseReformVO.setActionName(ProblemActionTypeEnum.valueOf(lastEnterpriseReformVO.getActionType()).getActionName());
            result.setEnterpriseReformReply(lastEnterpriseReformVO);
        }

        List<ProblemReformHistory> reformHistories = problemReformHistoryService.getReformHistories(problemId);
        List<ProblemReformHistoryResponseVO> reformHistoryVOS  = new ArrayList<>();
        if(!CollectionUtils.isEmpty(reformHistories)){
            Map<Integer, String> userHeadPicMap = userService.getUserHeadPicMap(reformHistories.stream().map(ProblemReformHistory::getSubmitUserId).distinct().collect(Collectors.toList()));
            for (ProblemReformHistory history : reformHistories){
                ProblemReformHistoryResponseVO item  = new ProblemReformHistoryResponseVO();
                BeanUtils.copyProperties(history,item);
                item.setActionName(ProblemActionTypeEnum.valueOf(history.getActionType()).getActionName());
                item.setSubmitUserHeadPic(userHeadPicMap.get(item.getSubmitUserId()));
                reformHistoryVOS.add(item);
            }
            result.getGovernmentReformReply().setSubmitUserHeadPic(userHeadPicMap.get(result.getGovernmentReformReply().getSubmitUserId()));
            result.getEnterpriseReformReply().setSubmitUserHeadPic(userHeadPicMap.get(result.getEnterpriseReformReply().getSubmitUserId()));
        }
        result.setReformHistory(reformHistoryVOS);

        return result;
    }



    @Transactional
    public void doAction(ProblemActionTypeEnum action, ProblemReformActionVO actionVO){
        ProblemDetail problemDetail = problemDetailService.getById(actionVO.getProblemId());
        if(problemDetail == null){
            throw new ApiException("隐患不存在");
        }

        ProblemStateEnum currentState = ProblemStateEnum.valueOf(problemDetail.getState());
        if(!action.getFromStates().contains(currentState)){
            throw new ApiException("当前状态不允许进行该动作");
        }

        CustomizeGrantedAuthority authority = UserContext.getCustomizeGrantedAuthority();

        ProblemReformHistory problemReformHistory = new ProblemReformHistory();
        BeanUtils.copyProperties(actionVO,problemReformHistory);
        problemReformHistory.setActionType(action.getActionType());
        problemReformHistory.setOldState(currentState.getStateCode());
        problemReformHistory.setNewState(action.getToStates().getStateCode());
        problemReformHistory.setSource(authority.getTenant().getTenantType());
        if(StringUtils.equals(RoleCodeConstants.GOV_ADMIN_CODE,authority.getRole().getRoleCode())){
            problemReformHistory.setSourceTenant(authority.getTenant().getTenantName()+" "+"政府消防安全责任人");
        }else if(StringUtils.equals(RoleCodeConstants.GOV_OPERATOR_CODE,authority.getRole().getRoleCode())){
            problemReformHistory.setSourceTenant(authority.getTenant().getTenantName()+" "+"政府消防安全管理人");
        }else if(StringUtils.equals(RoleCodeConstants.ENT_ADMIN_CODE,authority.getRole().getRoleCode())){
            problemReformHistory.setSourceTenant(authority.getTenant().getTenantName()+" "+"企业消防安全责任人");
        }else if(StringUtils.equals(RoleCodeConstants.ENT_OPERATOR_CODE,authority.getRole().getRoleCode())){
            problemReformHistory.setSourceTenant(authority.getTenant().getTenantName()+" "+"企业消防安全管理人");
        }

        problemReformHistory.setSubmitUserId(authority.getUserId());
        problemReformHistory.setSubmitUserName(authority.getUserName());
        problemReformHistory.setSubmitUserPhone(authority.getPhone());
        problemReformHistory.setSubmitTime(new Date());
        if(problemReformHistoryService.insert(problemReformHistory) < 1){
            throw new ApiException("系统繁忙，请稍后重试");
        }

        if(!currentState.equals(action.getToStates())){
            // 状态不一致，需要更新隐患状态
            Calendar calendar = Calendar.getInstance();
            switch (action){
                case SIGN:;
                case REPLY:;
                case REFORM_APPROVE_REJECT:;
                case DELAY_APPROVE_REJECT:
                    calendar.add(Calendar.DATE,3);
                    problemDetail.setReformTimeoutTime(calendar.getTime());
                    break;
                case DELAY_APPROVE_PASS:
                    calendar.add(Calendar.DATE,30);
                    problemDetail.setReformTimeoutTime(calendar.getTime());
                    break;
                case URGE:
                    // TODO 发送通知
                    break;
            }
            problemDetail.setState(action.getToStates().getStateCode());
            if(problemDetailService.updateById(problemDetail) < 1){
                throw new ApiException("系统繁忙，请稍后重试");
            }
        }
    }


}
