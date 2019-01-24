package com.linlazy.mmorpg.shop.count.reset;

import com.linlazy.mmorpg.domain.Shop;
import com.linlazy.mmorpg.server.common.Result;
import com.linlazy.mmorpg.utils.DateUtils;

/**
 * @author linlazy
 */
public class DayResetCount extends BaseResetCount {
    @Override
    protected Integer resetType() {
        return ResetType.DAY;
    }

    @Override
    public Result<?> doReset(Shop shop) {
        if(shop.getNextResetTime() < DateUtils.getNowMillis()){
            shop.setHasBuyCount(0);
            shop.setNextResetTime(DateUtils.getTomorrowMillis());
        }

        return Result.success();
    }
}
