package com.linlazy.mmorpglintingyang.module.equipment.manager;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.common.addition.Addition;
import com.linlazy.mmorpglintingyang.module.common.addition.AdditionService;
import com.linlazy.mmorpglintingyang.module.equipment.constants.EquipStatus;
import com.linlazy.mmorpglintingyang.module.equipment.manager.entity.Equip;
import com.linlazy.mmorpglintingyang.module.item.constants.ItemType;
import com.linlazy.mmorpglintingyang.module.item.manager.ItemManager;
import com.linlazy.mmorpglintingyang.module.item.manager.entity.Item;
import com.linlazy.mmorpglintingyang.utils.ItemIdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class EquipManager {

    @Autowired
    private ItemManager itemManager;
    @Autowired
    private AdditionService additionService;

    /**
     * 玩家已装备集合映射
     */
    private Map<Long, Map<Integer,Equip>> map = new HashMap<>();

    /**
     * 玩家装备集合
     * @param actorId
     * @return
     */
    private  Map<Integer,Equip> initActorEquipment(long actorId){
        map.computeIfAbsent(actorId, k -> new HashMap<>());
        return map.get(actorId);
    }

    public Map<Integer,Equip> getActorEqupment(long actorId){
        return initActorEquipment(actorId);
    }


    /**
     * 获取装备
     * @param actorId
     * @param equipId
     * @return
     *
     */
    public Equip getEquipment(long actorId, long equipId) {
        JSONObject itemConfig = itemManager.getItemConfig(ItemIdUtil.getBaseItemId(equipId));
        if(itemConfig.getIntValue("itemType") != ItemType.Equip){
            return null;
        }
        Item item = itemManager.getItem(actorId, equipId);
        if(item == null){
            return null;
        }

        return new Equip(itemManager.getItem(actorId, equipId));
    }

    /**
     * 卸下装备
     * @param equip
     */
    public void unDressEquipment(long actorId,Equip equip) {
        int baseItemId = ItemIdUtil.getBaseItemId(equip.getEquipId());
        JSONObject itemConfig = itemManager.getItemConfig(baseItemId);
        int equipType = itemConfig.getIntValue("equipType");
        map.get(actorId).remove(equipType);
        //移除装备加成
        List<Addition> additions =equip.getAdditionList();
        additionService.removeAddition(actorId,additions);
        //更新装备状态
        equip.setStatus(EquipStatus.UnEquiped);
        itemManager.updateItem(equip.getItem());
    }


    /**
     * 查找已穿着旧装备
     * @param actorId
     * @param newEquipId
     * @return
     */
    public Equip findOldDressedEquip(long actorId,long newEquipId) {
        int baseItemId = ItemIdUtil.getBaseItemId(newEquipId);
        JSONObject itemConfig = itemManager.getItemConfig(baseItemId);
        int equipType = itemConfig.getIntValue("equipType");
        return getActorEqupment(actorId).get(equipType);
    }

    /**
     * 装扮装备
     * @param equipment
     */
    public void dressEquipment(long actorId,Equip equipment) {
        int baseItemId = ItemIdUtil.getBaseItemId(equipment.getEquipId());
        JSONObject itemConfig = itemManager.getItemConfig(baseItemId);
        int equipType = itemConfig.getIntValue("equipType");
        map.get(actorId).put(equipType,equipment);

         //增加装备加成
        List<Addition> additions = equipment.getAdditionList();
        additionService.addAddition(actorId,additions);
        //更新装备状态
        equipment.setStatus(EquipStatus.Equiped);
        itemManager.updateItem(equipment.getItem());
    }
}
