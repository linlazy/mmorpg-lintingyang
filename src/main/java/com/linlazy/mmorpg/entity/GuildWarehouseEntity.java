package com.linlazy.mmorpg.entity;


import com.linlazy.mmorpg.server.db.annotation.Cloumn;
import com.linlazy.mmorpg.server.db.annotation.Table;
import com.linlazy.mmorpg.server.db.entity.Entity;
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
    @Cloumn(pk = true)
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
    public void afterReadDB() {
        //todo
    }
}
