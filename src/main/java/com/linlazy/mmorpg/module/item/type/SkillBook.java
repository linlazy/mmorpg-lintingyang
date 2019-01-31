package com.linlazy.mmorpg.module.item.type;

import com.linlazy.mmorpg.module.item.domain.Item;
import com.linlazy.mmorpg.server.common.Result;

/**
 *
 * 技能书籍：在游戏中能让玩家学习到技能的道具，招式技能书
 * @author linlazy
 */
public class SkillBook extends BaseItem{
    @Override
    protected Integer itemType() {
        return ItemType.SKILL;
    }

    @Override
    public Result<?> useItem(long actorId, Item item) {
        return null;
    }
}
