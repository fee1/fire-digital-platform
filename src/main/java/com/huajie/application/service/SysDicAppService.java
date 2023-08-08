package com.huajie.application.service;

import com.huajie.application.api.response.DicResponseVO;
import com.huajie.domain.entity.SysDic;
import com.huajie.domain.service.SysDicService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhuxiaofeng
 * @date 2023/8/8
 */
@Service
public class SysDicAppService {

    @Autowired
    private SysDicService sysDicService;

    public List<DicResponseVO> getDicList(String dicCode, String description) {
        List<SysDic> sysDics = sysDicService.getDicList(dicCode, description);
        List<DicResponseVO> dicResponseVOList = new ArrayList<>();
        for (SysDic sysDic : sysDics) {
            DicResponseVO dicResponseVO = new DicResponseVO();
            BeanUtils.copyProperties(sysDic, dicResponseVO);
            dicResponseVOList.add(dicResponseVO);
        }
        return dicResponseVOList;
    }
}