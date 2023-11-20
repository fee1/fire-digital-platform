package com.huajie.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.huajie.application.api.request.ProblemQueryRequestVO;
import com.huajie.domain.common.enums.ProblemStateEnum;
import com.huajie.domain.entity.ProblemDetail;
import com.huajie.infrastructure.mapper.ProblemDetailMapper;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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


}
