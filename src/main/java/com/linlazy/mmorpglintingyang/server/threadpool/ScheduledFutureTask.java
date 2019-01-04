package com.linlazy.mmorpglintingyang.server.threadpool;

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
