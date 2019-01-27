package com.linlazy.mmorpg.module.item.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.linlazy.mmorpg.module.backpack.service.PlayerBackpackService;
import com.linlazy.mmorpg.module.item.domain.ItemContext;
import com.linlazy.mmorpg.module.player.domain.PlayerBackpack;
import com.linlazy.mmorpg.module.common.reward.Reward;
import com.linlazy.mmorpg.module.common.reward.RewardService;
import com.linlazy.mmorpg.file.service.ItemConfigService;
import com.linlazy.mmorpg.server.common.Result;
import com.linlazy.mmorpg.utils.ItemIdUtil;
import com.linlazy.mmorpg.utils.RewardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 道具服务类
 * @author linlazy
 */
@Component
public class ItemService {


    @Autowired
    private PlayerBackpackService playerBackpackService;

    @Autowired
    private RewardService rewardService;

    @Autowired
    private ItemConfigService itemConfigService;

    /**
     * 消耗道具
     * @param actorId 玩家ID
     * @param itemId 道具ID
     * @return
     */
    public Result<?> consumeItem(long actorId, long itemId) {

        ItemContext itemContext = new ItemContext(itemId);
        itemContext.setCount(1);

        Result<?> enough = playerBackpackService.isEnough(actorId, Lists.newArrayList(itemContext));
        if(enough.isFail()){
            return Result.valueOf(enough.getCode());
        }
        PlayerBackpack playerBackpack = playerBackpackService.getPlayerBackpack(actorId);
        playerBackpack.pop(Lists.newArrayList(itemContext));

        JSONObject itemConfig = itemConfigService.getItemConfig(ItemIdUtil.getBaseItemId(itemId));
        String rewards = itemConfig.getString("rewards");
        List<Reward> rewardList = RewardUtils.parseRewards(rewards);
        rewardService.addRewardList(actorId,rewardList);

        return Result.success();
    }
}
