package com.linlazy.mmorpglintingyang.module.item.manager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.server.common.GlobalConfigService;
import com.linlazy.mmorpglintingyang.module.item.manager.config.ItemConfigService;
import com.linlazy.mmorpglintingyang.module.item.manager.dao.ItemDao;
import com.linlazy.mmorpglintingyang.module.item.manager.entity.Item;
import com.linlazy.mmorpglintingyang.module.item.manager.model.BackpackIndexCount;
import com.linlazy.mmorpglintingyang.module.common.reward.Reward;
import com.linlazy.mmorpglintingyang.module.common.reward.RewardConfgService;
import com.linlazy.mmorpglintingyang.utils.ItemIdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ItemManager {

    @Autowired
    private ItemDao itemDao;

    @Autowired
    private GlobalConfigService globalConfigService;
    @Autowired
    private ItemConfigService itemConfigService;
    @Autowired
    private RewardConfgService rewardConfgService;

    /**
     * baseItemId与orderId映射
     * actorId与（baseItemId与orderId映射结果）映射
     */
    private Map<Long,Map<Integer,Integer>> actorIdBaseItemIdMaxOrderIdMap = new HashMap<>();

    /**
     * 玩家与背包映射
     */
    private Map<Long,Item[]> actorIdBackPackMap = new HashMap<>();

    /**
     * 玩家策划配置ID（可叠加物品背包索引数量）映射
     */
    private Map<Long,Map<Integer,List<BackpackIndexCount>>> actorIdBackpackIndexCountMap = new HashMap<>();

    /**
     * 玩家策划配置ID（可叠加物品总数）映射
     */
    private Map<Long,Map<Integer,Integer>>  getActorIdBaseItemIdTotalMap = new HashMap<>();


    /**
     * 初始化玩家背包
     * @param actorId
     */
    public Item[] initActorBackPack(long actorId){
        actorIdBaseItemIdMaxOrderIdMap.putIfAbsent(actorId,new HashMap<>());
        Map<Integer, Integer> baseItemIdMaxOrderIdMap = actorIdBaseItemIdMaxOrderIdMap.get(actorId);

        Item[] items = new Item[globalConfigService.getPackageMaxLatticeNum()];

        Set<Item> itemSet = itemDao.getItemSet(actorId);
        for(Item item: itemSet){

            //首次初始化玩家背包时构建baseItemId与maxOrderIdMap,以便新增时确定orderId
            int baseItemId = ItemIdUtil.getBaseItemId(item.getItemId());
            int orderId = ItemIdUtil.getOrderId(item.getItemId());
            baseItemIdMaxOrderIdMap.putIfAbsent(baseItemId, 0);
            int maxOrderId = baseItemIdMaxOrderIdMap.get(baseItemId);
            if(maxOrderId < orderId){
                baseItemIdMaxOrderIdMap.put(baseItemId,orderId);
            }

            //获取背包索引
            int backPackIndex = ItemIdUtil.getBackPackIndex(item.getItemId());
            items[backPackIndex] = item;
        }

        actorIdBaseItemIdMaxOrderIdMap.put(actorId,baseItemIdMaxOrderIdMap);
        actorIdBackPackMap.put(actorId,items);
        return items;
    }


    /**
     * 获取背包信息
     * @param actorId
     * @return
     */
    public Item[] getActorBackPack(long actorId){
        Item[] items = actorIdBackPackMap.get(actorId);
        if(items == null){
            items = initActorBackPack(actorId);
        }
        return items;
    }

    /**
     * 可叠加物品是否背包已满
     * @param actorId
     * @param baseItemId
     * @param num
     * @return
     */
    private boolean isSuperPositionFullPackage(long actorId, int baseItemId, int num) {
        //计算玩家背包可放置该物品的总数量
        int totalNum = 0;
        Item[] backPack = getActorBackPack(actorId);
        JSONObject itemConfig = itemConfigService.getItemConfig(baseItemId);
        int superPosition = itemConfig.getIntValue("superPosition");
        for(int backPackIndex = 0 ; backPackIndex < backPack.length; backPackIndex++){
            //格子不为空
            if(backPack[backPackIndex] != null){
                if(ItemIdUtil.getBaseItemId(backPack[backPackIndex].getItemId()) == baseItemId){
                    totalNum =totalNum + (superPosition -backPack[backPackIndex].getCount());
                }
            }else {
                totalNum +=superPosition;
            }
        }

        return totalNum < num;
    }

    /**
     * 不可叠加物品是否背包已满
     * @param actorId
     * @param baseItemId
     * @return
     */
    private boolean isNonSuperPositionFullPackage(long actorId, int baseItemId){
        Item[] backPack = actorIdBackPackMap.get(actorId);
        for(int backPackIndex = 0; backPackIndex < backPack.length; backPackIndex++){
            if(backPack[backPackIndex] == null){
                return true;
            }
        }
        return false;
    }

    /**
     * 整理背包
     * @param actorId
     */
    public void arrangeBackPack(long actorId) {
        Item[] backPack =getActorBackPack(actorId);
        // 构建 [baseItemId + ":" + orderId]与数量映射结果
        Map<String, Integer> baseItemIdOrderIdCountMap = new HashMap<>();
        for(int backPackIndex = 0; backPackIndex <backPack.length; backPackIndex++){

            if(backPack[backPackIndex] != null){
                String baseItemIdOrderIdKey = ItemIdUtil.getBaseItemIdOrderIdKey(backPack[backPackIndex].getItemId());
                baseItemIdOrderIdCountMap.putIfAbsent(baseItemIdOrderIdKey, 0);
                int totalCount = baseItemIdOrderIdCountMap.get(baseItemIdOrderIdKey);
                totalCount += backPack[backPackIndex].getCount();
                baseItemIdOrderIdCountMap.put(baseItemIdOrderIdKey,totalCount);
            }
        }

        //放进背包
        Item[] arrangeBackPack = new Item[globalConfigService.getPackageMaxLatticeNum()];
        int backPackIndex = 0;
        for(Map.Entry<String,Integer> entry: baseItemIdOrderIdCountMap.entrySet()){

            int baseItemId = ItemIdUtil.getBaseItemId(entry.getKey());
            int orderId = ItemIdUtil.getOrderId(entry.getKey());

            JSONObject itemConfig = itemConfigService.getItemConfig(baseItemId);
            int superPosition = itemConfig.getIntValue("superPosition");

            //计算物品叠加上限所占据格子数
            int times= entry.getValue()/superPosition;
            for(int i = 1; i <= times ; i++){
                long newItemId = ItemIdUtil.getNewItemId(orderId, backPackIndex, baseItemId);
                arrangeBackPack[backPackIndex++] = new Item(actorId,newItemId,superPosition);
            }


            int remainCount = entry.getValue() - times * superPosition;
            //再占据一格子
            if(remainCount > 0){
                long newItemId = ItemIdUtil.getNewItemId(orderId, backPackIndex, baseItemId);
                arrangeBackPack[backPackIndex++] =  new Item(actorId,newItemId,remainCount);
            }
        }
        actorIdBackPackMap.put(actorId,arrangeBackPack);
        //清理玩家旧背包
        itemDao.deleteActorItems(actorId);
        //添加新背包
        for(int i = 0 ; i <arrangeBackPack.length; i++){
            if(arrangeBackPack[i] != null){
                itemDao.addItem(arrangeBackPack[i]);
            }
        }
    }



    /**
     * 增加不可叠加道具
     * @param actorId
     * @param baseItemId
     */
    private void addNonSuperPositionItem(long actorId,int baseItemId) {

        //遍历数组，找空格子
        Item[] backPack = actorIdBackPackMap.get(actorId);
        for(int backPackIndex = 0 ; backPackIndex < backPack.length; backPackIndex++){
            if(backPack[backPackIndex] == null){
                int maxOrderId = actorIdBaseItemIdMaxOrderIdMap.get(actorId).get(baseItemId);
                long newItemId =  ItemIdUtil.getNewItemId(maxOrderId + 1 ,backPackIndex,baseItemId);
                Item newItem = new Item(actorId, newItemId, 1);
                //更新背包，存档
                backPack[backPackIndex] =newItem;
                itemDao.addItem(newItem);
                //更新自增序列
                actorIdBaseItemIdMaxOrderIdMap.get(actorId).put(baseItemId,maxOrderId + 1);
            }
        }

    }

    /**
     * 增加可叠加道具
     * @param actorId
     * @param baseItemId
     * @param num
     */
    private void addSuperPositionItem(long actorId,int baseItemId,int num){
        JSONObject itemConfig = itemConfigService.getItemConfig(baseItemId);

        Item[] backPack = getActorBackPack(actorId);
        //从索引0到索引最大，查找配置ID
        int backPackIndex =0;
        for(backPackIndex = 0; backPackIndex < backPack.length; backPackIndex++){
            if(backPack[backPackIndex] != null){
                if(ItemIdUtil.getBaseItemId(backPack[backPackIndex].getItemId()) == baseItemId){
                    int superPosition = itemConfig.getIntValue("superPosition");
                    int count = backPack[backPackIndex].getCount();

                    //未超过可叠加上限
                    if(count + num <= superPosition){
                        backPack[backPackIndex].setCount(count + num);
                        //存档
                        itemDao.updateItem(backPack[backPackIndex]);
                        break;
                    }

                    backPack[backPackIndex].setCount(superPosition);
                    num = num - (superPosition - count);
                    //存档
                    itemDao.updateItem(backPack[backPackIndex]);
                }
            }
        }

        //道具在背包中不存在
        if(backPackIndex >= backPack.length){
            for(backPackIndex = 0; backPackIndex < backPack.length; backPackIndex++){
                if(backPack[backPackIndex] == null){

                    int superPosition = itemConfig.getIntValue("superPosition");
                    long newItemId = ItemIdUtil.getNewItemId(0,backPackIndex,baseItemId);
                    //未超过可叠加上限
                    if(num <= superPosition){
                        Item item = new Item(actorId, newItemId, num);
                        backPack[backPackIndex] = item;
                        //存档
                        itemDao.addItem(backPack[backPackIndex]);
                        break;
                    }

                    Item item = new Item(actorId, newItemId, superPosition);
                    backPack[backPackIndex] = item;
                    //存档
                    itemDao.addItem(backPack[backPackIndex]);
                    num = num - superPosition;
                }
            }
        }
    }

    public boolean isFullBackPack(long actorId, int baseItemId, int num) {
        JSONObject itemConfig = itemConfigService.getItemConfig(baseItemId);
        //可叠加
        if(itemConfig.getIntValue("superPosition") >1){
            return isSuperPositionFullPackage(actorId,baseItemId,num);
            //不可叠加
        }else {
            return isNonSuperPositionFullPackage(actorId,baseItemId);
        }
    }

    /**
     * 增加道具
     * @param actorId
     * @param baseItemId
     * @param num
     */
    public void addItem(long actorId, int baseItemId, int num) {
        JSONObject itemConfig = itemConfigService.getItemConfig(baseItemId);
        //可叠加
        if(itemConfig.getIntValue("superPosition") >1){
             addSuperPositionItem(actorId,baseItemId,num);
            //不可叠加
        }else {
           addNonSuperPositionItem(actorId,baseItemId);
        }
    }


    /**
     * 获取可叠加物品总数
     * @param actorId
     * @param baseItemId
     * @return
     */
    public int getSuperPositionTotal(long actorId, int baseItemId) {
        return  getActorIdBaseItemIdTotalMap.get(actorId).get(baseItemId);
    }

    /**
     * 获取策划配置ID奖励
     * @param baseItemId
     * @return
     */
    public List<Reward> getRewards(int baseItemId) {
        List<Reward> rewardList = new ArrayList<>();

        JSONObject itemConfig = itemConfigService.getItemConfig(baseItemId);
        String rewards = itemConfig.getString("rewards");
        JSONArray jsonArray = JSON.parseArray(rewards);
        for(int i = 0 ; i < jsonArray.size(); i++){
            Reward reward = new Reward();

            JSONArray jsonArray1 = jsonArray.getJSONArray(i);
            int rewardId = jsonArray1.getIntValue(0);
            int rewardCount = jsonArray1.getIntValue(1);

            reward.setRewardId(rewardId);
            reward.setCount(rewardCount);
            reward.setRewardDBType(rewardConfgService.getRewardDBType(rewardId));

            rewardList.add(reward);
        }

        return rewardList;
    }

    /**
     * 增加道具奖励
     * @param actorId
     * @param reward
     */
    public void addReward(long actorId, Reward reward) {
        int baseItemId = reward.getRewardId();
        addItem(actorId,baseItemId,reward.getCount());
    }

    /**
     * 消耗背包道具
     * @param actorId
     * @param baseItemId
     * @param consumeNum
     */
    public void consumeBackPackItem(long actorId,int baseItemId,int consumeNum) {
        Item[] actorBackPack = getActorBackPack(actorId);
        List<BackpackIndexCount> backpackIndexCounts = actorIdBackpackIndexCountMap.get(actorId).get(baseItemId);
        for(BackpackIndexCount backpackIndexCount: backpackIndexCounts){
            if(backpackIndexCount.getCount() > consumeNum){
                actorBackPack[backpackIndexCount.getBackPackIndex()].setCount(backpackIndexCount.getCount() - consumeNum);
                itemDao.updateItem(actorBackPack[backpackIndexCount.getBackPackIndex()]);
                break;
            }

            //更新背包
            consumeNum -= backpackIndexCount.getCount();
            itemDao.deleteItem(actorBackPack[backpackIndexCount.getBackPackIndex()]);
            actorBackPack[backpackIndexCount.getBackPackIndex()] = null;
        }
    }

    public Item getItem(long actorId, long itemId) {
        return itemDao.getItem(actorId,itemId);
    }

    public JSONObject getItemConfig(int baseItemId) {
        return itemConfigService.getItemConfig(baseItemId);
    }

    public void updateItem(Item item) {
        itemDao.updateItem(item);
    }
}
