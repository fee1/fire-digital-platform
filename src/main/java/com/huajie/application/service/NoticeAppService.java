package com.huajie.application.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.huajie.application.api.request.CreateNoticeRequestVO;
import com.huajie.application.api.request.EditNoticeRequestVO;
import com.huajie.application.api.response.EntPcNoticeResponseVO;
import com.huajie.application.api.response.NoticeResponseVO;
import com.huajie.application.api.response.NoticeAppDetailResponseVO;
import com.huajie.application.api.response.NoticeDetailResponseVO;
import com.huajie.application.api.response.SearchNoticeResponseVO;
import com.huajie.domain.common.utils.UserContext;
import com.huajie.domain.entity.Notice;
import com.huajie.domain.entity.SignForNotice;
import com.huajie.domain.entity.Tenant;
import com.huajie.domain.entity.User;
import com.huajie.domain.model.NoticeModel;
import com.huajie.domain.service.NoticeService;
import com.huajie.domain.service.RoleService;
import com.huajie.domain.service.SignForNoticeService;
import com.huajie.domain.service.TenantService;
import com.huajie.domain.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhuxiaofeng
 * @date 2023/10/22
 */
@Service
public class NoticeAppService {

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private SignForNoticeService signForNoticeService;

    @Autowired
    private TenantService tenantService;

    @Autowired
    private UserService userService;

    public void createNotice(CreateNoticeRequestVO requestVO) {
        Notice notice = new Notice();
        BeanUtils.copyProperties(requestVO, notice);
        notice.setType(requestVO.getType().byteValue());
        notice.setReceiveType(requestVO.getReceiveType().byteValue());
        notice.setSpecifyRange(requestVO.getSpecifyRange().byteValue());
        notice.setTenantIds(JSONObject.toJSONString(requestVO.getTenantIds()));
        if (requestVO.getAppendix().size() != 0){
            notice.setAppendix(JSONObject.toJSONString(requestVO.getAppendix()));
        }

        notice.setSaveDays(requestVO.getSaveDays());
        noticeService.createNotice(notice);
    }

    public Page<SearchNoticeResponseVO> searchNotice(String title, Integer pageNum, Integer pageSize) {
        Page<Notice> page = noticeService.searchNotice(title, pageNum, pageSize);
        Page<SearchNoticeResponseVO> responseVOPage = new Page<>();
        BeanUtils.copyProperties(page, responseVOPage);
//        List<Role> allRole = roleService.getAllRole();
//        Map<Integer, Role> id2Role = allRole.stream().collect(Collectors.toMap(Role::getId, item -> item));
        for (Notice notice : page) {
            SearchNoticeResponseVO searchNoticeResponseVO = new SearchNoticeResponseVO();
            BeanUtils.copyProperties(notice, searchNoticeResponseVO);
            searchNoticeResponseVO.setStatus(notice.getStatus().intValue());
            searchNoticeResponseVO.setReceiveType(notice.getReceiveType().intValue());
            searchNoticeResponseVO.setSpecifyRange(notice.getSpecifyRange().intValue());
            searchNoticeResponseVO.setType(notice.getType().intValue());
            responseVOPage.add(searchNoticeResponseVO);
//            Role roleById = id2Role.get(notice.getId());
//            searchNoticeResponseVO.setRoleName(roleById.getRoleName());
            Double signInRate = signForNoticeService.getSignRateByNoticeId(notice.getId());
            searchNoticeResponseVO.setSignInRate(signInRate);
            searchNoticeResponseVO.setExistAppendix(StringUtils.isNotBlank(notice.getAppendix()));
        }
        return responseVOPage;
    }

    public void publicNotice(Integer id) {
        noticeService.publicNotice(id);
    }

    public void editNotice(EditNoticeRequestVO requestVO) {
        Notice notice = new Notice();
        BeanUtils.copyProperties(requestVO, notice);
        notice.setId(requestVO.getNoticeId());
        notice.setType(requestVO.getType().byteValue());
        notice.setReceiveType(requestVO.getReceiveType().byteValue());
        notice.setSpecifyRange(requestVO.getSpecifyRange().byteValue());
        notice.setTenantIds(JSONObject.toJSONString(requestVO.getTenantIds()));
        notice.setAppendix(JSONObject.toJSONString(requestVO.getAppendix()));
        notice.setSaveDays(requestVO.getSaveDays());

        noticeService.editNotice(notice);
    }

