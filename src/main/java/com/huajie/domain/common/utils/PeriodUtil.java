package com.huajie.domain.common.utils;

import com.huajie.domain.common.enums.EnterpriseFireTypeEnum;
import com.huajie.domain.common.enums.EnterpriseTypeEnum;
import com.huajie.domain.model.PeriodDTO;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

public class PeriodUtil {

    public static LocalDateTime getTodayStartTime(){
        LocalDateTime now = LocalDateTime.now();
        return LocalDateTime.of(now.getYear(),now.getMonth(), now.getDayOfMonth(), 0,0,0);
    }

    public static LocalDateTime getTodayEndTime(){
        LocalDateTime now = LocalDateTime.now();
        return LocalDateTime.of(now.getYear(),now.getMonth(), now.getDayOfMonth(), 0,0,0).plusDays(1).minusSeconds(1);
    }

    public static LocalDateTime getLast15Days(){
        LocalDateTime time = LocalDateTime.now().minusDays(15);
        return LocalDateTime.of(time.getYear(),time.getMonth(),time.getDayOfMonth(),0,0,0);
    }

    public static PeriodDTO getPeriodByEnterprise(String enterpriseType, String enterpriseFireType){
        if (EnterpriseTypeEnum.Company.getCode().equals(enterpriseType)
                && !EnterpriseFireTypeEnum.ImportantFirePlace.getCode().equals(enterpriseFireType)) {
            // 非消防重点单位的 机关团体事业单位 按季度检查
           return getCurrentQuarterPeriod();
        }else{
            // 其余 均按月度检查
            return getCurrentMonthPeriod();
        }
    }

    /**
     * 获取当前月度期间
     * @return
     */
    public static PeriodDTO getCurrentMonthPeriod(){
        PeriodDTO periodDTO = new PeriodDTO();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = LocalDateTime.of(now.getYear(), now.getMonth(), 1, 0, 0, 0);
        periodDTO.setStartDateTime(start);
        periodDTO.setEndDateTime(start.plusMonths(1).minusSeconds(1));
        return periodDTO;
    }

    /**
     * 获取当前季度期间
     * @return
     */
    public static PeriodDTO getCurrentQuarterPeriod(){
        PeriodDTO periodDTO = new PeriodDTO();

        LocalDateTime now = LocalDateTime.now();
        int quarter = (now.getMonthValue()+2) / 3;
        int month = (quarter - 1) * 3 + 1;

        LocalDateTime start = LocalDateTime.of(now.getYear(), month, 1, 0, 0, 0);

        LocalDateTime end = LocalDateTime.of(now.getYear(), month+3, 1, 0, 0, 0);
        periodDTO.setStartDateTime(start);
        periodDTO.setEndDateTime(end.minusSeconds(1));
        return periodDTO;
    }

}
