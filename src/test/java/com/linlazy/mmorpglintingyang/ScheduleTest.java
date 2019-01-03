package com.linlazy.mmorpglintingyang;

import java.util.concurrent.*;

public class ScheduleTest {

    private static int count = 0 ;

    public static void main(String[] args) {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(20);
        scheduledExecutorService.scheduleAtFixedRate(()->{
            System.out.println("aaa");
        },2L,2L,TimeUnit.SECONDS);
    }
}