    public NoticeDetailResponseVO detailNotice(Integer noticeId) {
        Notice notice = this.noticeService.detailNotice(noticeId);

        NoticeDetailResponseVO noticeDetailResponseVO = new NoticeDetailResponseVO();
        BeanUtils.copyProperties(notice, noticeDetailResponseVO);
        return noticeDetailResponseVO;
    }

    public Page<NoticeResponseVO> getNoticeList(Integer noticeType, Date startDate, Date endDate, String title, String sendUserName, Integer pageNum, Integer pageSize) {
        Page<NoticeModel> govPcNoticeList = this.noticeService.getNoticeList(noticeType, startDate, endDate, title, sendUserName, pageNum, pageSize);
        if (CollectionUtils.isEmpty(govPcNoticeList)){
            return new Page<>();
        }
        Page<NoticeResponseVO> responseVOPage = new Page<>();
        BeanUtils.copyProperties(govPcNoticeList, responseVOPage);
        List<Integer> tenantIds = govPcNoticeList.stream().map(Notice::getFromTenantId).collect(Collectors.toList());
        Map<Integer, String> id2TenantNameMap = this.tenantService.getTenantNameMap(tenantIds);
        for (NoticeModel notice : govPcNoticeList) {
            NoticeResponseVO pcNoticeResponseVO = new NoticeResponseVO();
            BeanUtils.copyProperties(notice, pcNoticeResponseVO);
            pcNoticeResponseVO.setFromTenantName(id2TenantNameMap.get(notice.getFromTenantId()));
            pcNoticeResponseVO.setExistAppendix(StringUtils.isNotBlank(notice.getAppendix()));
            pcNoticeResponseVO.setStatus(notice.getSignStatus());
            responseVOPage.add(pcNoticeResponseVO);
        }
        return responseVOPage;
    }

    public Page<EntPcNoticeResponseVO> getEntPcNoticeList(Integer noticeType, Date startDate, Date endDate,String title, String sendUserName, Integer pageNum, Integer pageSize) {
        Page<Notice> entPcNoticeList = this.noticeService.getEntPcNoticeList(noticeType, startDate, endDate, title, sendUserName, pageNum, pageSize);
        Page<EntPcNoticeResponseVO> responseVOPage = new Page<>();
        BeanUtils.copyProperties(entPcNoticeList, responseVOPage);
        List<Integer> tenantIds = entPcNoticeList.stream().map(Notice::getFromTenantId).collect(Collectors.toList());
        Map<Integer, String> id2TenantNameMap = this.tenantService.getTenantNameMap(tenantIds);
        for (Notice notice : entPcNoticeList) {
            EntPcNoticeResponseVO entPcNoticeResponseVO = new EntPcNoticeResponseVO();
            BeanUtils.copyProperties(notice, entPcNoticeResponseVO);
            entPcNoticeResponseVO.setFromTenantName(id2TenantNameMap.get(notice.getFromTenantId()));
            entPcNoticeResponseVO.setExistAppendix(StringUtils.isNotBlank(notice.getAppendix()));
            responseVOPage.add(entPcNoticeResponseVO);
        }
        return responseVOPage;
    }

    public void receive(Integer noticeId) {
        this.noticeService.receive(noticeId);
    }

    public NoticeAppDetailResponseVO appDetail(Integer noticeId) {
        Notice notice = this.noticeService.detailNotice(noticeId);
        NoticeAppDetailResponseVO noticeAppDetailResponseVO = new NoticeAppDetailResponseVO();
        BeanUtils.copyProperties(notice, noticeAppDetailResponseVO);
        Tenant tenant = this.tenantService.getTenantByTenantId(notice.getFromTenantId());
        noticeAppDetailResponseVO.setFromTenantName(tenant.getTenantName());
        noticeAppDetailResponseVO.setAddress(tenant.getAddress());
        SignForNotice signForNotice = this.signForNoticeService.getSignForNoticeIdAndUserId(notice.getId(), UserContext.getCurrentUserId());
        noticeAppDetailResponseVO.setStatus(signForNotice.getSignStatus().intValue());
        return noticeAppDetailResponseVO;
    }
}
