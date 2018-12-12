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
        int itemId = jsonObject.getIntValue("itemId");
        Integer num = jsonObject.getInteger("num");
        return itemService.useItem(actorId,itemId,num == null? 1 : num);
    }
}
