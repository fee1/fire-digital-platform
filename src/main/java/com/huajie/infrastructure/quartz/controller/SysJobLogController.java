package com.huajie.infrastructure.quartz.controller;


import com.huajie.application.api.common.ApiResult;
import com.huajie.infrastructure.quartz.domain.SysJobLog;
import com.huajie.infrastructure.quartz.service.ISysJobLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 调度日志操作处理
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/monitor/jobLog")
public class SysJobLogController
{
    @Autowired
    private ISysJobLogService jobLogService;

    /**
     * 查询定时任务调度日志列表
     */
    @GetMapping("/list")
    public ApiResult<List<SysJobLog>> list(SysJobLog sysJobLog)
    {
        return ApiResult.ok(jobLogService.selectJobLogList(sysJobLog));
    }
    
    /**
     * 根据调度编号获取详细信息
     */
    @GetMapping(value = "/{jobLogId}")
    public ApiResult getInfo(@PathVariable Long jobLogId)
    {
        return ApiResult.ok(jobLogService.selectJobLogById(jobLogId));
    }


    /**
     * 删除定时任务调度日志
     */
    @DeleteMapping("/{jobLogIds}")
    public ApiResult remove(@PathVariable Long[] jobLogIds)
    {
        return ApiResult.ok(jobLogService.deleteJobLogByIds(jobLogIds)) ;
    }

    /**
     * 清空定时任务调度日志
     */
    @DeleteMapping("/clean")
    public ApiResult clean()
    {
        jobLogService.cleanJobLog();
        return ApiResult.ok();
    }
}
