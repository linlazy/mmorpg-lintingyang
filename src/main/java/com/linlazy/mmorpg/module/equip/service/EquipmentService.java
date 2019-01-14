package com.linlazy.mmorpg.module.equip.service;

import com.google.common.eventbus.Subscribe;
import com.linlazy.mmorpg.domain.Equip;
import com.linlazy.mmorpg.domain.Player;
import com.linlazy.mmorpg.domain.PlayerBackpack;
import com.linlazy.mmorpg.module.common.event.ActorEvent;
import com.linlazy.mmorpg.module.common.event.EventBusHolder;
import com.linlazy.mmorpg.module.equip.manager.EquipManager;
import com.linlazy.mmorpg.module.equip.manager.domain.DressedEquip;
import com.linlazy.mmorpg.module.item.manager.config.ItemConfigService;
import com.linlazy.mmorpg.server.common.Result;
import com.linlazy.mmorpg.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author linlazy
 */
@Component
public class EquipmentService {

    @Autowired
    private DressedEquip dressedEquip;

    @Autowired
    private PlayerService playerService;

    @PostConstruct
    public void init(){
        EventBusHolder.register(this);
    }

    @Subscribe
    public void listenEvent(ActorEvent actorEvent){
        switch (actorEvent.getEventType()){
            case ATTACK:
                //攻击凑效，减少武器耐久度
                handleAttack(actorEvent);
                break;
            case ACTOR_DAMAGE:
                handleAttacked(actorEvent);
                break;
                default:
                    break;
        }
    }

    private void handleAttacked(ActorEvent actorEvent) {
        dressedEquip.consumeDurabilityWithAttacked(actorEvent.getActorId());
    }

    private void handleAttack(ActorEvent actorEvent) {
        dressedEquip.consumeDurabilityWithAttack(actorEvent.getActorId());
    }

    @Autowired
    private EquipManager equipManager;
//    @Autowired
//    private ItemDao itemDao;
    @Autowired
    private ItemConfigService itemConfigService;

//    /**
//     * 装备
//     * @param actorId
//     * @param equipId
//     * @return
//     */
//    public Result<?> equip(long actorId, long equipId){
//        JSONObject itemConfig = itemConfigService.getItemConfig(ItemIdUtil.getBaseItemId(equipId));
//        int itemType = itemConfig.getIntValue("itemType");
//        if( itemType!= ItemType.EQUIP){
//            return Result.valueOf("参数有误");
//        }
//
//        Item item = itemDao.getItem(actorId, equipId);
//        EquipDo equipDo = new EquipDo(new ItemDo(item));
//        if(equipDo == null || equipDo.isDressed()){
//            return Result.valueOf("参数有误");
//        }
//
//        equipManager.dressEquipment(actorId,equipId);
//        return Result.success();
//    }


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
        Equip equip = player.getDressedEquip().getEquipMap().get(equipId);
        PlayerBackpack backPack = player.getBackPack();

        return null;
    }
}
