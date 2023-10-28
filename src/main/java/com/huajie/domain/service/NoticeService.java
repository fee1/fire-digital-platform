package com.huajie.domain.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.huajie.domain.common.constants.NoticeReceiveTypeConstants;
import com.huajie.domain.common.constants.NoticeStatusConstants;
import com.huajie.domain.common.constants.NoticeTypeConstants;
import com.huajie.domain.common.constants.SpecifyRangeConstants;
import com.huajie.domain.common.exception.ServerException;
import com.huajie.domain.common.utils.DateUtil;
import com.huajie.domain.entity.Notice;
import com.huajie.domain.entity.Role;
import com.huajie.infrastructure.mapper.NoticeMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author zhuxiaofeng
 * @date 2023/10/22
 */
@Service
public class NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private RoleService roleService;

    public void createNotice(Notice notice) {
        notice.setStatus(NoticeStatusConstants.NOT_PUBLIC.byteValue());
        noticeMapper.insert(notice);
    }

    public Page<Notice> searchNotice(String title, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().like(Notice::getTitle, title);
        return (Page<Notice>)noticeMapper.selectList(queryWrapper);
    }

    public void publicNotice(Integer id) {
        Notice notice = noticeMapper.selectById(id);
        if (notice.getStatus() == NoticeStatusConstants.PUBLIC.byteValue()
                || notice.getStatus() == NoticeStatusConstants.EXPIRED.byteValue()){
            throw new ServerException("已发布，请勿重复发布");
        }
        Notice updateNotice = new Notice();
        updateNotice.setStatus(NoticeStatusConstants.PUBLIC.byteValue());
        Date date = DateUtil.addDays(new Date(), notice.getSaveDays());
        updateNotice.setExpireTime(date);

        QueryWrapper<Notice> updateWrapper = new QueryWrapper<>();
        updateWrapper.lambda().eq(Notice::getId, id);
        noticeMapper.update(updateNotice, updateWrapper);
    }

    public void editNotice(Notice notice) {
        QueryWrapper<Notice> updateWrapper = new QueryWrapper<>();
        updateWrapper.lambda().eq(Notice::getId, notice.getId());
        noticeMapper.update(notice, updateWrapper);
    }
}
