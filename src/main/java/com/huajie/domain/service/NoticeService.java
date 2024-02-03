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
import com.huajie.domain.common.constants.TenantTypeConstants;
import com.huajie.domain.common.enums.SignStatusEnum;
import com.huajie.domain.common.exception.ServerException;
import com.huajie.domain.common.oauth2.model.CustomizeGrantedAuthority;
import com.huajie.domain.common.utils.AssertUtil;
import com.huajie.domain.common.utils.UserContext;
import com.huajie.domain.common.utils.ValidatorUtil;
import com.huajie.domain.convertor.NoticeConvertor;
import com.huajie.domain.entity.Notice;
import com.huajie.domain.entity.NotifyForNotice;
import com.huajie.domain.entity.Role;
import com.huajie.domain.entity.SignForNotice;
import com.huajie.domain.entity.Tenant;
import com.huajie.domain.entity.User;
import com.huajie.domain.model.NoticeModel;
import com.huajie.domain.model.SysCreateNotice;
import com.huajie.infrastructure.mapper.NoticeMapper;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author zhuxiaofeng
 * @date 2023/10/22
 */
@Service
@Slf4j
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

    @Autowired
    private NoticeConvertor noticeConvertor;

    public void createNotice(Notice notice) {
        notice.setStatus(NoticeStatusConstants.NOT_PUBLIC.byteValue());
        Tenant currentTenant = UserContext.getCurrentTenant();
        CustomizeGrantedAuthority customizeGrantedAuthority = UserContext.getCustomizeGrantedAuthority();
        notice.setFromUserId(customizeGrantedAuthority.getUserId());
        notice.setFromTenantId(currentTenant.getId());
        noticeMapper.insert(notice);
    }

    /**
     * 创建并且发送通知通告给指定用户
     *
     * @param createNotice
     */
    @Transactional(rollbackFor = Exception.class)
    public void sysCreateAndPublicNotice(SysCreateNotice createNotice){
        log.info("param: {}", createNotice);
        String result = ValidatorUtil.simpleValidate(createNotice);

        AssertUtil.isTrue(StringUtils.isBlank(result),"入参错误");

        Tenant systemTenant = tenantService.getTenantByType(TenantTypeConstants.SYSTEM);
        AssertUtil.nonNull(systemTenant, "没有设置超级租户");
        List<User> usersByTenantId = userService.getUsersByTenantId(systemTenant.getId());
        AssertUtil.isTrue(!CollectionUtils.isEmpty(usersByTenantId), "没有系统用户");

        Notice notice = new Notice();
        notice.setFromUserId(usersByTenantId.get(0).getId());
        notice.setFromTenantId(1);
        notice.setType(createNotice.getType().byteValue());
        notice.setReceiveType(NoticeReceiveTypeConstants.SYSTEM.byteValue());
        notice.setStatus(NoticeStatusConstants.NOT_PUBLIC.byteValue());
        notice.setRoleName("all");
        notice.setSpecifyRange(SpecifyRangeConstants.SYSTEM.byteValue());
        notice.setTenantIds("[]");
        notice.setTitle(createNotice.getTitle());
        notice.setText(createNotice.getText());
        notice.setAppendix("[]");
        notice.setSaveDays(createNotice.getSaveDays());
        this.noticeMapper.insert(notice);

        Date sendTime = new Date();
        if (notice.getType().intValue() == NoticeTypeConstants.NOTIFY){
            List<Integer> userIdList = createNotice.getUserIdList();

            List<SignForNotice> signForNoticeList = new ArrayList<>();
            for (Integer userId : userIdList) {
                SignForNotice signForNotice = new SignForNotice();
                signForNotice.setUserId(userId);
                signForNotice.setNoticeId(notice.getId());
                signForNotice.setSignStatus(SignStatusEnum.NotSign.getCode());
                signForNotice.setSendTime(sendTime);
                signForNotice.setSendUserId(notice.getFromUserId());
                signForNoticeList.add(signForNotice);
            }
            signForNoticeService.insertBatch(signForNoticeList);
        }else if(notice.getType().intValue() == NoticeTypeConstants.NOTICE){
            List<Integer> userIdList = createNotice.getUserIdList();

            List<NotifyForNotice> notifyForNotices = new ArrayList<>();
            for (Integer userId : userIdList) {
                NotifyForNotice notifyForNotice = new NotifyForNotice();
                notifyForNotice.setUserId(userId);
                notifyForNotice.setNoticeId(notice.getId());
                notifyForNotice.setSendUserId(notice.getFromUserId());
                notifyForNotice.setSendTime(sendTime);
                notifyForNotices.add(notifyForNotice);
            }
            notifyForNoticeService.insertBatch(notifyForNotices);
        }

        notice.setStatus(NoticeStatusConstants.PUBLIC.byteValue());
        noticeMapper.updateById(notice);
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
            List<SignForNotice> signForNotices = this.generateSignDataForSignNotice(notice);
            signForNoticeService.insertBatch(signForNotices);
        }else if (notice.getType().intValue() == NoticeTypeConstants.NOTICE){
            //通告数据表生成
            List<NotifyForNotice> notifyForNotices = this.generateSignDataForNotifyNotice(notice);
            notifyForNoticeService.insertBatch(notifyForNotices);
        }

    }

    private List<NotifyForNotice> generateSignDataForNotifyNotice(Notice notice){
        List<NotifyForNotice> notifyForNotices = new ArrayList<>();
        Byte receiveType = notice.getReceiveType();
        if (notice.getSpecifyRange().intValue() == SpecifyRangeConstants.ALL) {
            if (receiveType.intValue() == NoticeReceiveTypeConstants.ENTERPRISE) {
                List<Tenant> adminEnterpriseList = govermentOrganizationService
                        .getAdminEnterpriseList(1, Integer.MAX_VALUE, "", "", 1);

                notifyForNotices = this.generateSignDataForNotifyNotice(notice, adminEnterpriseList);
            } else if (receiveType.intValue() == NoticeReceiveTypeConstants.GOVERMENT) {
                List<Tenant> adminGovernmentList = govermentOrganizationService
                        .getAdminGovernmentList(1, Integer.MAX_VALUE, "");
                adminGovernmentList.add(UserContext.getCurrentTenant());
                notifyForNotices = this.generateSignDataForNotifyNotice(notice, adminGovernmentList);
            }
        } else {
            List<Integer> tenantIds = JSONObject.parseObject(notice.getTenantIds(), new TypeReference<List<Integer>>() {
            });
            List<Tenant> tenants = this.tenantService.getTenantByTenantIds(tenantIds);
            notifyForNotices = generateSignDataForNotifyNotice(notice, tenants);
        }
        return notifyForNotices;
    }

    private List<NotifyForNotice> generateSignDataForNotifyNotice(Notice notice, List<Tenant> tenants){
        List<NotifyForNotice> notifyForNotices = new ArrayList<>();
        Byte receiveType = notice.getReceiveType();
        if (receiveType.intValue() == NoticeReceiveTypeConstants.ENTERPRISE) {
            if (StringUtils.equals("all", notice.getRoleName())) {
                Role roleByCode = this.roleService.getRoleByCode(RoleCodeConstants.ENT_ADMIN_CODE);
                notifyForNotices.addAll(generateSignDataForNotifyNotice(tenants, notice.getId(), roleByCode.getId()));

                roleByCode = this.roleService.getRoleByCode(RoleCodeConstants.ENT_OPERATOR_CODE);
                notifyForNotices.addAll(generateSignDataForNotifyNotice(tenants, notice.getId(), roleByCode.getId()));
            }else {
                Role roleByCode = this.roleService.getRoleByCode(notice.getRoleName());
                notifyForNotices.addAll(generateSignDataForNotifyNotice(tenants, notice.getId(), roleByCode.getId()));
            }
        } else if (receiveType.intValue() == NoticeReceiveTypeConstants.GOVERMENT) {
            if (StringUtils.equals("all", notice.getRoleName())) {
                Role roleByCode = this.roleService.getRoleByCode(RoleCodeConstants.GOV_ADMIN_CODE);
                notifyForNotices.addAll(generateSignDataForNotifyNotice(tenants, notice.getId(), roleByCode.getId()));

                roleByCode = this.roleService.getRoleByCode(RoleCodeConstants.GOV_OPERATOR_CODE);
                notifyForNotices.addAll(generateSignDataForNotifyNotice(tenants, notice.getId(), roleByCode.getId()));
            }else {
                Role roleByCode = this.roleService.getRoleByCode(notice.getRoleName());
                notifyForNotices.addAll(generateSignDataForNotifyNotice(tenants, notice.getId(), roleByCode.getId()));
            }
        }
        return notifyForNotices;
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

    private List<SignForNotice> generateSignDataForSignNotice(Notice notice){
        List<SignForNotice> signForNotices = new ArrayList<>();
        if (notice.getSpecifyRange().intValue() == SpecifyRangeConstants.ALL) {
            if (notice.getReceiveType().intValue() == NoticeReceiveTypeConstants.ENTERPRISE) {
                List<Tenant> adminEnterpriseList = govermentOrganizationService
                        .getAdminEnterpriseList(1, Integer.MAX_VALUE, "", "", 1);

                signForNotices = generateSignDataForSignNotice(notice, adminEnterpriseList);
            } else if (notice.getReceiveType().intValue() == NoticeReceiveTypeConstants.GOVERMENT) {
                List<Tenant> adminGovernmentList = govermentOrganizationService
                        .getAdminGovernmentList(1, Integer.MAX_VALUE, "");
                adminGovernmentList.add(UserContext.getCurrentTenant());
                signForNotices = generateSignDataForSignNotice(notice, adminGovernmentList);
            }
        }else {
            List<Integer> tenantIds = JSONObject.parseObject(notice.getTenantIds(), new TypeReference<List<Integer>>() {
            });
            List<Tenant> tenants = this.tenantService.getTenantByTenantIds(tenantIds);
            signForNotices = generateSignDataForSignNotice(notice, tenants);
        }
        return signForNotices;
    }

    private List<SignForNotice> generateSignDataForSignNotice(Notice notice, List<Tenant> tenants){
        List<SignForNotice> signForNotices = new ArrayList<>();
        if (notice.getReceiveType().intValue() == NoticeReceiveTypeConstants.ENTERPRISE) {
            if (StringUtils.equals("all", notice.getRoleName())) {
                Role roleByCode = this.roleService.getRoleByCode(RoleCodeConstants.ENT_ADMIN_CODE);
                signForNotices.addAll(generateSignDataForSignNotice(tenants, notice.getId(), roleByCode.getId()));

                roleByCode = this.roleService.getRoleByCode(RoleCodeConstants.ENT_OPERATOR_CODE);
                signForNotices.addAll(generateSignDataForSignNotice(tenants, notice.getId(), roleByCode.getId()));
            } else {
                Role roleByCode = this.roleService.getRoleByCode(notice.getRoleName());
                signForNotices.addAll(generateSignDataForSignNotice(tenants, notice.getId(), roleByCode.getId()));
            }

        } else if (notice.getReceiveType().intValue() == NoticeReceiveTypeConstants.GOVERMENT) {
            if (StringUtils.equals("all", notice.getRoleName())) {
                Role roleByCode = this.roleService.getRoleByCode(RoleCodeConstants.GOV_ADMIN_CODE);
                signForNotices.addAll(generateSignDataForSignNotice(tenants, notice.getId(), roleByCode.getId()));

                roleByCode = this.roleService.getRoleByCode(RoleCodeConstants.GOV_OPERATOR_CODE);
                signForNotices.addAll(generateSignDataForSignNotice(tenants, notice.getId(), roleByCode.getId()));
            } else {
                Role roleByCode = this.roleService.getRoleByCode(notice.getRoleName());
                signForNotices.addAll(generateSignDataForSignNotice(tenants, notice.getId(), roleByCode.getId()));
            }
        }
        return signForNotices;
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
                    noticeModel.setSendTime(notifyForNotice.getSendTime());
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
                    noticeModel.setSendTime(signForNotice.getSendTime());
                }
            }
            return noticeModel;
        }
    }

    public Page<NoticeModel> getNoticeList(Integer noticeType, Date startDate, Date endDate, String title, String sendUserName, Integer pageNum, Integer pageSize) {
        if (noticeType.equals(NoticeTypeConstants.NOTIFY)) {
            List<SignForNotice> signForNoticeList = this.signForNoticeService.getSignForNoticeByUserIdAndBetweenTime(UserContext.getCurrentUserId(), startDate, endDate);
            Set<Integer> noticeIds = signForNoticeList.stream().map(SignForNotice::getNoticeId).collect(Collectors.toSet());
            Map<Integer, SignForNotice> noticeId2SignForNotice = signForNoticeList.stream().collect(Collectors.toMap(SignForNotice::getNoticeId, item -> item));
            if (!CollectionUtils.isEmpty(noticeIds)) {
                PageHelper.startPage(pageNum, pageSize);
                List<Notice> notices = this.noticeMapper.searchNotices(noticeType, title, sendUserName, noticeIds);

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
                        noticeModel.setSendTime(signForNotice.getSendTime());
                    }
                    page.add(noticeModel);
                }
                return page;
            } else {
                return new Page<>();
            }
        }else {
            List<NotifyForNotice> notifyForNotices = this.notifyForNoticeService.getNotifyForNoticeByUserIdAndBetweenTime(UserContext.getCurrentUserId(), startDate, endDate);
            Set<Integer> noticeIds = notifyForNotices.stream().map(NotifyForNotice::getNoticeId).collect(Collectors.toSet());
            Map<Integer, NotifyForNotice> noticeId2NotifyForNotice = notifyForNotices.stream().collect(Collectors.toMap(NotifyForNotice::getNoticeId, item -> item));
            if (!CollectionUtils.isEmpty(noticeIds)) {
                PageHelper.startPage(pageNum, pageSize);
                List<Notice> notices = this.noticeMapper.searchNotices(noticeType, title, sendUserName, noticeIds);

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
                        noticeModel.setSendTime(notifyForNotice.getSendTime());
                    }
                    page.add(noticeModel);
                }
                return page;
            } else {
                return new Page<>();
            }
        }
    }

    public void receive(Integer noticeId) {
        signForNoticeService.receive(noticeId);
    }

    public List<Notice> getNoticeByIds(Set<Integer> noticeIds) {
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(Notice::getId, noticeIds);
        return this.noticeMapper.selectList(queryWrapper);
    }
}
