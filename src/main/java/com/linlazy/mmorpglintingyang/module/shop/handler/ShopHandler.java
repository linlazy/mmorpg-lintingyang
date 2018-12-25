package com.linlazy.mmorpglintingyang.module.shop.handler;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.shop.service.ShopService;
import com.linlazy.mmorpglintingyang.server.common.Result;
import com.linlazy.mmorpglintingyang.server.route.Cmd;
import com.linlazy.mmorpglintingyang.server.route.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
