package com.linlazy.mmorpg.server.threadpool;

import com.linlazy.mmorpg.server.common.Result;
import lombok.Data;

import java.util.concurrent.Callable;

/**
 * @author linlazy
 */
@Data
public  abstract class BusinessTask implements Callable<Result<?>> {

    protected long identity;

    public BusinessTask(long identity) {
        this.identity = identity;
    }
}
