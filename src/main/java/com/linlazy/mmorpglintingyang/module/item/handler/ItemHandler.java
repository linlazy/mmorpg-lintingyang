package com.linlazy.mmorpglintingyang.module.item.handler;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.common.Result;
import com.linlazy.mmorpglintingyang.module.item.service.ItemService;
import com.linlazy.mmorpglintingyang.server.route.Cmd;
import com.linlazy.mmorpglintingyang.server.route.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Module("item")
public class ItemHandler {

    @Autowired
    private ItemService itemService;

    @Cmd("useItem")
    public Result<?> useItem(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        int backPackIndex = jsonObject.getIntValue("backPackIndex");
        Integer num = jsonObject.getIntValue("num");
        return itemService.useItem(actorId,backPackIndex,num == null? 1 : num);
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
    @Cmd("addItem")
    public Result<?> addItem(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        int itemId = jsonObject.getIntValue("baseItemId");
        int num = jsonObject.getIntValue("num");
        return itemService.addItem(actorId,itemId,num);
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
