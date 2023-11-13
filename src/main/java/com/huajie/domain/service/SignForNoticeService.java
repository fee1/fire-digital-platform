package com.huajie.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huajie.domain.common.enums.SignStatusEnum;
import com.huajie.domain.common.oauth2.model.CustomizeGrantedAuthority;
import com.huajie.domain.common.utils.UserContext;
import com.huajie.domain.entity.SignForNotice;
import com.huajie.infrastructure.mapper.SignForNoticeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhuxiaofeng
 * @date 2023/11/12
 */
@Service
public class SignForNoticeService {

    @Autowired
    private SignForNoticeMapper signForNoticeMapper;

    public void InsertBatch(List<SignForNotice> signForNotices) {
        signForNoticeMapper.InsertBatch(signForNotices);
    }

    public Double getSignRateByNoticeId(Integer noticeId) {
        QueryWrapper<SignForNotice> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SignForNotice::getNoticeId, noticeId);
        Integer all = signForNoticeMapper.selectCount(queryWrapper);
        queryWrapper.lambda().eq(SignForNotice::getSignStatus, SignStatusEnum.Sign.getCode());
        Integer sign = signForNoticeMapper.selectCount(queryWrapper);

        return sign.doubleValue() / all.doubleValue();
    }

    public void receive(Integer noticeId) {
        CustomizeGrantedAuthority customizeGrantedAuthority = UserContext.getCustomizeGrantedAuthority();
        Integer userId = customizeGrantedAuthority.getUserId();
        SignForNotice signForNotice = new SignForNotice();
        signForNotice.setSignStatus(SignStatusEnum.Sign.getCode());

        QueryWrapper<SignForNotice> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(SignForNotice::getNoticeId, noticeId)
                .eq(SignForNotice::getUserId, userId);
        this.signForNoticeMapper.update(signForNotice, queryWrapper);
    }
}
