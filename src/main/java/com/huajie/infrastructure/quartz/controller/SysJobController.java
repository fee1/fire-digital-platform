package com.huajie.infrastructure.quartz.controller;


import com.huajie.application.api.common.ApiResult;
import com.huajie.domain.common.exception.TaskException;
import com.huajie.infrastructure.quartz.domain.SysJob;
import com.huajie.infrastructure.quartz.service.ISysJobService;
import com.huajie.infrastructure.quartz.util.CronUtils;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 调度任务信息操作处理
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/monitor/job")
public class SysJobController
{
    @Autowired
    private ISysJobService jobService;

    /**
     * 查询定时任务列表
     */
    @GetMapping("/list")
    public ApiResult<List<SysJob>> list(SysJob sysJob) {
        
        return ApiResult.ok( jobService.selectJobList(sysJob));
    }
    
    /**
     * 获取定时任务详细信息
     */
    @GetMapping(value = "/{jobId}")
    public ApiResult getInfo(@PathVariable("jobId") Long jobId) {
        return ApiResult.ok(jobService.selectJobById(jobId));
    }

    /**
     * 新增定时任务
     */
    @PostMapping
    public ApiResult add(@RequestBody SysJob job) throws SchedulerException, TaskException
    {
        if (!CronUtils.isValid(job.getCronExpression()))
        {
            return ApiResult.failed("新增任务'" + job.getJobName() + "'失败，Cron表达式不正确");
        }

        return ApiResult.ok(jobService.insertJob(job));
    }

    /**
     * 修改定时任务
     */
    @PutMapping
    public ApiResult edit(@RequestBody SysJob job) throws SchedulerException, TaskException
    {
        if (!CronUtils.isValid(job.getCronExpression()))
        {
            return ApiResult.failed("修改任务'" + job.getJobName() + "'失败，Cron表达式不正确");
        }


        return ApiResult.ok(jobService.updateJob(job));
    }

    /**
     * 定时任务状态修改
     */
    @PutMapping("/changeStatus")
    public ApiResult changeStatus(@RequestBody SysJob job) throws SchedulerException
    {
        SysJob newJob = jobService.selectJobById(job.getJobId());
        newJob.setStatus(job.getStatus());
        return ApiResult.ok(jobService.changeStatus(newJob));
    }

    /**
     * 定时任务立即执行一次
     */
    @PutMapping("/run")
    public ApiResult run(@RequestBody SysJob job) throws SchedulerException
    {
        boolean result = jobService.run(job);
        return result ? ApiResult.ok() : ApiResult.failed("任务不存在或已过期！");
    }

    /**
     * 删除定时任务
     */
    @DeleteMapping("/{jobIds}")
    public ApiResult remove(@PathVariable Long[] jobIds) throws SchedulerException, TaskException
    {
        jobService.deleteJobByIds(jobIds);
        return ApiResult.ok();
    }
}
