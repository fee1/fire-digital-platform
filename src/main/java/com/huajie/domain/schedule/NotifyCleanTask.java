package com.huajie.domain.schedule;

import com.huajie.domain.common.utils.DateUtil;
import com.huajie.domain.entity.Notice;
import com.huajie.domain.entity.SignForNotice;
import com.huajie.domain.service.NoticeService;
import com.huajie.domain.service.NotifyForNoticeService;
import com.huajie.domain.service.SignForNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.Period;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 删除通知定时任务
 *
 * @author zhuxiaofeng
 * @date 2023/12/10
 */
@Component
public class NotifyCleanTask {

    @Autowired
    private SignForNoticeService signForNoticeService;

    @Autowired
    private NoticeService noticeService;

    @Scheduled(cron = "0 */59 * * * ?")
    public void cleanNotify(){
        List<SignForNotice> notifyForNoticeIsNotDelete =
                signForNoticeService.getNotifyForNoticeIsNotDelete();
        if (!CollectionUtils.isEmpty(notifyForNoticeIsNotDelete)) {
            Map<Integer, List<SignForNotice>> noticeId2Sign = notifyForNoticeIsNotDelete.stream().collect(Collectors.groupingBy(SignForNotice::getNoticeId));
            Set<Integer> noticeIds = notifyForNoticeIsNotDelete.stream().map(SignForNotice::getNoticeId).collect(Collectors.toSet());
            List<Notice> noticeList = noticeService.getNoticeByIds(noticeIds);
            for (Notice notice : noticeList) {
                Integer saveDays = notice.getSaveDays();
                List<SignForNotice> signForNotices = noticeId2Sign.get(notice.getId());
                long days = DateUtil.dateSub(new Date(), signForNotices.get(0).getSendTime());
                if (days > saveDays) {
                    this.signForNoticeService.deleteSignForNoticeByNoticeId(notice.getId());
                }
            }
        }
    }

}
