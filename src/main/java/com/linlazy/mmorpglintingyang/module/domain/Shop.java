package com.linlazy.mmorpglintingyang.module.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * 商城领域类
 * @author linlazy
 */
public class Shop {

    /**
     * 商品Id
     */
    private long goodsId;

    /**
     * 玩家商品信息
     */
    private Map<Long,PlayerShopInfo> playerShopInfoMap = new HashMap<>();
}
