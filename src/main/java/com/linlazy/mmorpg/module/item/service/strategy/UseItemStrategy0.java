//package com.linlazy.mmorpg.module.item.service.strategy;
//
//import com.alibaba.fastjson.JSONObject;
//import com.linlazy.mmorpg.module.common.reward.Reward;
//import com.linlazy.mmorpg.module.common.reward.RewardService;
//import com.linlazy.mmorpg.module.item.manager.ItemManager;
//import com.linlazy.mmorpg.module.item.manager.backpack.response.BackPackInfo;
//import com.linlazy.mmorpg.server.common.Result;
//import com.linlazy.mmorpg.utils.ItemIdUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
///**
// * @author linlazy
// */
//@Component
//public class UseItemStrategy0 extends BaseUseItemStrategy {
//
//    @Autowired
//    private ItemManager itemManager;
//    @Autowired
//    private RewardService rewardService;
//
//    @Override
//    public Result<?> doUseItem(long actorId, long itemId, JSONObject jsonObject) {
//
//        //扣除消耗
//        int consumeNum = jsonObject.getIntValue("num");
//        long itemTotal = itemManager.getItemTotal(actorId, itemId);
//        if(itemTotal <consumeNum){
//            return Result.valueOf("道具不足");
//        }
//
//        Result<BackPackInfo> backPackInfo = itemManager.consumeBackPackItem(actorId, itemId, consumeNum);
//        if(backPackInfo.isFail()){
//            return Result.valueOf(backPackInfo.getCode());
//        }
//        //获取使用道具获得的奖励列表
//        int baseItemId = ItemIdUtil.getBaseItemId(itemId);
//        List<Reward> rewardList = itemManager.getRewardList(baseItemId);
//        //发奖励
//        rewardService.addRewardList(actorId,rewardList);
//        return backPackInfo;
//    }
//}
