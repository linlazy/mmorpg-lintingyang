package com.linlazy.mmorpglintingyang.module.pk.arena.entity;

import lombok.Data;

/**
 * @author linlazy
 */
@Data
public class Arena {

    /**
     * 竞技场Id
     */
    private long arenaId;

    /**
     * 竞技场玩家ID
     */
    private long actorId;

    /**
     * 玩家在竞技场所得分数
     */
    private int score;

    /**
     * 玩家在竞技场击杀人数
     */
    private int killNum;

    /**
     * 玩家在竞技场被击杀次数
     */
    private int killedNum;
}
