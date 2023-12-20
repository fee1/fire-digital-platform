package com.huajie.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huajie.domain.common.enums.IsDeleteEnum;
import com.huajie.domain.common.enums.SignStatusEnum;
import com.huajie.domain.common.oauth2.model.CustomizeGrantedAuthority;
import com.huajie.domain.common.utils.UserContext;
import com.huajie.domain.entity.SignForNotice;
import com.huajie.infrastructure.mapper.SignForNoticeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author zhuxiaofeng
 * @date 2023/11/12
 */
@Service
public class SignForNoticeService {

    @Autowired
    private SignForNoticeMapper signForNoticeMapper;

    public void insertBatch(List<SignForNotice> signForNotices) {
        signForNoticeMapper.insertBatch(signForNotices);
    }

    public Double getSignRateByNoticeId(Integer noticeId) {
        QueryWrapper<SignForNotice> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SignForNotice::getNoticeId, noticeId);
        Integer all = signForNoticeMapper.selectCount(queryWrapper);
        queryWrapper.lambda().eq(SignForNotice::getSignStatus, SignStatusEnum.Sign.getCode());
        Integer sign = signForNoticeMapper.selectCount(queryWrapper);

        if (all == 0){
            return 0.0;
        }
        return sign.doubleValue() / all.doubleValue();
    }

    public void receive(Integer noticeId) {
        CustomizeGrantedAuthority customizeGrantedAuthority = UserContext.getCustomizeGrantedAuthority();
        Integer userId = customizeGrantedAuthority.getUserId();
        SignForNotice signForNotice = new SignForNotice();
        signForNotice.setSignStatus(SignStatusEnum.Sign.getCode());
        signForNotice.setSendTime(new Date());

        QueryWrapper<SignForNotice> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(SignForNotice::getNoticeId, noticeId)
                .eq(SignForNotice::getUserId, userId);
        this.signForNoticeMapper.update(signForNotice, queryWrapper);
    }

    public List<SignForNotice> getSignForNoticeByUserId(Integer currentUserId) {
        QueryWrapper<SignForNotice> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(SignForNotice::getUserId, currentUserId)
                .eq(SignForNotice::getIsDelete, IsDeleteEnum.UN_DELETE.getCode());
        return this.signForNoticeMapper.selectList(queryWrapper);
    }

    public List<SignForNotice> getSignForNoticeByUserIdAndBetweenTime(Integer currentUserId, Date startTime, Date endTime) {
        QueryWrapper<SignForNotice> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(SignForNotice::getUserId, currentUserId)
                .eq(SignForNotice::getIsDelete, IsDeleteEnum.UN_DELETE.getCode())
                .ge(SignForNotice::getSendTime, startTime)
                .le(SignForNotice::getSendTime, endTime);
        return this.signForNoticeMapper.selectList(queryWrapper);
    }

    public SignForNotice getSignForNoticeIdAndUserId(Integer noticeId, Integer userId) {
        QueryWrapper<SignForNotice> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SignForNotice::getNoticeId, noticeId)
                .eq(SignForNotice::getUserId, userId);
        SignForNotice signForNotice = this.signForNoticeMapper.selectOne(queryWrapper);
        return signForNotice;
    }

    public List<SignForNotice> getSignForNoticeByNoticeId(Integer noticeId) {
        QueryWrapper<SignForNotice> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SignForNotice::getNoticeId, noticeId);
        return this.signForNoticeMapper.selectList(queryWrapper);
    }

    public List<SignForNotice> getNotifyForNoticeIsNotDelete() {
        QueryWrapper<SignForNotice> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(SignForNotice::getIsDelete, IsDeleteEnum.UN_DELETE.getCode());
        return this.signForNoticeMapper.selectList(queryWrapper);
    }

    public void deleteSignForNoticeByNoticeId(Integer noticeId) {
        QueryWrapper<SignForNotice> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SignForNotice::getNoticeId, noticeId);
        SignForNotice signForNotice = new SignForNotice();
        signForNotice.setIsDelete(IsDeleteEnum.DELETE.getCode());
        this.signForNoticeMapper.update(signForNotice, queryWrapper);
    }
}
