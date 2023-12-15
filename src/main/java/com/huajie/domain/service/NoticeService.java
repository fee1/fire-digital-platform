package com.huajie.domain.service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.huajie.domain.common.constants.NoticeReceiveTypeConstants;
import com.huajie.domain.common.constants.NoticeStatusConstants;
import com.huajie.domain.common.constants.NoticeTypeConstants;
import com.huajie.domain.common.constants.RoleCodeConstants;
import com.huajie.domain.common.constants.SpecifyRangeConstants;
import com.huajie.domain.common.exception.ServerException;
import com.huajie.domain.common.oauth2.model.CustomizeGrantedAuthority;
import com.huajie.domain.common.utils.DateUtil;
import com.huajie.domain.common.utils.UserContext;
import com.huajie.domain.entity.Notice;
import com.huajie.domain.entity.NotifyForNotice;
import com.huajie.domain.entity.Role;
import com.huajie.domain.entity.SignForNotice;
import com.huajie.domain.entity.Tenant;
import com.huajie.domain.entity.User;
import com.huajie.domain.model.NoticeModel;
import com.huajie.infrastructure.mapper.NoticeMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
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

    @Autowired
    private NotifyForNoticeService notifyForNoticeService;

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
        queryWrapper.lambda().eq(Notice::getFromTenantId, currentTenant.getId());
        if (StringUtils.isNotBlank(title)) {
            queryWrapper.lambda().like(Notice::getTitle, title);
        }
        queryWrapper.lambda().orderByDesc(Notice::getCreateTime);
        return (Page<Notice>)noticeMapper.selectList(queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    public void publicNotice(Integer id) {
        Notice notice = noticeMapper.selectById(id);
        if (!UserContext.getCurrentTenant().getId().equals(notice.getFromTenantId())){
            throw new ServerException("不可发布其他租户下的通知通告");
        }

        if (notice.getStatus() == NoticeStatusConstants.PUBLIC.byteValue()
                || notice.getStatus() == NoticeStatusConstants.EXPIRED.byteValue()){
            throw new ServerException("已发布，请勿重复发布");
        }
        Notice updateNotice = new Notice();
        updateNotice.setStatus(NoticeStatusConstants.PUBLIC.byteValue());

        QueryWrapper<Notice> updateWrapper = new QueryWrapper<>();
        updateWrapper.lambda().eq(Notice::getId, id);
        noticeMapper.update(updateNotice, updateWrapper);

        //通知相关的 签收表数据生成
        if (notice.getType().intValue() == NoticeTypeConstants.NOTIFY) {
            Byte receiveType = notice.getReceiveType();
            if (notice.getSpecifyRange().intValue() == SpecifyRangeConstants.ALL) {
                if (receiveType.intValue() == NoticeReceiveTypeConstants.ENTERPRISE) {
                        List<Tenant> adminEnterpriseList = govermentOrganizationService
                                .getAdminEnterpriseList(1, Integer.MAX_VALUE, "", "", 1);
                    List<SignForNotice> signForNotices = new ArrayList<>();
                        if (StringUtils.equals("all", notice.getRoleName())) {
                            Role roleByCode = this.roleService.getRoleByCode(RoleCodeConstants.ENT_ADMIN_CODE);
                            signForNotices.addAll(generateSignDataForSignNotice(adminEnterpriseList, id, roleByCode.getId()));

                            roleByCode = this.roleService.getRoleByCode(RoleCodeConstants.ENT_OPERATOR_CODE);
                            signForNotices.addAll(generateSignDataForSignNotice(adminEnterpriseList, id, roleByCode.getId()));
                        }else {
                            Role roleByCode = this.roleService.getRoleByCode(notice.getRoleName());
                            signForNotices.addAll(generateSignDataForSignNotice(adminEnterpriseList, id, roleByCode.getId()));
                        }
                        signForNoticeService.insertBatch(signForNotices);
                } else if (receiveType.intValue() == NoticeReceiveTypeConstants.GOVERMENT) {
                        List<Tenant> adminGovernmentList = govermentOrganizationService
                                .getAdminGovernmentList(1, Integer.MAX_VALUE, "");
                    List<SignForNotice> signForNotices = new ArrayList<>();
                    if (StringUtils.equals("all", notice.getRoleName())) {
                        Role roleByCode = this.roleService.getRoleByCode(RoleCodeConstants.GOV_ADMIN_CODE);
                        signForNotices.addAll(generateSignDataForSignNotice(adminGovernmentList, id, roleByCode.getId()));

                        roleByCode = this.roleService.getRoleByCode(RoleCodeConstants.GOV_OPERATOR_CODE);
                        signForNotices.addAll(generateSignDataForSignNotice(adminGovernmentList, id, roleByCode.getId()));
                    }else {
                        Role roleByCode = this.roleService.getRoleByCode(notice.getRoleName());
                        signForNotices.addAll(generateSignDataForSignNotice(adminGovernmentList, id, roleByCode.getId()));
                    }
                        signForNoticeService.insertBatch(signForNotices);
                } else {
                    throw new ServerException("数据异常");
                }
            } else {
                List<Integer> tenantIds = JSONObject.parseObject(notice.getTenantIds(), new TypeReference<List<Integer>>() {
                });
                List<Tenant> tenants = this.tenantService.getTenantByTenantIds(tenantIds);
                Role roleByCode = this.roleService.getRoleByCode(notice.getRoleName());
                List<SignForNotice> signForNotices = generateSignDataForSignNotice(tenants, id, roleByCode.getId());
                signForNoticeService.insertBatch(signForNotices);
            }
        }else if (notice.getType().intValue() == NoticeTypeConstants.NOTICE){
            //通告数据表生成
            Byte receiveType = notice.getReceiveType();
            if (notice.getSpecifyRange().intValue() == SpecifyRangeConstants.ALL) {
                if (receiveType.intValue() == NoticeReceiveTypeConstants.ENTERPRISE) {
                    List<Tenant> adminEnterpriseList = govermentOrganizationService
                            .getAdminEnterpriseList(1, Integer.MAX_VALUE, "", "", 1);
                    List<NotifyForNotice> notifyForNotices = new ArrayList<>();
                    if (StringUtils.equals("all", notice.getRoleName())) {
                        Role roleByCode = this.roleService.getRoleByCode(RoleCodeConstants.ENT_ADMIN_CODE);
                        notifyForNotices.addAll(generateSignDataForNotifyNotice(adminEnterpriseList, id, roleByCode.getId()));

                        roleByCode = this.roleService.getRoleByCode(RoleCodeConstants.ENT_OPERATOR_CODE);
                        notifyForNotices.addAll(generateSignDataForNotifyNotice(adminEnterpriseList, id, roleByCode.getId()));
                    }else {
                        Role roleByCode = this.roleService.getRoleByCode(notice.getRoleName());
                        notifyForNotices.addAll(generateSignDataForNotifyNotice(adminEnterpriseList, id, roleByCode.getId()));
                    }
                    notifyForNoticeService.insertBatch(notifyForNotices);
                } else if (receiveType.intValue() == NoticeReceiveTypeConstants.GOVERMENT) {
                    List<Tenant> adminGovernmentList = govermentOrganizationService
                            .getAdminGovernmentList(1, Integer.MAX_VALUE, "");
                    List<NotifyForNotice> notifyForNotices = new ArrayList<>();
                    if (StringUtils.equals("all", notice.getRoleName())) {
                        Role roleByCode = this.roleService.getRoleByCode(RoleCodeConstants.GOV_ADMIN_CODE);
                        notifyForNotices.addAll(generateSignDataForNotifyNotice(adminGovernmentList, id, roleByCode.getId()));

                        roleByCode = this.roleService.getRoleByCode(RoleCodeConstants.GOV_OPERATOR_CODE);
                        notifyForNotices.addAll(generateSignDataForNotifyNotice(adminGovernmentList, id, roleByCode.getId()));
                    }else {
                        Role roleByCode = this.roleService.getRoleByCode(notice.getRoleName());
                        notifyForNotices.addAll(generateSignDataForNotifyNotice(adminGovernmentList, id, roleByCode.getId()));
                    }
                    notifyForNoticeService.insertBatch(notifyForNotices);
                } else {
                    throw new ServerException("数据异常");
                }
            } else {
                List<Integer> tenantIds = JSONObject.parseObject(notice.getTenantIds(), new TypeReference<List<Integer>>() {
                });
                List<Tenant> tenants = this.tenantService.getTenantByTenantIds(tenantIds);
                Role roleByCode = this.roleService.getRoleByCode(notice.getRoleName());
                List<SignForNotice> signForNotices = generateSignDataForSignNotice(tenants, id, roleByCode.getId());
                signForNoticeService.insertBatch(signForNotices);
            }
        }

    }

    private List<NotifyForNotice> generateSignDataForNotifyNotice(List<Tenant> signTenants, Integer noticeId, Integer roleId){
        List<NotifyForNotice> notifyForNotices = new ArrayList<>();
        for (Tenant signTenant : signTenants) {
            List<User> usersByTenantId = userService.getUsersByTenantId(signTenant.getId());
            for (User user : usersByTenantId) {
                NotifyForNotice notifyForNotice = new NotifyForNotice();
                notifyForNotice.setNoticeId(noticeId);
                notifyForNotice.setUserId(user.getId());
                notifyForNotice.setSendTime(new Date());
                notifyForNotice.setSendUserId(UserContext.getCurrentUserId());
                if (roleId == 0) {
                    notifyForNotices.add(notifyForNotice);
                }else if (roleId.equals(user.getRoleId())){
                    notifyForNotices.add(notifyForNotice);
                }
            }
        }
        return notifyForNotices;
    }

    private List<SignForNotice> generateSignDataForSignNotice(List<Tenant> signTenants, Integer noticeId, Integer roleId){
        List<SignForNotice> signForNotices = new ArrayList<>();
        for (Tenant signTenant : signTenants) {
            List<User> usersByTenantId = userService.getUsersByTenantId(signTenant.getId());
            for (User user : usersByTenantId) {
                SignForNotice signForNotice = new SignForNotice();
                signForNotice.setNoticeId(noticeId);
                signForNotice.setUserId(user.getId());
                signForNotice.setSendTime(new Date());
                signForNotice.setSendUserId(UserContext.getCurrentUserId());
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
        Notice oldNotice = noticeMapper.selectById(notice.getId());
        if (oldNotice.getStatus() == NoticeStatusConstants.PUBLIC.byteValue()
                || oldNotice.getStatus() == NoticeStatusConstants.EXPIRED.byteValue()){
            throw new ServerException("已发布，无法编辑");
        }
        noticeMapper.update(notice, updateWrapper);
    }

    public NoticeModel detailNotice(Integer noticeId) {
        Notice notice = this.noticeMapper.selectById(noticeId);
        if (notice.getType().intValue() == NoticeTypeConstants.NOTICE) {
            NoticeModel noticeModel = new NoticeModel();
            BeanUtils.copyProperties(notice, noticeModel);

            List<NotifyForNotice> notifyForNotices = this.notifyForNoticeService.getNotifyForNoticeByNoticeId(noticeId);
            if (!CollectionUtils.isEmpty(notifyForNotices)) {
                NotifyForNotice notifyForNotice = notifyForNotices.get(0);
                if (notifyForNotice != null) {
                    User user = userService.getUserById(notifyForNotice.getSendUserId());
                    noticeModel.setPhone(user.getPhone());
                    noticeModel.setSendUserName(user.getUserName());
                    noticeModel.setHeadPic(user.getHeadPic());
                }
            }
            return noticeModel;
        }else {
            NoticeModel noticeModel = new NoticeModel();
            BeanUtils.copyProperties(notice, noticeModel);

            List<SignForNotice> signForNotices = this.signForNoticeService.getSignForNoticeByNoticeId(noticeId);
            if (!CollectionUtils.isEmpty(signForNotices)) {
                SignForNotice signForNotice = signForNotices.get(0);
                if (signForNotice != null) {
                    User user = userService.getUserById(signForNotice.getSendUserId());
                    noticeModel.setPhone(user.getPhone());
                    noticeModel.setSendUserName(user.getUserName());
                    noticeModel.setHeadPic(user.getHeadPic());
                }
            }
            return noticeModel;
        }
    }

    public Page<NoticeModel> getNoticeList(Integer noticeType, Date startDate, Date endDate, String title, String sendUserName, Integer pageNum, Integer pageSize) {
        if (noticeType.equals(NoticeTypeConstants.NOTIFY)) {
            List<SignForNotice> signForNoticeList = this.signForNoticeService.getSignForNoticeByUserId(UserContext.getCurrentUserId());
            Set<Integer> noticeIds = signForNoticeList.stream().map(SignForNotice::getNoticeId).collect(Collectors.toSet());
            Map<Integer, SignForNotice> noticeId2SignForNotice = signForNoticeList.stream().collect(Collectors.toMap(SignForNotice::getNoticeId, item -> item));
            if (!CollectionUtils.isEmpty(noticeIds)) {
                PageHelper.startPage(pageNum, pageSize);
                List<Notice> notices = this.noticeMapper.searchNotices(noticeType, startDate, endDate, title, sendUserName, noticeIds);

                Page<NoticeModel> page = new Page<>();
                BeanUtils.copyProperties(notices, page);

                Map<Integer, Byte> noticeId2SignStatus = signForNoticeList.stream().collect(Collectors.toMap(SignForNotice::getNoticeId, SignForNotice::getSignStatus));
                for (Notice notice : notices) {
                    NoticeModel noticeModel = new NoticeModel();
                    BeanUtils.copyProperties(notice, noticeModel);
                    noticeModel.setSignStatus(noticeId2SignStatus.get(notice.getId()).intValue());

                    SignForNotice signForNotice = noticeId2SignForNotice.get(notice.getId());
                    if (signForNotice != null) {
                        User userById = this.userService.getUserById(signForNotice.getSendUserId());
                        noticeModel.setSendUserName(userById.getUserName());
                        noticeModel.setHeadPic(userById.getHeadPic());
                    }
                    page.add(noticeModel);
                }
                return page;
            } else {
                return new Page<>();
            }
        }else {
            List<NotifyForNotice> notifyForNotices = this.notifyForNoticeService.getNotifyForNoticeByUserId(UserContext.getCurrentUserId());
            Set<Integer> noticeIds = notifyForNotices.stream().map(NotifyForNotice::getNoticeId).collect(Collectors.toSet());
            Map<Integer, NotifyForNotice> noticeId2NotifyForNotice = notifyForNotices.stream().collect(Collectors.toMap(NotifyForNotice::getNoticeId, item -> item));
            if (!CollectionUtils.isEmpty(noticeIds)) {
                PageHelper.startPage(pageNum, pageSize);
                List<Notice> notices = this.noticeMapper.searchNotices(noticeType, startDate, endDate, title, sendUserName, noticeIds);

                Page<NoticeModel> page = new Page<>();
                BeanUtils.copyProperties(notices, page);

                for (Notice notice : notices) {
                    NoticeModel noticeModel = new NoticeModel();
                    BeanUtils.copyProperties(notice, noticeModel);
                    noticeModel.setSignStatus(0);

                    NotifyForNotice notifyForNotice = noticeId2NotifyForNotice.get(notice.getId());
                    if (notifyForNotice != null) {
                        User userById = this.userService.getUserById(notifyForNotice.getSendUserId());
                        noticeModel.setSendUserName(userById.getUserName());
                        noticeModel.setHeadPic(userById.getHeadPic());
                    }
                    page.add(noticeModel);
                }
                return page;
            } else {
                return new Page<>();
            }
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
