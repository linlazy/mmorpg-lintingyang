package com.linlazy.mmorpglintingyang.module.item.service;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.common.Result;
import com.linlazy.mmorpglintingyang.module.reward.Reward;
import com.linlazy.mmorpglintingyang.module.reward.RewardService;
import com.linlazy.mmorpglintingyang.module.item.manager.ItemManager;
import com.linlazy.mmorpglintingyang.module.item.manager.config.ItemConfigService;
import com.linlazy.mmorpglintingyang.module.item.manager.dao.ItemDao;
import com.linlazy.mmorpglintingyang.module.item.manager.entity.Item;
import com.linlazy.mmorpglintingyang.utils.ItemIdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
     * @param consumeNum
     * @return
     */
    public Result<?> useItem(long actorId, long itemId,int consumeNum) {
        int baseItemId = ItemIdUtil.getBaseItemId(itemId);

        int count = itemManager.getSuperPositionTotal(actorId,baseItemId);
        if(count <consumeNum){
            return Result.valueOf("道具不足");
        }
        //扣除消耗
        itemManager.consumeBackPackItem(actorId,ItemIdUtil.getBaseItemId(itemId),consumeNum);
        //获取使用道具获得的奖励列表
        List<Reward> rewardList = itemManager.getRewards(baseItemId);
        //发奖励
        rewardService.addRewardList(actorId,rewardList);
        return Result.success();
    }

    /**
     * 获取玩家道具信息
     * @param actorId
     * @return
     */
    public Result<?> getActorItemInfo(long actorId) {
        List<JSONObject> result = new ArrayList<>();

        Item[] actorBackPack = itemManager.getActorBackPack(actorId);
        for(int backPackIndex = 0; backPackIndex < actorBackPack.length; backPackIndex++){
            if(actorBackPack[backPackIndex] != null){
                Item item = actorBackPack[backPackIndex];
                long itemId = item.getItemId();
                int count = item.getCount();

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("itemId",itemId);
                jsonObject.put("count",count);
                jsonObject.put("backPackIndex",backPackIndex);

                result.add(jsonObject);

            }

        }

        return Result.success(result);
    }

    /**
     * 获得道具
     * @param actorId
     * @param baseItemId
     * @param num
     * @return
     */
    public Result<?> addItem(long actorId, int baseItemId, int num) {

        //是否背包已满
        if(itemManager.isFullBackPack(actorId,baseItemId,num)){
            return Result.valueOf("背包已满");
        }
        itemManager.addItem(actorId,baseItemId,num);
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
