package com.linlazy.mmorpglintingyang.module.entity;

import com.linlazy.mmorpglintingyang.server.db.Cloumn;
import com.linlazy.mmorpglintingyang.server.db.Entity;
import com.linlazy.mmorpglintingyang.server.db.Table;
import lombok.Data;

/**
 * 公会仓库实体
 * @author linlazy
 */
@Data
@Table("guild_warehouse")
public class GuildWarehouseEntity extends Entity {

    /**
     * 公会ID
     */
    @Cloumn(pk = true)
    private long guildId;

    /**
     * 道具ID
     */
    @Cloumn
    private long itemId;

    /**
     * 数量
     */
    @Cloumn
    private int count;

    /**
     * 扩展属性
     */
    @Cloumn
    private String ext;

    /**
     * 可折叠
     */
    private boolean superPosition;

    /**
     * 折叠上限
     */
    private int superPositionUp;


    @Override
    protected void afterReadDB() {
        //todo
    }
}
