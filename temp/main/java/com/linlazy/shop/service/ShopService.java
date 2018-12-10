package com.linlazy.shop.service;


import com.linlazy.common.GameCode;
import com.linlazy.common.Result;
import com.linlazy.shop.config.ShopConfigService;
import com.linlazy.shop.constants.ShopCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShopService {


    @Autowired
    private ShopConfigService shopConfigService;

    /**
     * 购买商品
     */
    public Result buy(int goodsId, int num){

        // 1.商品是否存在
        if(shopConfigService.hasGoods(goodsId)){
            return Result.valueOf(GameCode.ARGS_ERROR);
        }

        if(num <= 0){
            return Result.valueOf(GameCode.ARGS_ERROR);
        }

        //剩余购买次数是否足够
        int remainBuyTimes = 0;
        if(num > remainBuyTimes){
            return Result.valueOf(ShopCode.SHOP_NOT_ENGOUHT);
        }
        return Result.success();
    }
}
