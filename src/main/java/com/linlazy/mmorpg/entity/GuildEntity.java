package com.linlazy.mmorpg.entity;

import com.linlazy.mmorpg.server.db.annotation.Cloumn;
import com.linlazy.mmorpg.server.db.annotation.Table;
import com.linlazy.mmorpg.server.db.entity.Entity;
import lombok.Data;

/**
 * 公会实体类
 * @author linlazy
 */
@Data
@Table("guild")
public class GuildEntity extends Entity {
    /**
     * 公会ID
     */
    @Cloumn(pk = true)
    private long guildId;

    /**
     * 金币
     */
    @Cloumn
    private int gold;

    /**
     * 公会等级
     */
    @Cloumn
    private int level;
}
