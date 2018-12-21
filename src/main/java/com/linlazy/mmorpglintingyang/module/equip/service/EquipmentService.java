package com.linlazy.mmorpglintingyang.module.equip.service;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.equip.dto.EquipDTO;
import com.linlazy.mmorpglintingyang.module.equip.dto.FixEquipmentDTO;
import com.linlazy.mmorpglintingyang.module.equip.manager.EquipManager;
import com.linlazy.mmorpglintingyang.module.equip.manager.domain.EquipDo;
import com.linlazy.mmorpglintingyang.module.item.constants.ItemType;
import com.linlazy.mmorpglintingyang.module.item.manager.config.ItemConfigService;
import com.linlazy.mmorpglintingyang.module.item.manager.dao.ItemDao;
import com.linlazy.mmorpglintingyang.module.item.manager.entity.Item;
import com.linlazy.mmorpglintingyang.server.common.Result;
import com.linlazy.mmorpglintingyang.utils.ItemIdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EquipmentService {

    @Autowired
    private EquipManager equipManager;
    @Autowired
    private ItemDao itemDao;
    @Autowired
    private ItemConfigService itemConfigService;

    /**
     * 装备
     * @param actorId
     * @param equipId
     * @return
     */
    public Result<?> equip(long actorId, long equipId){
        JSONObject itemConfig = itemConfigService.getItemConfig(ItemIdUtil.getBaseItemId(equipId));
        if(itemConfig.getIntValue("itemType") != ItemType.Equip){
            return Result.valueOf("参数有误");
        }

        Item item = itemDao.getItem(actorId, equipId);
        EquipDo equipDo = new EquipDo(item);
        if(equipDo == null || equipDo.isDressed()){
            return Result.valueOf("参数有误");
        }

        equipManager.dressEquipment(actorId,equipId);
        return Result.success();
    }


    /**
     * 卸载装备
     * @param actorId
     * @param equipId
     * @return
     */
    public Result<?> unEquip(long actorId, long equipId) {
        JSONObject itemConfig = itemConfigService.getItemConfig(ItemIdUtil.getBaseItemId(equipId));
        if(itemConfig.getIntValue("itemType") != ItemType.Equip){
            return Result.valueOf("参数有误");
        }

        Item item = itemDao.getItem(actorId, equipId);
        EquipDo equipDo = new EquipDo(item);
        if(equipDo == null || !equipDo.isDressed()){
            return Result.valueOf("参数有误");
        }
        equipManager.unDressEquipment(actorId,equipId);
        return Result.success();
    }

    /**
     * 修复装备
     * @param actorId 玩家ID
     * @param equipId 装备ID
     * @return
     */
    public Result<FixEquipmentDTO> fixEquipment(long actorId, long equipId) {
        //校验
        JSONObject itemConfig = itemConfigService.getItemConfig(ItemIdUtil.getBaseItemId(equipId));
        if(itemConfig.getIntValue("itemType") != ItemType.Equip){
            return Result.valueOf("参数有误");
        }


        FixEquipmentDTO fixEquipmentDTO = new FixEquipmentDTO();
        //修复耐久度
        EquipDo equipDo = equipManager.fixEquipment(actorId, equipId);
        fixEquipmentDTO.setEquipDTO(new EquipDTO(equipDo));

        return Result.success(fixEquipmentDTO);
    }

}
