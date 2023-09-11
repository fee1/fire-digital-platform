package com.huajie.domain.common.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author zhuxiaofeng
 * @date 2023/9/11
 */
public class LocalDateUtil {

    public static Date getCurrentDate(){
        // 获取当前日期
        LocalDate currentDate = LocalDate.now();
        // 将 LocalDate 转换为 Instant
        Instant instant = currentDate.atStartOfDay().atZone(java.time.ZoneId.systemDefault()).toInstant();
        // 将 Instant 转换为 Date
        return Date.from(instant);
    }

    public static Date addYears(Date date, int years){
        // 将 Date 转换为 Instant
        Instant instant = date.toInstant();

        // 将 Instant 转换为 LocalDate
        LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();

        // 将日期加上一年
        LocalDate newDate = localDate.plusYears(years);

        // 将 LocalDate 转换为 Instant
        instant = newDate.atStartOfDay().atZone(java.time.ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }



}
