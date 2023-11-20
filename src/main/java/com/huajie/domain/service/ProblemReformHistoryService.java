package com.huajie.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huajie.domain.common.enums.ProblemActionTypeEnum;
import com.huajie.domain.entity.ProblemReformHistory;
import com.huajie.infrastructure.mapper.ProblemReformHistoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class ProblemReformHistoryService {

    @Autowired
    private ProblemReformHistoryMapper problemReformHistoryMapper;

    public int insert(ProblemReformHistory history){
        return problemReformHistoryMapper.insert(history);
    }

    public ProblemReformHistory getLastEnterpriseReform(Long problemId){
        QueryWrapper<ProblemReformHistory> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(ProblemReformHistory::getProblemId,problemId)
                .eq(ProblemReformHistory::getSource,"enterprise")
                .in(ProblemReformHistory::getActionType, ProblemActionTypeEnum.REPLY,ProblemActionTypeEnum.SIGN)
                .orderByDesc(ProblemReformHistory::getSubmitTime);
        List<ProblemReformHistory> selectList = problemReformHistoryMapper.selectList(queryWrapper);
        return CollectionUtils.isEmpty(selectList) ? null : selectList.get(0);
    }

    public ProblemReformHistory getLastGovernmentReform(Long problemId){
        QueryWrapper<ProblemReformHistory> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(ProblemReformHistory::getProblemId,problemId)
                .eq(ProblemReformHistory::getSource,"goverment")
                .orderByDesc(ProblemReformHistory::getSubmitTime);
        List<ProblemReformHistory> selectList = problemReformHistoryMapper.selectList(queryWrapper);
        return CollectionUtils.isEmpty(selectList) ? null : selectList.get(0);
    }

    public List<ProblemReformHistory> getReformHistories(Long problemId){
        QueryWrapper<ProblemReformHistory> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(ProblemReformHistory::getProblemId,problemId)
                .orderByDesc(ProblemReformHistory::getSubmitTime);
        return problemReformHistoryMapper.selectList(queryWrapper);
    }

}
