package com.huajie.application.service;

import com.huajie.application.api.response.SignResponseVO;
import com.huajie.domain.service.CommonService;
import com.huajie.infrastructure.external.oss.model.SignModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhuxiaofeng
 * @date 2023/10/16
 */
@Service
public class FileUploadAppService {

    @Autowired
    private CommonService commonService;

    /**
     * https://codeleading.com/article/44635417825/
     *
     * @return
     */
    public SignResponseVO getSign() {
        SignModel sign = commonService.getSign();
        SignResponseVO signResponseVO = new SignResponseVO();
        BeanUtils.copyProperties(sign, signResponseVO);
        return signResponseVO;
    }

}
