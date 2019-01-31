package com.linlazy.mmorpg.module.item.type;

import com.linlazy.mmorpg.module.item.domain.Item;
import com.linlazy.mmorpg.server.common.Result;

/**
 * 普通道具
 * 玩家平时击败怪物后掉落，不能使用。这类道具可用作两方面用途：可在千货商处贩卖获得金钱，还可在游戏中作为完成任务需要上交的任务道具。
 * @author linlazy
 */
public class OrdinaryItem extends BaseItem{
    @Override
    protected Integer itemType() {
        return ItemType.ORDINARY;
    }

    @Override
    public Result<?> useItem(long actorId, Item item) {
        return Result.valueOf("普通道具不能被使用");
    }

}
