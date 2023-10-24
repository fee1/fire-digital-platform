package com.huajie.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huajie.domain.entity.ProblemDetail;
import com.huajie.infrastructure.mapper.ProblemDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProblemDetailService {

    @Autowired
    private ProblemDetailMapper problemDetailMapper;

    public ProblemDetail insert(ProblemDetail problemDetail){
        problemDetailMapper.insert(problemDetail);
        return problemDetail;
    }

    public Map<Integer,Integer> getInspectProblemMapByInspectIds(List<Integer> inspectIds){
        if(CollectionUtils.isEmpty(inspectIds)){
            return new HashMap<>();
        }
        QueryWrapper<ProblemDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(ProblemDetail::getRelationId,inspectIds);
        List<ProblemDetail> problemDetails = problemDetailMapper.selectList(queryWrapper);
        return problemDetails.stream().collect(Collectors.toMap(problemDetail -> problemDetail.getRelationId(),problemDetail -> problemDetail.getId()));
    }


}
