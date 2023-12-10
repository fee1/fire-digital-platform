package com.huajie.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huajie.domain.entity.NotifyForNotice;
import com.huajie.infrastructure.mapper.NotifyForNoticeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhuxiaofeng
 * @date 2023/12/10
 */
@Service
public class NotifyForNoticeService {

    @Autowired
    private NotifyForNoticeMapper notifyForNoticeMapper;

    public void insertBatch(List<NotifyForNotice> notifyForNoticeList){
        notifyForNoticeMapper.insertBatch(notifyForNoticeList);
    }

    public List<NotifyForNotice> getNotifyForNoticeByNoticeId(Integer noticeId) {
        QueryWrapper<NotifyForNotice> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(NotifyForNotice::getNoticeId, noticeId);
        return this.notifyForNoticeMapper.selectList(queryWrapper);
    }

    public List<NotifyForNotice> getNotifyForNoticeByUserId(Integer currentUserId) {
        QueryWrapper<NotifyForNotice> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(NotifyForNotice::getUserId, currentUserId);
        return this.notifyForNoticeMapper.selectList(queryWrapper);
    }
}
