package com.huajie.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.huajie.application.api.request.ProblemQueryRequestVO;
import com.huajie.domain.common.enums.ProblemStateEnum;
import com.huajie.domain.common.utils.DateUtil;
import com.huajie.domain.entity.Device;
import com.huajie.domain.entity.ProblemDetail;
import com.huajie.infrastructure.mapper.ProblemDetailMapper;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProblemDetailService {

    @Autowired
    private ProblemDetailMapper problemDetailMapper;

    public List<ProblemDetail> getTimeoutProblemList(){
        QueryWrapper<ProblemDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(ProblemDetail::getState, ProblemStateEnum.TODO.getStateCode())
                .lt(ProblemDetail::getReformTimeoutTime, new Date());
        return problemDetailMapper.selectList(queryWrapper);
    }


    public Page<ProblemDetail> getProblemList(QueryWrapper<ProblemDetail> queryWrapper, Integer pageNum, Integer pageSize){
        PageHelper.startPage(pageNum, pageSize);
        return (Page<ProblemDetail>) problemDetailMapper.selectList(queryWrapper);
    }

    public List<ProblemDetail> getFinishProblemByEnterpriseId(Integer tenantId){
        QueryWrapper<ProblemDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(ProblemDetail::getEntTenantId, tenantId)
                .eq(ProblemDetail::getState, ProblemStateEnum.FINISH);
        return problemDetailMapper.selectList(queryWrapper);
    }

    public List<ProblemDetail> getUnFinishProblemByEnterpriseId(Integer tenantId){
        QueryWrapper<ProblemDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(ProblemDetail::getEntTenantId, tenantId)
                .ne(ProblemDetail::getState, ProblemStateEnum.FINISH);
        return problemDetailMapper.selectList(queryWrapper);
    }

    public List<ProblemDetail> getTimeOutProblemByEnterpriseId(Integer tenantId){
        QueryWrapper<ProblemDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(ProblemDetail::getEntTenantId, tenantId)
                .eq(ProblemDetail::getState, ProblemStateEnum.TIMEOUT);
        return problemDetailMapper.selectList(queryWrapper);
    }

    public Integer getProblemCountByGovernmentId(Integer tenantId){
        QueryWrapper<ProblemDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(ProblemDetail::getGovTenantId, tenantId);
        return problemDetailMapper.selectCount(queryWrapper);
    }

    public Integer getNewProblemCountByGovernmentId(Integer tenantId, Date startDate){
        QueryWrapper<ProblemDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(ProblemDetail::getGovTenantId, tenantId)
                .gt(ProblemDetail::getCreateTime, startDate);
        return problemDetailMapper.selectCount(queryWrapper);
    }

    public List<ProblemDetail> getUnFinishProblemByGovernmentId(Integer tenantId){
        QueryWrapper<ProblemDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(ProblemDetail::getGovTenantId, tenantId)
                .ne(ProblemDetail::getState, ProblemStateEnum.FINISH);
        return problemDetailMapper.selectList(queryWrapper);
    }

    public List<ProblemDetail> getCurrentMonthProblemsByGovernmentId(Integer tenantId){
        QueryWrapper<ProblemDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(ProblemDetail::getGovTenantId, tenantId)
                .gt(ProblemDetail::getCreateTime, DateUtil.getCurrentMonthStartDate());
        return problemDetailMapper.selectList(queryWrapper);
    }

    public ProblemDetail insert(ProblemDetail problemDetail){
        problemDetailMapper.insert(problemDetail);
        return problemDetail;
    }

    public Integer getUnfinishProblemCount(Integer tenantId){
        QueryWrapper<ProblemDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(ProblemDetail::getEntTenantId,tenantId);
        queryWrapper.lambda().ne(ProblemDetail::getState,ProblemStateEnum.FINISH);
        return problemDetailMapper.selectCount(queryWrapper);
    }

    public ProblemDetail getById(Long id){
        return problemDetailMapper.selectById(id);
    }

    public int updateById(ProblemDetail problemDetail){
        return problemDetailMapper.updateById(problemDetail);
    }

    public Map<Integer,Long> getInspectProblemMapByInspectIds(List<Integer> inspectIds){
        if(CollectionUtils.isEmpty(inspectIds)){
            return new HashMap<>();
        }
        QueryWrapper<ProblemDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(ProblemDetail::getRelationId,inspectIds);
        List<ProblemDetail> problemDetails = problemDetailMapper.selectList(queryWrapper);
        return problemDetails.stream().collect(Collectors.toMap(problemDetail -> problemDetail.getRelationId(),problemDetail -> problemDetail.getId()));
    }

    /**
     * 检查设备下是否还有其他隐患, true 是, false 否
     * @return
     */
    public int getDeviceUnfinishedProblemCount(Integer deviceId){
        QueryWrapper<ProblemDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ProblemDetail::getDeviceId, deviceId)
                .ne(ProblemDetail::getState, ProblemStateEnum.FINISH);
        return problemDetailMapper.selectCount(queryWrapper);
    }


}
