package com.linlazy.mmorpg.entity;

import com.linlazy.mmorpg.server.db.annotation.Cloumn;
import com.linlazy.mmorpg.server.db.entity.Entity;
import lombok.Data;

/**
 * 技能实体类
 * @author linlazy
 */
@Data
public class SkillEntity extends Entity {
    /**
     * 玩家ID
     */
    @Cloumn(pk = true)
    private long actorId;

    /**
     * 技能ID
     */
    @Cloumn(pk = true)
    private int skillId;

    /**
     * 技能等级
     */
    @Cloumn
    private int level;

    /**
     *  已穿戴
     */
    @Cloumn
    private boolean dressed;

    /**
     * 下一次CDH恢复时间
     */
    @Cloumn
    private long nextCDResumeTimes;
}
