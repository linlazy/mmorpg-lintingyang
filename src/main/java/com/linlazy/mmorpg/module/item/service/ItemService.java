package com.linlazy.mmorpg.module.item.service;

import com.google.common.collect.Lists;
import com.linlazy.mmorpg.file.service.ItemConfigService;
import com.linlazy.mmorpg.module.backpack.service.PlayerBackpackService;
import com.linlazy.mmorpg.module.item.domain.Item;
import com.linlazy.mmorpg.module.item.type.BaseItem;
import com.linlazy.mmorpg.module.player.domain.Player;
import com.linlazy.mmorpg.module.player.domain.PlayerBackpack;
import com.linlazy.mmorpg.module.player.service.PlayerService;
import com.linlazy.mmorpg.module.scene.domain.Scene;
import com.linlazy.mmorpg.module.scene.service.SceneService;
import com.linlazy.mmorpg.server.common.Result;
import com.linlazy.mmorpg.server.threadpool.ScheduledThreadPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 道具服务类
 * @author linlazy
 */
@Component
public class ItemService {


    @Autowired
    private PlayerBackpackService playerBackpackService;
    @Autowired
    private SceneService sceneService;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private ItemConfigService itemConfigService;

    /**
     * 丢弃道具消失调度线程池
     */
    ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPool(20);


    /**
     * 使用道具
     * @param actorId 玩家ID
     * @param itemId 道具ID
     * @return
     */
    public Result<?> useItem(long actorId, long itemId) {

        Item item = new Item(itemId,1);
        BaseItem baseItem = BaseItem.getBaseItem(item.getItemType());
        baseItem.useItem(actorId,item);

//        ItemContext itemContext = new ItemContext(itemId);
//        itemContext.setCount(1);
//
//        Result<?> enough = playerBackpackService.isEnough(actorId, Lists.newArrayList(itemContext));
//        if(enough.isFail()){
//            return Result.valueOf(enough.getCode());
//        }
//        PlayerBackpack playerBackpack = playerBackpackService.getPlayerBackpack(actorId);
//        playerBackpack.pop(Lists.newArrayList(itemContext));
//
//        JSONObject itemConfig = itemConfigService.getItemConfig(ItemIdUtil.getBaseItemId(itemId));
//        String rewards = itemConfig.getString("rewards");
//        List<Reward> rewardList = RewardUtils.parseRewards(rewards);
//        rewardService.addRewardList(actorId,rewardList);

        return Result.success();
    }

    /**
     * 拾取道具
     * @param actorId 玩家ID
     * @param id 道具标识
     * @return
     */
    public Result<?> pickItem(long actorId, long id) {

        Result<?> notFull = playerBackpackService.isNotFull(actorId, Lists.newArrayList());
        if(notFull.isFail()){
            return Result.valueOf(notFull.getCode());
        }
        Player player = playerService.getPlayer(actorId);
        Scene scene= sceneService.getSceneBySceneEntity(player);
        byte[] itemLock = scene.getItemLock();
        synchronized (itemLock){
            Item item = scene.getItemMap().remove(id);
            if(item == null){
                return Result.valueOf("物品不存在");
            }
            scene.cancelItemRemoveSchedule(id);
            PlayerBackpack playerBackpack = playerBackpackService.getPlayerBackpack(actorId);
            playerBackpack.push(Lists.newArrayList(item));
        }
        return Result.success();
    }

    /**
     * 丢弃道具
     * @param actorId 玩家ID
     * @param itemId 道具ID
     * @return
     */
    public Result<?> discardItem(long actorId, long itemId) {
        Player player = playerService.getPlayer(actorId);

        Result<?> pop = playerBackpackService.pop(actorId, Lists.newArrayList());
        if(pop.isFail()){
            return Result.valueOf(pop.getCode());
        }

        Scene scene = sceneService.getSceneBySceneEntity(player);
        Item item = new Item(itemId,1);
        byte[] itemLock = scene.getItemLock();
        synchronized (itemLock){
            scene.addItem(item);
            //60秒后掉落道具消失
            ScheduledFuture<?> itemRemoveSchedule = scheduledExecutorService.schedule(() -> {
                scene.removeItem(item);
            }, 60L, TimeUnit.SECONDS);
            scene.addItemRemoveSchedule(item.getId(),itemRemoveSchedule);
        }
        return Result.success();
    }

    /**
     * 销毁道具
     * @param actorId 玩家ID
     * @param itemId 道具ID
     * @return
     */
    public Result<?> destoryItem(long actorId, long itemId) {

        return  playerBackpackService.pop(actorId, Lists.newArrayList());
    }
}
