package com.linlazy.mmorpglintingyang;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeTest {
    public static void main(String[] args) {
        String dateTime = "2018-09-11 03:22:22";
        DateTimeFormatter dateTimeFormatter =DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalTime beginTime = LocalTime.parse(dateTime, dateTimeFormatter);
        System.out.println(beginTime.toString());
    }
}
