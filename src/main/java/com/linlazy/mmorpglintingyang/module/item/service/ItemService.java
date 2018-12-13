package com.linlazy.mmorpglintingyang.module.item.service;

import com.linlazy.mmorpglintingyang.module.common.Result;
import com.linlazy.mmorpglintingyang.module.common.RewardService;
import com.linlazy.mmorpglintingyang.module.item.manager.ItemManager;
import com.linlazy.mmorpglintingyang.module.item.manager.config.ItemConfigService;
import com.linlazy.mmorpglintingyang.module.item.manager.dao.ItemDao;
import com.linlazy.mmorpglintingyang.module.item.manager.entity.Item;
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

    @Autowired
    private ItemManager itemManager;

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

    /**
     * 获取玩家道具信息
     * @param actorId
     * @return
     */
    public Result<?> getActorItemInfo(long actorId) {
        return Result.success(itemManager.getItemInfo(actorId));
    }

    /**
     * 获得道具
     * @param actorId
     * @param itemId
     * @param num
     * @return
     */
    public Result<?> addItem(long actorId, int itemId, int num) {
        //是否背包已满，道具不存在或已到达叠加数，且没有多余格子
        if(itemManager.isFullPackage(actorId,itemId,num)){
            return Result.valueOf("背包已满");
        }

        return Result.success();
    }

    /**
     * 整理背包
     * @param actorId
     * @return
     */
    public Result<?> arrangeBackPack(long actorId) {
        itemManager.arrangeBackPack(actorId);
        return Result.success();
    }
}
