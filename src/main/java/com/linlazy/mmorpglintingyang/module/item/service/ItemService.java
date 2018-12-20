package com.linlazy.mmorpglintingyang.module.item.service;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.item.manager.backpack.response.BackPackInfo;
import com.linlazy.mmorpglintingyang.module.common.reward.RewardService;
import com.linlazy.mmorpglintingyang.module.item.manager.ItemManager;
import com.linlazy.mmorpglintingyang.module.item.manager.config.ItemConfigService;
import com.linlazy.mmorpglintingyang.module.item.manager.dao.ItemDao;
import com.linlazy.mmorpglintingyang.module.item.service.strategy.UseItemStrategy;
import com.linlazy.mmorpglintingyang.server.common.Result;
import com.linlazy.mmorpglintingyang.utils.ItemIdUtil;
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

    /**
     * 使用道具
     * @param actorId
     * @param itemId
     * @param jsonObject
     * @return
     */
    public Result<?> useItem(long actorId,long itemId,JSONObject jsonObject) {
        int baseItemId = ItemIdUtil.getBaseItemId(itemId);
        JSONObject itemConfig = itemConfigService.getItemConfig(baseItemId);
        UseItemStrategy useItemStrategy = UseItemStrategy.newUseItemStrategy(itemConfig.getIntValue("useItem"));
        return useItemStrategy.doUseItem(actorId,itemId,jsonObject);
    }

    /**
     * 获取玩家背包信息
     * @param actorId
     * @return
     */
    public Result<?> getActorItemInfo(long actorId) {

        BackPackInfo actorBackPack = itemManager.getActorBackPack(actorId);

        return Result.success(actorBackPack);
    }

    /**
     * 增加道具
     * @param actorId
     * @param baseItemId
     * @param num
     * @return 返回增加道具后，背包变化的信息
     */
    public Result<?> pushBackPack(long actorId, int baseItemId, int num) {
        //是否背包已满
        if(itemManager.isFullBackPack(actorId,baseItemId,num)){
            return Result.valueOf("背包已满");
        }

        return itemManager.pushBackPack(actorId,baseItemId,num);
    }

    /**
     * 整理背包
     * @param actorId
     * @return
     */
    public Result<BackPackInfo> arrangeBackPack(long actorId) {
        BackPackInfo backPackInfo = itemManager.arrangeBackPack(actorId);
        return Result.success(backPackInfo);
    }


    /**
     * 消耗背包道具
     * @param actorId
     * @param itemId
     * @param consumeNum
     */
    public Result<?> consumeBackPackItem(long actorId,long itemId,int consumeNum) {
        long count = itemManager.getItemTotal(actorId, itemId);
        if(count <consumeNum){
            return Result.valueOf("道具不足");
        }
        return itemManager.consumeBackPackItem(actorId, itemId, consumeNum);
    }
}
