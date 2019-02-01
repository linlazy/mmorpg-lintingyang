package com.linlazy.mmorpg.module.equip.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import com.linlazy.mmorpg.dao.ItemDAO;
import com.linlazy.mmorpg.entity.ItemEntity;
import com.linlazy.mmorpg.event.type.PlayerAttackEvent;
import com.linlazy.mmorpg.event.type.PlayerAttackedEvent;
import com.linlazy.mmorpg.file.service.ItemConfigService;
import com.linlazy.mmorpg.module.backpack.service.PlayerBackpackService;
import com.linlazy.mmorpg.module.common.event.ActorEvent;
import com.linlazy.mmorpg.module.common.event.EventBusHolder;
import com.linlazy.mmorpg.module.common.event.EventType;
import com.linlazy.mmorpg.module.equip.constants.EquipType;
import com.linlazy.mmorpg.module.equip.domain.DressedEquip;
import com.linlazy.mmorpg.module.equip.dto.DressedEquipDTO;
import com.linlazy.mmorpg.module.equip.dto.EquipDTO;
import com.linlazy.mmorpg.module.equip.push.EquipPushHelper;
import com.linlazy.mmorpg.module.item.domain.Item;
import com.linlazy.mmorpg.module.item.domain.ItemContext;
import com.linlazy.mmorpg.module.item.type.ItemType;
import com.linlazy.mmorpg.module.player.domain.Player;
import com.linlazy.mmorpg.module.player.service.PlayerService;
import com.linlazy.mmorpg.server.common.Result;
import com.linlazy.mmorpg.utils.ItemIdUtil;
import com.linlazy.mmorpg.utils.RandomUtils;
import com.linlazy.mmorpg.utils.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author linlazy
 */
@Component
public class EquipmentService {

    @Autowired
    private ItemDAO itemDAO;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private PlayerBackpackService playerBackpackService;
    @Autowired
    private ItemConfigService itemConfigService;

    /**
     * 玩家已穿戴装备缓存
     */
    public static LoadingCache<Long, DressedEquip> playerDressedEquipCache = CacheBuilder.newBuilder()
            .maximumSize(10)
            .expireAfterAccess(60, TimeUnit.SECONDS)
            .recordStats()
            .build(new CacheLoader<Long, DressedEquip>() {
                @Override
                public DressedEquip load(Long actorId) {

                    DressedEquip dressedEquip = new DressedEquip();

                    ItemDAO itemDAO = SpringContextUtil.getApplicationContext().getBean(ItemDAO.class);
                    itemDAO.getItemList(actorId).stream()
                            .map(Item::new)
                            .filter(item -> item.getItemType() == ItemType.EQUIP)
                            .filter(equip -> {
                                JSONObject ext = equip.getExt();
                                return ext.getBooleanValue("dress");
                            })
                            .forEach(equip -> {

                                int baseItemId = ItemIdUtil.getBaseItemId(equip.getItemId());
                                int orderId = ItemIdUtil.getOrderId(equip.getItemId());
                                long id  = ItemIdUtil.getNewItemId(orderId,0,baseItemId);
                                dressedEquip.getEquipMap().put(id,equip);
                            });
                    return dressedEquip;
                }
            });


    @PostConstruct
    public void init(){
        EventBusHolder.register(this);
    }

    @Subscribe
    private void handleAttacked(PlayerAttackedEvent playerAttackedEvent) {
        Player player = playerAttackedEvent.getPlayer();
        Collection<Item> equips =  player.getDressedEquip().getEquipMap().values().stream()
                .filter(equip -> {
                    JSONObject extConfig = equip.getExtConfig();
                    return extConfig.getIntValue("equipType") != EquipType.ARMS;
                })
                .filter(equip -> {
                    JSONObject ext= equip.getExt();
                    return ext.getIntValue("durability") > 0;
                })
                .collect(Collectors.toList());
        if(equips.size() == 0){
            return;
        }

        Item equip = RandomUtils.randomElement(equips);
        equip.modifyDurability();
        EquipPushHelper.pushDressedEquip(player.getActorId(),"受到攻击耐久度减少" + new EquipDTO(equip).toString());
        ItemEntity itemEntity = equip.convertItemEntity();
        itemEntity.setActorId(player.getActorId());
        itemDAO.updateQueue(itemEntity);
    }

    @Subscribe
    private void handleAttack(PlayerAttackEvent playerAttackEvent) {
        Player player = playerAttackEvent.getPlayer();
        Collection<Item> equips = player.getDressedEquip().getEquipMap().values()
                .stream()
                .filter(equip -> {
                    JSONObject extConfig = equip.getExtConfig();
                    return extConfig.getIntValue("equipType") == EquipType.ARMS;
                })
                .filter(equip -> {
                    JSONObject ext= equip.getExt();
                    return ext.getIntValue("durability") > 0;
                })
                .collect(Collectors.toList());
        if(equips.size() == 0){
            return;
        }

        Item equip = RandomUtils.randomElement(equips );

        equip.modifyDurability();
        EquipPushHelper.pushDressedEquip(player.getActorId(),"攻击成功耐久度减少" + new EquipDTO(equip).toString());
        ItemEntity itemEntity = equip.convertItemEntity();
        itemEntity.setActorId(player.getActorId());
        itemDAO.updateQueue(itemEntity);
    }


