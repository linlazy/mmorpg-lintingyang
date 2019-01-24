//package com.linlazy.mmorpg.module.fight.defense.actor;
//
//import com.alibaba.fastjson.JSONObject;
//import com.linlazy.mmorpg.module.dressEquip.manager.domain.EquipDo;
//import com.linlazy.mmorpg.module.item.manager.backpack.domain.ItemDo;
//import com.linlazy.mmorpg.file.service.ItemConfigService;
//import com.linlazy.mmorpg.module.item.manager.dao.ItemDao;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
///**
// * 装备防御力 = 基础防御力 + 装备等级 * 6
// * @author linlazy
// */
//@Component
//public class EquipActorDefense extends BaseActorDefense {
//
//    @Autowired
//    private ItemDao itemDao;
//    @Autowired
//    private ItemConfigService itemConfigService;
//
//    @Override
//    public int actorDefenseType() {
//        return ActorDefenseType.EQUIP;
//    }
//
//    @Override
//    public int computeDefense(long actorId,JSONObject jsonObject) {
//        return itemDao.getItemSet(actorId).stream()
//                .map(ItemDo::new)
//                .map(EquipDo::new)
//                .filter(equipDo -> equipDo.isDressed() && equipDo.getDurability() > 0)
//                .map(equipDo -> {
//                    JSONObject itemConfig = itemConfigService.getItemConfig(equipDo.getBaseItemId());
//                    int defense = itemConfig.getIntValue("defense");
//                    return defense + equipDo.getLevel() * 6;
//                }).reduce(0,(a,b) -> a + b);
//    }
//}
