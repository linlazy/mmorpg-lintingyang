package com.linlazy.mmorpglintingyang.module.item.manager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.backpack.BackPack;
import com.linlazy.mmorpglintingyang.module.backpack.ItemDo;
import com.linlazy.mmorpglintingyang.module.common.reward.Reward;
import com.linlazy.mmorpglintingyang.module.common.reward.RewardConfgService;
import com.linlazy.mmorpglintingyang.module.item.manager.config.ItemConfigService;
import com.linlazy.mmorpglintingyang.module.item.manager.dao.ItemDao;
import com.linlazy.mmorpglintingyang.module.item.manager.domain.BackpackCell;
import com.linlazy.mmorpglintingyang.module.item.manager.entity.Item;
import com.linlazy.mmorpglintingyang.server.common.GlobalConfigService;
import com.linlazy.mmorpglintingyang.server.common.Result;
import com.linlazy.mmorpglintingyang.utils.ItemIdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

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
     * 获取背包配置ID为baseItemId的最大orderId
     * @param actorId
     * @param baseItemId
     * @return
     */
    public int getBaseItemIdMaxOrderId(long actorId,int baseItemId){
        Set<Item> itemSet = itemDao.getItemSet(actorId);
        return itemSet.stream()
                .map(BackpackCell::new)
                .filter(backpackCell -> backpackCell.getBaseItemId() == baseItemId)
                .max(Comparator.comparingInt(BackpackCell::getOrderId))
                .map(BackpackCell::getOrderId)
                .orElse(0);
    }

    /**
     * 获取背包信息
     * @param actorId
     * @return
     */
    public Set<BackpackCell> getActorBackPack(long actorId){
        Set<Item> itemSet = itemDao.getItemSet(actorId);
        return itemSet.stream()
                .map(BackpackCell::new)
                .collect(Collectors.toSet());
    }

    /**
     * 可叠加物品是否背包已满
     * @param backPack 背包
     * @param baseItemId 可叠加物品策划配置ID
     * @param num 需要放入背包的数量
     * @return
     */
    private boolean isSuperPositionFullPackage(Set<BackpackCell> backPack, int baseItemId, int num,int superPosition) {
        //计算玩家背包可放置该物品的总数量
        //空格子数量
        int spaceNum = globalConfigService.getPackageMaxLatticeNum() -backPack.size();
        long totalNum = spaceNum * superPosition + backPack.stream()
                .filter(backpackCell -> backpackCell.getBaseItemId() == baseItemId)
                .map(backpackCell -> superPosition - backpackCell.getCount())
                .reduce(0,(a,b)->a+b);
        return totalNum < num;
    }

    /**
     * 不可叠加物品是否背包已满
     * @param backPack 背包
     * @param needSpaceNum 需要的背包空格数量
     * @return
     */
    private boolean isNonSuperPositionFullPackage(Set<BackpackCell> backPack, int needSpaceNum){
        int spaceNum =globalConfigService.getPackageMaxLatticeNum() - backPack.size();
        return spaceNum <needSpaceNum;
    }

    /**
     * 整理背包
     * @param actorId
     */
    public List<BackpackCell> arrangeBackPack(long actorId) {

        List<BackpackCell> arrangeBackPack = new ArrayList<>(globalConfigService.getPackageMaxLatticeNum());
        //构建 baseItemId，orderId，数量映射
        Set<BackpackCell> actorBackPack = getActorBackPack(actorId);
        Map<String,Integer>  baseItemIdOrderIdCountMap = new HashMap<>();
        for(BackpackCell backpackCell: actorBackPack){
            baseItemIdOrderIdCountMap.putIfAbsent(backpackCell.getBaseItemId() + ":" + backpackCell.getOrderId(), 0);
            Integer total = baseItemIdOrderIdCountMap.get(backpackCell.getBaseItemId() + ":" + backpackCell.getOrderId());
            total += backpackCell.getCount();
            baseItemIdOrderIdCountMap.put(backpackCell.getBaseItemId() + ":" + backpackCell.getOrderId(),total);
        }


        //放进背包
        int backPackIndex =0;
        for(Map.Entry<String,Integer> baseItemIdOrderIdCount: baseItemIdOrderIdCountMap.entrySet()){

            JSONObject itemConfig = itemConfigService.getItemConfig(ItemIdUtil.getBaseItemId(baseItemIdOrderIdCount.getKey()));
            int superPosition = itemConfig.getInteger("superPosition") == null? 1: itemConfig.getInteger("superPosition");

            //计算物品叠加上限所占据格子数
            int times= baseItemIdOrderIdCount.getValue()/superPosition;
            for(int i = 1; i <= times ; i++){
                long newItemId = ItemIdUtil.getNewItemId(ItemIdUtil.getOrderId(baseItemIdOrderIdCount.getKey()), backPackIndex++, ItemIdUtil.getBaseItemId(baseItemIdOrderIdCount.getKey()));
                Item item = new Item(actorId, newItemId, superPosition);
                arrangeBackPack.add(new BackpackCell(item));
            }

            int remainCount = baseItemIdOrderIdCount.getValue() - times * superPosition;
            //再占据一格子
            if(remainCount > 0){
                long newItemId = ItemIdUtil.getNewItemId(ItemIdUtil.getOrderId(baseItemIdOrderIdCount.getKey()), backPackIndex++, ItemIdUtil.getBaseItemId(baseItemIdOrderIdCount.getKey()));
                Item item = new Item(actorId, newItemId, remainCount);
                arrangeBackPack.add(new BackpackCell(item));
            }
        }

        //清理玩家旧背包
        itemDao.deleteActorItems(actorId);
        //添加新背包
        for(BackpackCell backpackCell: arrangeBackPack){
            itemDao.addItem(backpackCell.convert2Item());
        }
        return arrangeBackPack;
    }

    /**
     * 增加不可叠加道具
     * @param actorId
     * @param baseItemId
     */
    private Result<List<BackpackCell>> addNonSuperPositionItem(long actorId, int baseItemId, int num) {
        List<BackpackCell> result = new ArrayList<>();

        Set<BackpackCell> actorBackPack = getActorBackPack(actorId);

        while(num >=0){
            int spaceBackPackIndex = getSpaceBackPackIndex(actorId);

            int baseItemIdMaxOrderId = getBaseItemIdMaxOrderId(actorId, baseItemId);
            long newItemId =ItemIdUtil.getNewItemId(baseItemIdMaxOrderId + 1,spaceBackPackIndex,baseItemId);
            Item item = new Item(actorId,newItemId,1);
            BackpackCell backpackCell = new BackpackCell(item);
            itemDao.addItem(backpackCell.convert2Item());

            result.add(backpackCell);
            num--;
        }
        return Result.success(result);
    }

    /**
     * 获取背包空格子索引
     * @return
     */
    public int getSpaceBackPackIndex(long actorId){
        Set<BackpackCell> actorBackPack = getActorBackPack(actorId);
        for(int backPackIndex = 0; backPackIndex < globalConfigService.getPackageMaxLatticeNum(); backPackIndex++){
            if(!actorBackPack.contains(new BackpackCell(backPackIndex))){
                return backPackIndex;
            }
        }
        return -1;
    }

    /**
     * 增加可叠加道具
     * @param actorId
     * @param baseItemId
     * @param num
     * @param superPosition
     */
    private Result<List<BackpackCell>> addSuperPositionItem(long actorId, int baseItemId, int num, int superPosition){
        List<BackpackCell> result = new ArrayList<>();

        Set<BackpackCell> actorBackPack = getActorBackPack(actorId);
        //获取格子未满的baseItemId的格子
        List<BackpackCell> backpackCellList = actorBackPack.stream()
                .filter(backpackCell -> backpackCell.getBaseItemId() == baseItemId && backpackCell.getCount() < superPosition)
                .collect(Collectors.toList());

        for(BackpackCell backpackCell:backpackCellList){
            //未超过可叠加上限
            if(backpackCell.getCount() + num <= superPosition){
                backpackCell.setCount(backpackCell.getCount() + num);
                //存档
                itemDao.updateItem(backpackCell.convert2Item());

                result.add(backpackCell);
                return Result.success(result);
            }

            //格子未满，但超过叠加上限
            num = num - (superPosition - backpackCell.getCount());
            backpackCell.setCount(superPosition);
            //存档
            itemDao.updateItem(backpackCell.convert2Item());
            result.add(backpackCell);
        }


        //若还有，则放入空格子
        for(int backPackIndex = 0; backPackIndex < globalConfigService.getPackageMaxLatticeNum() && num > 0; backPackIndex++){
            if(!actorBackPack.contains(new BackpackCell(backPackIndex))){

                long newItemId = ItemIdUtil.getNewItemId(0,backPackIndex,baseItemId);
                //未超过可叠加上限
                if(num <= superPosition){
                    Item item = new Item(actorId, newItemId, num);
                    BackpackCell backpackCell = new BackpackCell(item);
                    //存档
                    itemDao.addItem(backpackCell.convert2Item());
                    result.add(backpackCell);
                    return Result.success(result);

                }

                Item item = new Item(actorId, newItemId, superPosition);
                BackpackCell backpackCell = new BackpackCell(item);
                //存档
                itemDao.addItem(backpackCell.convert2Item());
                num = num - superPosition;
                result.add(backpackCell);
            }
        }
        return Result.success(result);
    }

    /**
     * 判断是否背包已满
     * @param actorId
     * @param baseItemId
     * @param num
     * @return
     */
    public boolean isFullBackPack(long actorId, int baseItemId, int num) {
        Set<BackpackCell> actorBackPack = getActorBackPack(actorId);
        JSONObject itemConfig = itemConfigService.getItemConfig(baseItemId);
        //判断策略
        if( itemConfig.getInteger("superPosition") != null){
            //可叠加
            return isSuperPositionFullPackage(actorBackPack,baseItemId,num,itemConfig.getIntValue("superPosition"));
        }else {
            //不可叠加
            return isNonSuperPositionFullPackage(actorBackPack,num);
        }
    }

    /**
     * 增加道具
     * @param actorId
     * @param baseItemId
     * @param num
     */
    public Result<List<BackpackCell>> addItem(long actorId, int baseItemId, int num) {

        JSONObject itemConfig = itemConfigService.getItemConfig(baseItemId);

        if(itemConfig.getInteger("superPosition") != null){
            //可叠加
             return addSuperPositionItem(actorId,baseItemId,num,itemConfig.getIntValue("superPosition"));
        }else {
            //不可叠加
           return addNonSuperPositionItem(actorId,baseItemId,num);
        }
    }


    /**
     * 获取玩家背包配置ID位baseItemID的物品总数
     * @param actorId
     * @param baseItemId
     * @return
     */
    public long getSuperPositionTotal(long actorId, int baseItemId) {
        Set<Item> itemSet = itemDao.getItemSet(actorId);
        return itemSet.stream()
                .map(BackpackCell::new)
                .filter(backpackCell -> backpackCell.getBaseItemId() ==baseItemId)
                .map(BackpackCell::getCount)
                .reduce(0,(a,b)->a + b);
    }

    /**
     * 获取策划配置ID奖励
     * @param baseItemId
     * @return
     */
    public List<Reward> getRewardList(int baseItemId) {
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
    public List<BackpackCell> consumeBackPackItem(long actorId,int baseItemId,int consumeNum) {
        List<BackpackCell> result = new ArrayList<>();

        Set<BackpackCell> actorBackPack = getActorBackPack(actorId);
        List<BackpackCell> backpackCellList = actorBackPack.stream()
                .filter(backpackCell -> backpackCell.getBaseItemId() == baseItemId)
                .collect(Collectors.toList());

        for(BackpackCell backpackCell: backpackCellList){
            //当前格子满足消耗
            if(backpackCell.getCount() > consumeNum){
                backpackCell.setCount(backpackCell.getCount() - consumeNum);
                itemDao.updateItem(backpackCell.convert2Item());
                result.add(backpackCell);
                break;
            }

            //否则，更新背包
            consumeNum -= backpackCell.getCount();
            itemDao.deleteItem(backpackCell.convert2Item());

            backpackCell.setCount(0);
            result.add(backpackCell);
        }

        return result;
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

    public void addItem(Item item) {
        itemDao.addItem(item);
    }

    /**
     * 获取非可叠加物品itemId
     * @param actorId
     * @param baseItemId
     * @return
     */
    public long getNonSuperPositionNewItemId(long actorId, int baseItemId){
        int spaceBackPackIndex = getSpaceBackPackIndex(actorId);
        int baseItemIdMaxOrderId = getBaseItemIdMaxOrderId(actorId, baseItemId);
        return ItemIdUtil.getNewItemId(baseItemIdMaxOrderId + 1, spaceBackPackIndex, baseItemId);
    }




    /**
     * 整理背包
     * @param actorId
     */
    public void arrange(long actorId){
        BackPack backPack = new BackPack(actorId);
        backPack.arrange();
    }

    /**
     * 放置道具进背包
     * @param actorId
     */
    public void pushBackPack(long actorId, ItemDo itemDo){
        BackPack backPack = new BackPack(actorId);
        backPack.pushBackPack(itemDo);
    }
}
