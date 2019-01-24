package com.linlazy.mmorpg.shop.count.reset;

import com.linlazy.mmorpg.domain.Shop;
import com.linlazy.mmorpg.server.common.Result;
import com.linlazy.mmorpg.utils.DateUtils;

import java.time.DayOfWeek;

/**
 * @author linlazy
 */
public class WeekResetCount extends BaseResetCount {
    @Override
    protected Integer resetType() {
        return ResetType.WEEK;
    }

    @Override
    public Result<?> doReset(Shop shop) {
        if(shop.getNextResetTime() < DateUtils.getNowMillis()){
            shop.setHasBuyCount(0);
            shop.setNextResetTime(DateUtils.nextWeek(DayOfWeek.SATURDAY));
        }

        return Result.success();
    }
}
