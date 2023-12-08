package com.huajie.domain.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author zhuxiaofeng
 * @date 2023/9/11
 */
@Slf4j
public class DateUtil {

    /**
     * 获取当前时间
     * @return
     */
    public static Date getCurrentDate(){
        // 获取当前日期
        LocalDate currentDate = LocalDate.now();
        // 将 LocalDate 转换为 Instant
        Instant instant = currentDate.atStartOfDay().atZone(java.time.ZoneId.systemDefault()).toInstant();
        // 将 Instant 转换为 Date
        return Date.from(instant);
    }

    /**
     * 当前时间加上几天
     * @param date
     * @param days
     * @return
     */
    public static Date addDays(Date date, int days){
        // 将 Date 转换为 Instant
        Instant instant = date.toInstant();

        // 将 Instant 转换为 LocalDate
        LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();

        // 将日期加上一年
        LocalDate newDate = localDate.plusDays(days);

        // 将 LocalDate 转换为 Instant
        instant = newDate.atStartOfDay().atZone(java.time.ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    /**
     * 当前时间加上几年
     * @param date
     * @param years
     * @return
     */
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

    public static Period dateSubtract(Date minuend,Date subtrahend){
        LocalDate minuendLocalDate = minuend.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        LocalDate subtrahendLocalDate = subtrahend.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // 计算两个日期之间的差距
        Period period = Period.between(minuendLocalDate, subtrahendLocalDate);
        // 打印差距中的年、月、日部分
        log.debug("相差 " + period.getYears() + " 年, " + period.getMonths() + " 个月, " + period.getDays() + " 天");
        return period;
    }

    public static long timeSubtracct(Date minuend,Date subtrahend){
        // 创建两个 LocalDateTime 对象
        LocalDateTime minuendDateTime = minuend.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime subtrahendDateTime = subtrahend.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        // 计算两个日期时间之间的差距
        Duration duration = Duration.between(minuendDateTime, subtrahendDateTime);

        // 获取分钟差距
        long minutesDifference = duration.toMinutes();

        // 打印分钟差距
        log.debug("相差 " + minutesDifference + " 分钟");
        return Math.abs(minutesDifference);
    }

    public static Date getCurrentMonthStartDate(){
        LocalDate now = LocalDate.now();
        LocalDate start = LocalDate.of(now.getYear(), now.getMonth(), 1);
        Instant instant = start.atStartOfDay().atZone(java.time.ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    public static Date getLastWeekDate(){
        LocalDate now = LocalDate.now();
        LocalDate lastWeekDate = now.minusDays(7);
        Instant instant = lastWeekDate.atStartOfDay().atZone(java.time.ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

}
