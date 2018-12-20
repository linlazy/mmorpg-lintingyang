package com.linlazy.mmorpglintingyang.module.fight.skill.entity;

import lombok.Data;

@Data
public class Skill {

    /**
     * 玩家ID
     */
    private long actorId;

    /**
     * 技能ID
     */
    private int skillId;

    /**
     * 技能等级
     */
    private int level;

    /**
     *  已穿戴
     */
    private boolean dressed;

    /**
     * 下一次CDH恢复时间
     */
    private long nextCDResumeTimes;
}
