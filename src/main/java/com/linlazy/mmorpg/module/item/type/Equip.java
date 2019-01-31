package com.linlazy.mmorpg.module.item.type;

import com.linlazy.mmorpg.module.item.domain.Item;
import com.linlazy.mmorpg.server.common.Result;

/**
 * 玩家可以在商店购买基本的装备或者通过任务、杀怪等形式获得其它更高级的装备。装备都有等级的限制。玩家使用武器及被攻击后，装备的耐久度会下降，当耐久降为0后，这件装备无法使用。
 * @author linlazy
 */
public class Equip extends BaseItem{
    @Override
    protected Integer itemType() {
        return ItemType.EQUIP;
    }

    @Override
    public Result<?> useItem(long actorId, Item item) {
        return Result.valueOf("普通道具不能被使用");
    }
}
