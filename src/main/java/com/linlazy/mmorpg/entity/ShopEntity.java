package com.linlazy.mmorpg.entity;

import com.linlazy.mmorpg.server.db.annotation.Cloumn;
import com.linlazy.mmorpg.server.db.annotation.Table;
import com.linlazy.mmorpg.server.db.entity.Entity;
import lombok.Data;

/**
 * 商城实体类
 * @author linlazy
 */
@Table("shop")
@Data
public class ShopEntity extends Entity {

    /**
     * 玩家ID
     */
    @Cloumn(pk = true)
    private long actorId;

    /**
     * 商品ID
     */
    @Cloumn(pk = true)
    private long goodsId;

    /**
     * 已购买次数
     */
    @Cloumn
    private int hasBuyTimes;

    /**
     * 下一次重置时间
     */
    @Cloumn
    private long nextResetTime;
}
