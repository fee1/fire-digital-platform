package com.huajie.application.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.huajie.application.api.request.CreateNoticeRequestVO;
import com.huajie.application.api.request.EditNoticeRequestVO;
import com.huajie.application.api.response.SearchNoticeResponseVO;
import com.huajie.domain.common.constants.NoticeStatusConstants;
import com.huajie.domain.common.utils.DateUtil;
import com.huajie.domain.entity.Notice;
import com.huajie.domain.entity.Role;
import com.huajie.domain.service.NoticeService;
import com.huajie.domain.service.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Period;
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

    public void createNotice(CreateNoticeRequestVO requestVO) {
        Notice notice = new Notice();
        BeanUtils.copyProperties(requestVO, notice);
        notice.setType(requestVO.getType().byteValue());
        notice.setReceiveType(requestVO.getReceiveType().byteValue());
        notice.setSpecifyRange(requestVO.getSpecifyRange().byteValue());
        notice.setTenantIds(JSONObject.toJSONString(requestVO.getTenantIds()));
        notice.setAppendix(JSONObject.toJSONString(requestVO.getAppendix()));

        notice.setSaveDays(requestVO.getSaveDays());
        noticeService.createNotice(notice);
    }

    public Page<SearchNoticeResponseVO> searchNotice(String title, Integer pageNum, Integer pageSize) {
        Page<Notice> page = noticeService.searchNotice(title, pageNum, pageSize);
        Page<SearchNoticeResponseVO> responseVOPage = new Page<>();
        BeanUtils.copyProperties(page, responseVOPage);
        List<Role> allRole = roleService.getAllRole();
        Map<Integer, Role> id2Role = allRole.stream().collect(Collectors.toMap(Role::getId, item -> item));
        for (Notice notice : page) {
            SearchNoticeResponseVO searchNoticeResponseVO = new SearchNoticeResponseVO();
            BeanUtils.copyProperties(notice, searchNoticeResponseVO);
            responseVOPage.add(searchNoticeResponseVO);
            Role roleById = id2Role.get(notice.getId());
            searchNoticeResponseVO.setRoleName(roleById.getRoleName());
            //todo 签收率
//            searchNoticeResponseVO.setSignInRate();
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
}
