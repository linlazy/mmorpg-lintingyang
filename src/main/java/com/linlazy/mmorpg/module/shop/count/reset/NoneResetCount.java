package com.linlazy.mmorpg.module.shop.count.reset;

import com.linlazy.mmorpg.domain.Shop;
import com.linlazy.mmorpg.server.common.Result;
import org.springframework.stereotype.Component;

/**
 * @author linlazy
 */
@Component
public class NoneResetCount extends BaseResetCount {
    @Override
    protected Integer resetType() {
        return ResetType.NONE;
    }

    @Override
    public Result<?> doReset(Shop shop) {
        return Result.success();
    }
}
