package com.linlazy.mmorpglintingyang.module.equipment.handler;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.server.common.Result;
import com.linlazy.mmorpglintingyang.module.equipment.service.EquipmentService;
import com.linlazy.mmorpglintingyang.server.route.Cmd;
import com.linlazy.mmorpglintingyang.server.route.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Module("equipment")
@Component
public class EquipmentHandler {

    @Autowired
    private EquipmentService equipmentService;

    /**
     * 获取装备配置信息
     * @param jsonObject
     * @return
     */
    @Cmd("getEquipConfigInfo")
    public Result<?> getEquipConfigInfo(JSONObject jsonObject){

        return Result.success();
    }

    /**
     * 获取玩家装备信息
     * @param jsonObject
     * @return
     */
    @Cmd("getActorEquipInfo")
    public Result<?> getActorEquipInfo(JSONObject jsonObject){

        return Result.success();
    }

    /**
     * 穿着装备
     * @param jsonObject
     * @return
     */
    @Cmd("equip")
    public Result<?> equip(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        long equipId = jsonObject.getLongValue("itemId");

        return equipmentService.equip(actorId,equipId);
    }

    /**
     * 卸下装备
     * @param jsonObject
     * @return
     */
    @Cmd("unEquip")
    public Result<?> unEquip(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        long equipId = jsonObject.getLongValue("itemId");
        return equipmentService.unEquip(actorId,equipId);
    }


    /**
     * 修理装备
     * @param jsonObject
     * @return
     */
    @Cmd("fixEquipment")
    public Result<?> fixEquipment(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        long equipId = jsonObject.getLongValue("equipId");
        return equipmentService.fixEquipment(actorId,equipId);
    }
}
