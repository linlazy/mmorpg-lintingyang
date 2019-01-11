package com.linlazy.mmorpg.module.shop.handler;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.module.shop.service.ShopService;
import com.linlazy.mmorpg.server.common.Result;
import com.linlazy.mmorpg.server.route.Cmd;
import com.linlazy.mmorpg.server.route.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author linlazy
 */
@Component
@Module("shop")
public class ShopHandler {

    @Autowired
    private ShopService shopService;


    /**
     * 商城购买
     * @param jsonObject
     * @return
     */
    @Cmd("buy")
    public Result<?> buy(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        long goodsId = jsonObject.getLongValue("goodsId");
        int num = jsonObject.getIntValue("num");
        return shopService.buy(actorId,goodsId,num);
    }
}