    /**
     * 装备
     * @param actorId
     * @param equipId
     * @return
     */
    public Result<?> dressEquip(long actorId, long equipId){

        ItemContext itemContext = new ItemContext(equipId);
        itemContext.setCount(1);
        Result<?> enough = playerBackpackService.isEnough(actorId, Lists.newArrayList(itemContext));
        if(enough.isFail()){
            return Result.valueOf(enough.getCode());
        }


        Player player = playerService.getPlayer(actorId);
        Item equip = player.getBackPack().getItem(equipId);
        ItemContext equipItemContext = new ItemContext(equip.getItemId());
        equipItemContext.setCount(equip.getCount());
        player.getBackPack().pop(Lists.newArrayList(equipItemContext));


        DressedEquip dressedEquip = player.getDressedEquip();
        Collection<Item> equips = dressedEquip.getEquipMap().values();
        for(Item dressEquip: equips){
            if(dressEquip.getExtConfig().getIntValue("equipType") == equip.getExtConfig().getIntValue("equipType")){

                int baseItemId = ItemIdUtil.getBaseItemId(dressEquip.getItemId());
                int orderId = ItemIdUtil.getOrderId(dressEquip.getItemId());
                long id  = ItemIdUtil.getNewItemId(orderId,0,baseItemId);

                dressedEquip.getEquipMap().remove(id);

                ItemContext dressedItemContext = new ItemContext(dressEquip.getItemId());
                dressedItemContext.setCount(dressEquip.getCount());

                player.getBackPack().push(Lists.newArrayList(dressedItemContext));
            }
        }


        int baseItemId = ItemIdUtil.getBaseItemId(equip.getItemId());
        int orderId = ItemIdUtil.getOrderId(equip.getItemId());
        long id  = ItemIdUtil.getNewItemId(orderId,0,baseItemId);
        dressedEquip.getEquipMap().put(id,equip);


        equip.getExt().put("dress",true);
        ItemEntity itemEntity = equip.convertItemEntity();
        itemEntity.setActorId(actorId);
        itemDAO.insertQueue(itemEntity);

        EventBusHolder.post(new ActorEvent<>(actorId, EventType.DRESS_EQUIP));
        return Result.success();
    }



    /**
     * 修复装备
     * @param actorId 玩家ID
     * @param equipId 装备ID
     * @return
     */
    public Result<?> fixEquipment(long actorId, long equipId) {

        Player player = playerService.getPlayer(actorId);

        int baseItemId = ItemIdUtil.getBaseItemId(equipId);
        int orderId = ItemIdUtil.getOrderId(equipId);
        long id = ItemIdUtil.getNewItemId(orderId, 0, baseItemId);

        Item equip = player.getDressedEquip().getEquipMap().get(id);
        equip.getExt().put("durability",equip.getExtConfig().getIntValue("durability"));
        ItemEntity itemEntity = equip.convertItemEntity();
        itemEntity.setActorId(actorId);
        itemDAO.updateQueue(itemEntity);

        return Result.success();
    }

    /**
     * 卸载装备
     * @param actorId
     * @param equipId
     * @return
     */
    public Result<?> unDressEquip(long actorId,long equipId) {
        ItemContext itemContext = new ItemContext(equipId);
        itemContext.setCount(1);

        Result<?> notFull = playerBackpackService.isNotFull(actorId, Lists.newArrayList(itemContext));
        if(notFull.isFail()){
            return Result.valueOf(notFull.getCode());
        }

        Player player = playerService.getPlayer(actorId);
        DressedEquip dressedEquip = player.getDressedEquip();
        int baseItemId = ItemIdUtil.getBaseItemId(equipId);
        int orderId = ItemIdUtil.getOrderId(equipId);
        long id = ItemIdUtil.getNewItemId(orderId, 0, baseItemId);
        Item equip = dressedEquip.getEquipMap().remove(id);
        ItemEntity itemEntity = equip.convertItemEntity();
        itemEntity.setActorId(actorId);
        itemDAO.deleteQueue(itemEntity);

        playerBackpackService.push(actorId,Lists.newArrayList(itemContext));


        return Result.success();
    }


    /**
     * 获取玩家缓存装备
     * @param playerId
     * @return
     */
    public DressedEquip getDressedEquip(long playerId){
        try {
            return playerDressedEquipCache.get(playerId);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Result<?> dressedEquipInfo(long actorId) {
        DressedEquip dressedEquip = getDressedEquip(actorId);
        EquipPushHelper.pushDressedEquip(actorId,new DressedEquipDTO(dressedEquip).toString());
        return Result.success();
    }

    public Result<?> upgradeEquip(long actorId,long equipId) {
        DressedEquip dressedEquip = getDressedEquip(actorId);
        Item equip = dressedEquip.getEquipMap().get(equipId);
        if(equip != null){
            equip.getExt().put("level",equip.getExt().getIntValue("level") + 1);
            ItemEntity itemEntity = equip.convertItemEntity();
            itemEntity.setActorId(actorId);
            itemDAO.updateQueue(itemEntity);
        }
        EquipPushHelper.pushDressedEquip(actorId,new DressedEquipDTO(dressedEquip).toString());
        EventBusHolder.post(new ActorEvent<>(actorId,EventType.DRESS_EQUIP));
        return Result.success();
    }
}
