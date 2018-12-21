package com.linlazy.mmorpglintingyang.utils;

import java.time.Instant;

public class DateUtils {

    /**
     * 获取当前时间
     * @return
     */
    public static long getNowMillis(){
        return Instant.now().toEpochMilli();
    }
}