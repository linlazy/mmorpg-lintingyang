package com.linlazy.mmorpg.entity;

import com.linlazy.mmorpg.server.db.annotation.Cloumn;
import com.linlazy.mmorpg.server.db.annotation.Table;
import com.linlazy.mmorpg.server.db.entity.Entity;
import lombok.Data;

/**
 * @author linlazy
 */
@Data
@Table("guild_offline")
public class GuildOffLineEntity extends Entity {

    /**
     * 公会Id
     */
    @Cloumn(pk = true)
    private long guild;

    /**
     * 消息接受者
     */
    @Cloumn
    private long receiver;
    /**
     * 消息来源
     */
    @Cloumn
    private long sourceId;

}
