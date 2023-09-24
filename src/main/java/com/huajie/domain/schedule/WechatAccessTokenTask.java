package com.huajie.domain.schedule;

import com.huajie.domain.service.WechatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/**
 * @author zhuxiaofeng
 * @date 2023/9/23
 */
@Component
@Slf4j
public class WechatAccessTokenTask {

    @Autowired
    private WechatService wechatService;

    /**
     * todo 日志查看是否两小时运行一次
     * 每两小时运行一次
     */
    @Scheduled(cron = "* * */2 * * *")
    public void refresh(){
        wechatService.refreshAccessToken();
    }

}
