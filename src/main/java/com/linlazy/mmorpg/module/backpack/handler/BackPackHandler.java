//package com.linlazy.mmorpg.module.backpack.handler;
//
//import com.alibaba.fastjson.JSONObject;
//import com.linlazy.mmorpg.module.backpack.dto.PlayerBackPackDTO;
//import com.linlazy.mmorpg.module.backpack.service.BackPackService;
//import com.linlazy.mmorpg.module.backpack.service.PlayerBackpackService;
//import com.linlazy.mmorpg.server.common.Result;
//import com.linlazy.mmorpg.server.route.Cmd;
//import com.linlazy.mmorpg.server.route.Module;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
///**
// * @author linlazy
// */
//@Component
//@Module("backpack")
//public class BackPackHandler {
//
//
//    @Autowired
//    private BackPackService backPackService;
//
//
//    /**
//     * 整理背包
//     * @param jsonObject
//     * @return
//     */
//    @Cmd("arrange")
//    public Result<?> arrangeBackPack(JSONObject jsonObject){
//        long actorId = jsonObject.getLongValue("actorId");
//        int backpackType = jsonObject.getIntValue("backpackType");
//        return backPackService.arrange(actorId,backpackType,jsonObject);
//    }
//
//    /**
//     * 整理背包
//     * @param jsonObject
//     * @return
//     */
//    @Cmd("getActorPackPack")
//    public Result<?> getActorPackPack(JSONObject jsonObject){
//        long actorId = jsonObject.getLongValue("actorId");
//        PlayerBackPackDTO playerBackpack = PlayerBackpackService.getPlayerBackpackDTO(actorId);
//        return Result.success(playerBackpack);
//    }
//}
