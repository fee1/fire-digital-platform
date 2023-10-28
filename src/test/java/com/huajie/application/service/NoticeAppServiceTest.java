package com.huajie.application.service;

import com.huajie.BaseTest;
import com.huajie.application.api.request.CreateNoticeRequestVO;
import com.huajie.domain.common.constants.NoticeReceiveTypeConstants;
import com.huajie.domain.common.constants.NoticeTypeConstants;
import com.huajie.domain.common.constants.SpecifyRangeConstants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

/**
 * @author zhuxiaofeng
 * @date 2023/10/22
 */
class NoticeAppServiceTest extends BaseTest {

    @Autowired
    private NoticeAppService noticeAppService;

    @Test
    public void createNotice() {
        CreateNoticeRequestVO createNoticeRequestVO = new CreateNoticeRequestVO();
        createNoticeRequestVO.setType(NoticeTypeConstants.NOTIFY);
        createNoticeRequestVO.setReceiveType(NoticeReceiveTypeConstants.ENTERPRISE);
        createNoticeRequestVO.setRoleId(1);
        createNoticeRequestVO.setSpecifyRange(SpecifyRangeConstants.ALL);
        createNoticeRequestVO.setTenantIds(Arrays.asList(1));
        createNoticeRequestVO.setTitle("test");
        createNoticeRequestVO.setText("tets");
        createNoticeRequestVO.setSaveDays(10);
        noticeAppService.createNotice(createNoticeRequestVO);
    }
}