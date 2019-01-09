package com.linlazy.mmorpglintingyang.module.entity;

import com.linlazy.mmorpglintingyang.server.db.Cloumn;
import com.linlazy.mmorpglintingyang.server.db.Entity;
import com.linlazy.mmorpglintingyang.server.db.Table;
import lombok.Data;

/**
 * 玩家实体
 * @author linlazy
 */
@Data
@Table("player")
public class PlayerEntity extends Entity {

    /**
     * 玩家
     */
    @Cloumn(pk = true)
    private long actorId;

    /**
     * 用户名
     */
    @Cloumn(fk = true)
    private String username;

    /**
     * 密码
     */
    private String password;

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
