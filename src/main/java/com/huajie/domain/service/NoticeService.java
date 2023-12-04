package com.huajie.domain.service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.huajie.domain.common.constants.NoticeReceiveTypeConstants;
import com.huajie.domain.common.constants.NoticeStatusConstants;
import com.huajie.domain.common.constants.NoticeTypeConstants;
import com.huajie.domain.common.constants.SpecifyRangeConstants;
import com.huajie.domain.common.enums.SignStatusEnum;
import com.huajie.domain.common.exception.ServerException;
import com.huajie.domain.common.oauth2.model.CustomizeGrantedAuthority;
import com.huajie.domain.common.utils.DateUtil;
import com.huajie.domain.common.utils.UserContext;
import com.huajie.domain.entity.Notice;
import com.huajie.domain.entity.SignForNotice;
import com.huajie.domain.entity.Tenant;
import com.huajie.domain.entity.User;
import com.huajie.infrastructure.mapper.NoticeMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
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

    @Autowired
    private UserService userService;

    @Autowired
    private SignForNoticeService signForNoticeService;

    @Autowired
    private GovermentOrganizationService govermentOrganizationService;

    @Autowired
    private TenantService tenantService;

    public void createNotice(Notice notice) {
        notice.setStatus(NoticeStatusConstants.NOT_PUBLIC.byteValue());
        Tenant currentTenant = UserContext.getCurrentTenant();
        CustomizeGrantedAuthority customizeGrantedAuthority = UserContext.getCustomizeGrantedAuthority();
        notice.setFromUserId(customizeGrantedAuthority.getUserId());
        notice.setFromTenantId(currentTenant.getId());
        noticeMapper.insert(notice);
    }

    public Page<Notice> searchNotice(String title, Integer pageNum, Integer pageSize) {
        Tenant currentTenant = UserContext.getCurrentTenant();
        PageHelper.startPage(pageNum, pageSize);
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Notice::getFromTenantId, currentTenant.getId())
                .like(Notice::getTitle, title);
        return (Page<Notice>)noticeMapper.selectList(queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
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
        updateNotice.setSendTime(date);
        //可能涉及到用户修改,最好不用这个字段
//        CustomizeGrantedAuthority customizeGrantedAuthority = UserContext.getCustomizeGrantedAuthority();
//        updateNotice.setSendUserName(customizeGrantedAuthority.getUserName());

        QueryWrapper<Notice> updateWrapper = new QueryWrapper<>();
        updateWrapper.lambda().eq(Notice::getId, id);
        noticeMapper.update(updateNotice, updateWrapper);

        //通知相关的 签收表数据生成
        if (notice.getType().intValue() == NoticeTypeConstants.NOTIFY) {
            Byte receiveType = notice.getReceiveType();
            if (notice.getSpecifyRange().intValue() == SpecifyRangeConstants.ALL) {
                if (receiveType.intValue() == NoticeReceiveTypeConstants.ENTERPRISE) {
                    if (notice.getSpecifyRange().intValue() == SpecifyRangeConstants.ALL) {
                        List<Tenant> adminEnterpriseList = govermentOrganizationService
                                .getAdminEnterpriseList(1, Integer.MAX_VALUE, "", "", 1);
                        List<SignForNotice> signForNotices = generateSignData(adminEnterpriseList, id, notice.getRoleId());
                        signForNoticeService.InsertBatch(signForNotices);
                    }
                } else if (receiveType.intValue() == NoticeReceiveTypeConstants.GOVERMENT) {
                    if (notice.getSpecifyRange().intValue() == SpecifyRangeConstants.ALL) {
                        List<Tenant> adminGovernmentList = govermentOrganizationService
                                .getAdminGovernmentList(1, Integer.MAX_VALUE, "");
                        List<SignForNotice> signForNotices = generateSignData(adminGovernmentList, id, notice.getRoleId());
                        signForNoticeService.InsertBatch(signForNotices);
                    }
                } else {
                    throw new ServerException("数据异常");
                }
            } else {
                List<Integer> tenantIds = JSONObject.parseObject(notice.getTenantIds(), new TypeReference<List<Integer>>() {
                });
                List<Tenant> tenants = this.tenantService.getTenantByTenantIds(tenantIds);
                List<SignForNotice> signForNotices = generateSignData(tenants, id, notice.getRoleId());
                signForNoticeService.InsertBatch(signForNotices);
            }
        }
    }



    private List<SignForNotice> generateSignData(List<Tenant> signTenants, Integer noticeId, Integer roleId){
        List<SignForNotice> signForNotices = new ArrayList<>();
        for (Tenant signTenant : signTenants) {
            List<User> usersByTenantId = userService.getUsersByTenantId(signTenant.getId());
            for (User user : usersByTenantId) {
                SignForNotice signForNotice = new SignForNotice();
                signForNotice.setNoticeId(noticeId);
                signForNotice.setSignStatus(SignStatusEnum.NotSign.getCode());
                signForNotice.setUserId(user.getId());
                if (roleId == 0) {
                    signForNotices.add(signForNotice);
                }else if (roleId.equals(user.getRoleId())){
                    signForNotices.add(signForNotice);
                }
            }
        }
        return signForNotices;
    }

    public void editNotice(Notice notice) {
        QueryWrapper<Notice> updateWrapper = new QueryWrapper<>();
        updateWrapper.lambda().eq(Notice::getId, notice.getId());
        noticeMapper.update(notice, updateWrapper);
    }

    public Notice detailNotice(Integer noticeId) {
        Notice notice = this.noticeMapper.selectById(noticeId);
        return notice;
    }

    public Page<Notice> getGovPcNoticeList(Integer noticeType, Date startDate, Date endDate, String title, String sendUserName, Integer pageNum, Integer pageSize) {
        List<SignForNotice> signForNoticeList = this.signForNoticeService.getSignForNoticeByUserId(UserContext.getCurrentUserId());
        Set<Integer> noticeIds = signForNoticeList.stream().map(SignForNotice::getNoticeId).collect(Collectors.toSet());
        if (!CollectionUtils.isEmpty(noticeIds)) {
            PageHelper.startPage(pageNum, pageSize);
            return (Page<Notice>) this.noticeMapper.searchNotices(noticeType, startDate, endDate, title, sendUserName, noticeIds);
        }else {
            return new Page<>();
        }
    }

    public Page<Notice> getEntPcNoticeList(Integer noticeType, Date startDate, Date endDate, String title, String sendUserName, Integer pageNum, Integer pageSize) {
        List<SignForNotice> signForNoticeList = this.signForNoticeService.getSignForNoticeByUserId(UserContext.getCurrentUserId());
        Set<Integer> noticeIds = signForNoticeList.stream().map(SignForNotice::getNoticeId).collect(Collectors.toSet());
        if (!CollectionUtils.isEmpty(noticeIds)) {
            PageHelper.startPage(pageNum, pageSize);
            return (Page<Notice>) this.noticeMapper.searchNotices(noticeType, startDate, endDate, title, sendUserName, noticeIds);
        }else {
            return new Page<>();
        }
    }

    public void receive(Integer noticeId) {
        signForNoticeService.receive(noticeId);
    }
}
