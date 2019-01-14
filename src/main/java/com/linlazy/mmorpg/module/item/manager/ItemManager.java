//package com.linlazy.mmorpg.module.item.manager;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.linlazy.mmorpg.dao.ItemDAO;
//import com.linlazy.mmorpg.module.item.manager.backpack.domain.BackPack;
//import com.linlazy.mmorpg.module.item.manager.backpack.domain.ItemDo;
//import com.linlazy.mmorpg.module.item.manager.backpack.response.BackPackInfo;
//import com.linlazy.mmorpg.module.common.reward.Reward;
//import com.linlazy.mmorpg.module.common.reward.RewardConfigService;
//import com.linlazy.mmorpg.module.item.manager.config.ItemConfigService;
//import com.linlazy.mmorpg.module.item.manager.dao.ItemDao;
//import com.linlazy.mmorpg.module.item.manager.entity.Item;
//import com.linlazy.mmorpg.server.common.GlobalConfigService;
//import com.linlazy.mmorpg.server.common.Result;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Set;
//
///**
// * @author linlazy
// */
//@Component
//public class ItemManager {
//
//    @Autowired
//    private ItemDAO itemDAO;
//
//    @Autowired
//    private GlobalConfigService globalConfigService;
//    @Autowired
//    private ItemConfigService itemConfigService;
//    @Autowired
//    private RewardConfigService rewardConfigService;
//
//    /**
//     * 获取背包信息
//     * @param actorId
//     * @return
//     */
//    public BackPackInfo getActorBackPack(long actorId){
//        return new BackPack(actorId).getInfo();
//    }
//
//    /**
//     * 判断是否背包已满
//     * @param actorId
//     * @param baseItemId
//     * @param num
//     * @return
//     */
//    public boolean isFullBackPack(long actorId, int baseItemId, int num) {
//        ItemDo itemDo = new ItemDo(baseItemId);
//        //客户端数据
//        itemDo.setActorId(actorId);
//        itemDo.setBaseItemId(baseItemId);
//        itemDo.setCount(num);
//        return new BackPack(actorId).isFullBackPack(itemDo);
//    }
//
//    /**
//     * 增加道具
//     * @param actorId
//     * @param baseItemId
//     * @param num
//     */
//    public Result<BackPackInfo> pushBackPack(long actorId, int baseItemId, int num) {
//        ItemDo itemDo = new ItemDo(baseItemId);
//        //客户端数据
//        itemDo.setActorId(actorId);
//        itemDo.setBaseItemId(baseItemId);
//        itemDo.setCount(num);
//
//        BackPackInfo backPackInfo = new BackPack(actorId).pushBackPack(itemDo);
//        return Result.success(backPackInfo);
//
//    }
//
//    /**
//     * 获取玩家背包物品总数
//     * @param actorId
//     * @param itemId
//     * @return
//     */
//    public long getItemTotal(long actorId, long itemId) {
//        ItemDo itemDo = new ItemDo(itemId);
//
//        Set<Item> itemSet = itemDao.getItemSet(actorId);
//        return itemSet.stream()
//                .map(ItemDo::new)
//                .filter(itemDo1 -> itemDo1.getBaseItemIdOrderIdKey().equals(itemDo.getBaseItemIdOrderIdKey()))
//                .map(ItemDo::getCount)
//                .reduce(0,(a,b)->a + b);
//
//    }
//
//    /**
//     * 获取策划配置ID奖励
//     * @param baseItemId
//     * @return
//     */
//    public List<Reward> getRewardList(int baseItemId) {
//        List<Reward> rewardList = new ArrayList<>();
//
//        JSONObject itemConfig = itemConfigService.getItemConfig(baseItemId);
//        String rewards = itemConfig.getString("rewards");
//        JSONArray jsonArray = JSON.parseArray(rewards);
//        for(int i = 0 ; i < jsonArray.size(); i++){
//            Reward reward = new Reward();
//
//            JSONArray jsonArray1 = jsonArray.getJSONArray(i);
//            int rewardId = jsonArray1.getIntValue(0);
//            int rewardCount = jsonArray1.getIntValue(1);
//
//            reward.setRewardId(rewardId);
//            reward.setCount(rewardCount);
//            reward.setRewardDBType(rewardConfigService.getRewardDBType(rewardId));
//
//            rewardList.add(reward);
//        }
//
//        return rewardList;
//    }
//
//    /**
//     * 增加道具奖励
//     * @param actorId
//     * @param reward
//     */
//    public void addReward(long actorId, Reward reward) {
//        int baseItemId = (int) reward.getRewardId();
//        pushBackPack(actorId,baseItemId,reward.getCount());
//    }
//
//    /**
//     * 消耗背包道具
//     * @param actorId
//     * @param itemId
//     * @param consumeNum
//     */
//    public Result<BackPackInfo> consumeBackPackItem(long actorId,long itemId,int consumeNum) {
//        ItemDo itemDo = new ItemDo(itemId);
//        itemDo.setActorId(actorId);
//        itemDo.setCount(consumeNum);
//
//        BackPackInfo backPackInfo = new BackPack(actorId).popBackPack(itemDo);
//        return Result.success(backPackInfo);
//    }
//    /**
//     * 整理背包
//     * @param actorId
//     */
//    public BackPackInfo arrangeBackPack(long actorId){
//        BackPack backPack = new BackPack(actorId);
//        return backPack.arrange();
//    }
//
//
//    public Item getItem(long actorId, long itemId) {
//        return itemDao.getItem(actorId,itemId);
//    }
//}
