package com.linlazy.mmorpg.entity;

import com.linlazy.mmorpg.server.db.annotation.Cloumn;
import com.linlazy.mmorpg.server.db.annotation.Table;
import com.linlazy.mmorpg.server.db.entity.Entity;
import lombok.Data;

/**
 * 竞技场实体类
 * @author linlazy
 */
@Data
@Table("arena")
public class ArenaEntity extends Entity {

    /**
     * 竞技场Id
     */
    @Cloumn(pk = true)
    private long arenaId;

    /**
     * 竞技场玩家ID
     */
    @Cloumn(pk = true)
    private long actorId;

    /**
     * 玩家在竞技场所得分数
     */
    @Cloumn
    private int score;

    /**
     * 玩家在竞技场击杀人数
     */
    @Cloumn
    private int killNum;

    /**
     * 玩家在竞技场被击杀次数
     */
    @Cloumn
    private int killedNum;
}
