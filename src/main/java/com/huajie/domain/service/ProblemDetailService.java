package com.huajie.domain.service;

import com.huajie.domain.entity.ProblemDetail;
import com.huajie.infrastructure.mapper.ProblemDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProblemDetailService {

    @Autowired
    private ProblemDetailMapper problemDetailMapper;

    public ProblemDetail insert(ProblemDetail problemDetail){
        problemDetailMapper.insert(problemDetail);
        return problemDetail;
    }


}
