package com.linlazy.mmorpg.module.player.domain;

import com.linlazy.mmorpg.module.shop.domain.Shop;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author linlazy
 */
@Data
public class PlayerShop {

    private Map<Long, Shop> map = new HashMap<>();


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        map.values().forEach(shop -> {
            stringBuilder.append(shop.toString()).append("\r\n");
        });

        return stringBuilder.toString();
    }
}
