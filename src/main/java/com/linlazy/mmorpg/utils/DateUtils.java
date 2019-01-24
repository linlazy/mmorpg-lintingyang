package com.linlazy.mmorpg.utils;

import java.time.*;
import java.time.temporal.TemporalAdjusters;

/**
 * @author linlazy
 */
public class DateUtils {


    public static void main(String[] args) {
//        nextWeek(DayOfWeek.SATURDAY);
        long tomorrowMillis = getTomorrowMillis();
        System.out.println(tomorrowMillis);
    }

    /**
     * 获取当前时间
     * @return
     */
    public static long getNowMillis(){
        return Instant.now().toEpochMilli();
    }


    /**
     * 获取明天零点时间
     * @return
     */
    public static long getTomorrowMillis(){
        return ZonedDateTime.of(LocalDateTime.of(LocalDate.now(), LocalTime.MIN), ZoneOffset.systemDefault())
                .plusDays(1)
                .toInstant().toEpochMilli();
    }

    /**
     * 下周零点
     * @param dayOfWeek
     * @return
     */
    public static long nextWeek(DayOfWeek dayOfWeek){
        return ZonedDateTime.of(LocalDateTime.of(LocalDate.now(), LocalTime.MIN), ZoneOffset.systemDefault())
                .with(TemporalAdjusters.next(dayOfWeek))
                .toInstant().toEpochMilli();
    }

}
