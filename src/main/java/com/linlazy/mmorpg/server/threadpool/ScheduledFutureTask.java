package com.linlazy.mmorpg.server.threadpool;

import lombok.Data;

/**
 * @author linlazy
 */
@Data
public class ScheduledFutureTask implements Runnable {


    private long deadlineNanos;

    @Override
    public void run() {

    }
}
