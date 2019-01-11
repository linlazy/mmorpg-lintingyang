package com.linlazy.mmorpg.module.item.handler;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.server.common.Result;
import com.linlazy.mmorpg.module.item.service.ItemService;
import com.linlazy.mmorpg.server.route.Cmd;
import com.linlazy.mmorpg.server.route.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author linlazy
 */
@Component
@Module("item")
public class ItemHandler {

    @Autowired
    private ItemService itemService;

    @Cmd("useItem")
    public Result<?> useItem(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        int itemId = jsonObject.getIntValue("itemId");
        return itemService.useItem(actorId,itemId,jsonObject);
    }

    /**
     * 获取玩家道具信息
     * @param jsonObject
     * @return
     */
    @Cmd("getBackPackInfo")
    public Result<?> getActorItemInfo(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        return itemService.getActorItemInfo(actorId);
    }

    /**
     * 获得道具
     * @param jsonObject
     * @return
     */
    @Cmd("pushBackPack")
    public Result<?> pushBackPack(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        int itemId = jsonObject.getIntValue("baseItemId");
        int num = jsonObject.getIntValue("num");
        return itemService.pushBackPack(actorId,itemId,num);
    }

    /**
     * 整理背包
     * @param jsonObject
     * @return
     */
    @Cmd("arrangeBackPack")
    public Result<?> arrangeBackPack(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        return itemService.arrangeBackPack(actorId);
    }

}