package com.huajie.infrastructure.quartz.domain;


import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 定时任务调度日志表 sys_job_log
 * 
 * @author ruoyi
 */
@Data
public class SysJobLog implements Serializable {
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long jobLogId;

    /** 任务名称 */
    private String jobName;

    /** 任务组名 */
    private String jobGroup;

    /** 调用目标字符串 */
    private String invokeTarget;

    /** 日志信息 */
    private String jobMessage;

    /** 执行状态（0正常 1失败） */
    private String status;

    /** 异常信息 */
    private String exceptionInfo;

    /** 开始时间 */
    private Date startTime;

    /** 停止时间 */
    private Date stopTime;

    /** 开始时间 查询条件 */
    @TableField(exist = false)
    private Date beginTime;

    /** 截至时间 查询条件*/
    @TableField(exist = false)
    private Date endTime;

}
