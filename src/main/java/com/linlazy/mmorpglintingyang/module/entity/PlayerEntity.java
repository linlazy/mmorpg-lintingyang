package com.linlazy.mmorpglintingyang.module.entity;

import com.linlazy.mmorpglintingyang.server.db.Table;
import lombok.Data;

/**
 * 玩家实体
 * @author linlazy
 */
@Data
@Table("player")
public class PlayerEntity {
    /**
     * 玩家
     */
    private long actorId;

    /**
     * 职业
     */
    private int profession;

    /**
     * 血量
     */
    private int hp;

    /**
     * 蓝
     */
    private int mp;

    /**
     * 金币
     */
    private int gold;
}
