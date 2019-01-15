package com.linlazy.mmorpg.module.equip.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.eventbus.Subscribe;
import com.linlazy.mmorpg.constants.EquipType;
import com.linlazy.mmorpg.constants.ItemType;
import com.linlazy.mmorpg.dao.ItemDAO;
import com.linlazy.mmorpg.domain.Equip;
import com.linlazy.mmorpg.domain.Player;
import com.linlazy.mmorpg.domain.PlayerBackpack;
import com.linlazy.mmorpg.event.type.PlayerAttackEvent;
import com.linlazy.mmorpg.event.type.PlayerAttackedEvent;
import com.linlazy.mmorpg.module.common.event.EventBusHolder;
import com.linlazy.mmorpg.module.equip.manager.domain.DressedEquip;
import com.linlazy.mmorpg.server.common.Result;
import com.linlazy.mmorpg.service.PlayerService;
import com.linlazy.mmorpg.utils.RandomUtils;
import com.linlazy.mmorpg.utils.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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
                            .filter(itemEntity -> itemEntity.getItemType() == ItemType.EQUIP)
                            .map(Equip::new)
                            .filter(equip -> equip.isDress())
                            .forEach(equip -> {
                                dressedEquip.getEquipMap().put(equip.getItemId(),equip);
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
        Equip equip = RandomUtils.randomElement(
                player.getDressedEquip().getEquipMap().values()
                    .stream()
                    .filter(equip1 -> equip1.getEquipType() != EquipType.ARMS)
                    .filter(equip1 -> equip1.getDurability() > 0)
                    .collect(Collectors.toList())
        );
        equip.modifyDurability();
        itemDAO.updateQueue(equip.convertItemEntity());
    }

    @Subscribe
    private void handleAttack(PlayerAttackEvent playerAttackEvent) {
        Player player = playerAttackEvent.getPlayer();
        Equip equip = RandomUtils.randomElement(
                player.getDressedEquip().getEquipMap().values()
                        .stream()
                        .filter(equip1 -> equip1.getEquipType() == EquipType.ARMS)
                        .filter(equip1 -> equip1.getDurability() > 0)
                        .collect(Collectors.toList())
        );
        equip.modifyDurability();
        itemDAO.updateQueue(equip.convertItemEntity());
    }


    /**
     * 装备
     * @param actorId
     * @param equipId
     * @return
     */
    public Result<?> equip(long actorId, long equipId){
//        JSONObject itemConfig = itemConfigService.getItemConfig(ItemIdUtil.getBaseItemId(equipId));
//        int itemType = itemConfig.getIntValue("itemType");
//        if( itemType!= ItemType.EQUIP){
//            return Result.valueOf("参数有误");
//        }

        Player player = playerService.getPlayer(actorId);
        DressedEquip dressedEquip = player.getDressedEquip();
        PlayerBackpack backPack = player.getBackPack();
        return Result.success();
    }


//    /**
//     * 卸载装备
//     * @param actorId
//     * @param equipId
//     * @return
//     */
//    public Result<?> unEquip(long actorId, long equipId) {
//        JSONObject itemConfig = itemConfigService.getItemConfig(ItemIdUtil.getBaseItemId(equipId));
//        int itemType = itemConfig.getIntValue("itemType");
//        if(itemType != ItemType.EQUIP){
//            return Result.valueOf("参数有误");
//        }
//
//        Item item = itemDao.getItem(actorId, equipId);
//        EquipDo equipDo = new EquipDo(new ItemDo(item));
//        if(equipDo == null || !equipDo.isDressed()){
//            return Result.valueOf("参数有误");
//        }
//        equipManager.unDressEquipment(actorId,equipId);
//        return Result.success();
//    }

//    /**
//     * 修复装备
//     * @param actorId 玩家ID
//     * @param equipId 装备ID
//     * @return
//     */
//    public Result<FixEquipmentDTO> fixEquipment(long actorId, long equipId) {
////        //校验
////        JSONObject itemConfig = itemConfigService.getItemConfig(ItemIdUtil.getBaseItemId(equipId));
////        int itemType = itemConfig.getIntValue("itemType");
////        if(itemType != ItemType.EQUIP){
////            return Result.valueOf("参数有误");
////        }
////
////
////        FixEquipmentDTO fixEquipmentDTO = new FixEquipmentDTO();
////        //修复耐久度
////        EquipDo equipDo = equipManager.fixEquipment(actorId, equipId);
////        fixEquipmentDTO.setEquipDTO(new EquipDTO(equipDo));
//
//        return Result.success(fixEquipmentDTO);
//    }

    public Result<?> unDressEquip(long actorId,long equipId) {
        Player player = playerService.getPlayer(actorId);
        Equip equip = player.getDressedEquip().getEquipMap().remove(equipId);
        PlayerBackpack backPack = player.getBackPack();
        return null;
    }
}
