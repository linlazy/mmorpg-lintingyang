package com.linlazy.mmorpg.shop.count;

import com.linlazy.mmorpg.domain.Shop;
import com.linlazy.mmorpg.server.common.Result;
import com.linlazy.mmorpg.shop.count.reset.BaseResetCount;

/**
 * @author linlazy
 */
public class ShopBuyCount {

    /**
     * 是否有足够次数
     * @return
     */
    public static Result<?> isEnough(Shop shop){
        //重置处理
        BaseResetCount baseResetCount = BaseResetCount.getBaseResetCount(shop.getResetType());
        baseResetCount.doReset(shop);
        if(shop.getHasBuyCount() >= shop.getLimitBuyCount()){
            return Result.valueOf("购买次数不足");
        }

        return Result.success();
    }

}
