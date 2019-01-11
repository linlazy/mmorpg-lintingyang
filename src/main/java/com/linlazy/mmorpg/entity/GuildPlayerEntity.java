package com.linlazy.mmorpg.entity;

import com.linlazy.mmorpg.server.db.annotation.Cloumn;
import com.linlazy.mmorpg.server.db.annotation.Table;
import com.linlazy.mmorpg.server.db.entity.Entity;
import lombok.Data;

/**
 * 公会玩家实体类
 * @author linlazy
 */
@Data
@Table("guild_player")
public class GuildPlayerEntity extends Entity {

    /**
     * 公会ID
     */
    @Cloumn(pk = true)
    private long guildId;
    /**
     * 玩家ID
     */
    @Cloumn
    private long actorId;

    /**
     * 权限级别
     */
    @Cloumn
    private int authLevel;
}
