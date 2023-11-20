package com.huajie.domain.schedule.task;

import com.huajie.domain.common.enums.ProblemStateEnum;
import com.huajie.domain.entity.ProblemDetail;
import com.huajie.domain.service.ProblemDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Component(value = "problemTimeoutTask")
@Slf4j
public class ProblemTimeoutTask {

    @Autowired
    private ProblemDetailService problemDetailService;

    public void problemTimeout(){
        List<ProblemDetail> timeoutProblemList = problemDetailService.getTimeoutProblemList();
        if(!CollectionUtils.isEmpty(timeoutProblemList)){
            log.info("隐患超时定时任务执行成功，隐患id：{}",timeoutProblemList.stream().map(ProblemDetail::getId).collect(Collectors.toList()));
            for (ProblemDetail problemDetail: timeoutProblemList){
                try {
                    ProblemDetail update = new ProblemDetail();
                    update.setId(problemDetail.getId());
                    update.setState(ProblemStateEnum.TIMEOUT.getStateCode());
                    update.setVersion(problemDetail.getVersion());
                    problemDetailService.updateById(problemDetail);
                }catch (Exception e){
                    log.error("隐患超时状态修改失败, id:{}, 错误原因:{}",problemDetail.getId(), e.getMessage());
                }
            }
        }
    }

}
