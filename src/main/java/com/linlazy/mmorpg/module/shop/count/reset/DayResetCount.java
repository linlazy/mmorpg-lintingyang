package com.linlazy.mmorpg.module.shop.count.reset;

import com.linlazy.mmorpg.module.shop.domain.Shop;
import com.linlazy.mmorpg.server.common.Result;
import com.linlazy.mmorpg.utils.DateUtils;
import org.springframework.stereotype.Component;

/**
 * @author linlazy
 */
@Component
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
            shopDAO.updateQueue(shop.convertEntity());
        }

        return Result.success();
    }
}
