package com.linlazy.mmorpglintingyang.module.entity;

import com.linlazy.mmorpglintingyang.server.db.Cloumn;
import com.linlazy.mmorpglintingyang.server.db.Entity;
import com.linlazy.mmorpglintingyang.server.db.Table;
import lombok.Data;

/**
 * @author linlazy
 */
@Table("item")
@Data
public class ItemEntity extends Entity {


    @Cloumn(pk = true)
    private long itemId;
    @Cloumn(fk = true)
    private long actorId;

    @Cloumn
    private int count;

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
