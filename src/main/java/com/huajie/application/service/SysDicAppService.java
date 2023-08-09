package com.huajie.application.service;

import com.github.pagehelper.Page;
import com.huajie.application.api.request.DicAddRequestVO;
import com.huajie.application.api.request.DicValueAddRequestVO;
import com.huajie.application.api.response.DicResponseVO;
import com.huajie.application.api.response.DicValueResponseVO;
import com.huajie.domain.entity.SysDic;
import com.huajie.domain.entity.SysDicValue;
import com.huajie.domain.model.DicValueAddDTO;
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

    public Page<DicResponseVO> getPageDicList(String dicCode, String description, Integer pageNum, Integer pageSize) {
        Page<SysDic> sysDics = sysDicService.getDicList(dicCode, description, pageNum, pageSize);
        Page<DicResponseVO> dicResponseVOList = new Page<>();
        BeanUtils.copyProperties(sysDics, dicResponseVOList);
        for (SysDic sysDic : sysDics) {
            DicResponseVO dicResponseVO = new DicResponseVO();
            BeanUtils.copyProperties(sysDic, dicResponseVO);
            dicResponseVOList.add(dicResponseVO);
        }
        return dicResponseVOList;
    }

    public void addDic(DicAddRequestVO requestVO) {
        sysDicService.addDic(requestVO.getDicCode(), requestVO.getDicName(), requestVO.getDescription());
    }

    public List<DicValueResponseVO> getDicValueList(String dicCode) {
        List<SysDicValue> sysDicValueList = sysDicService.getDicValueList(dicCode);
        List<DicValueResponseVO> dicValueResponseVOList = new ArrayList<>();
        for (SysDicValue sysDicValue : sysDicValueList) {
            DicValueResponseVO dicValueResponseVO = new DicValueResponseVO();
            BeanUtils.copyProperties(sysDicValue, dicValueResponseVO);
            dicValueResponseVOList.add(dicValueResponseVO);
        }
        return dicValueResponseVOList;
    }

    public void addDicValue(DicValueAddRequestVO requestVO) {
        DicValueAddDTO dicValueAddDTO = new DicValueAddDTO();
        BeanUtils.copyProperties(requestVO, dicValueAddDTO);
        sysDicService.addDicValue(dicValueAddDTO);
    }
}
