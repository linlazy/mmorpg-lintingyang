package com.linlazy.mmorpglintingyang.module.item.service;

import com.linlazy.mmorpglintingyang.module.common.Result;
import com.linlazy.mmorpglintingyang.module.common.RewardService;
import com.linlazy.mmorpglintingyang.module.item.config.ItemConfigService;
import com.linlazy.mmorpglintingyang.module.item.dao.ItemDao;
import com.linlazy.mmorpglintingyang.module.item.entity.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemService {

    @Autowired
    private ItemConfigService itemConfigService;
    @Autowired
    private RewardService rewardService;
    @Autowired
    private ItemDao itemDao;

    public Result<?> useItem(long actorId, int itemId,int consumeNum) {
        Item item = itemDao.getItem(actorId, itemId);
        if(item == null || item.getCount() < consumeNum){
            Result.valueOf("道具不足");
        }


        itemConfigService.getItemConfig(itemId);
        //发奖励
//
//        Reward reward = new Reward();
//        rewardService.addReward(reward);

        //存档
        item.setCount(item.getCount() -consumeNum);
        itemDao.updateItem(item);
        return Result.success();
    }
}
