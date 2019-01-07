package com.linlazy.mmorpglintingyang.module.domain;

import lombok.Data;

import java.util.concurrent.atomic.AtomicLong;

/**
 * BOSS
 * @author linlazy
 */
@Data
public class Boss {

    /**
     * boss标识
     */
    private long id;

    /**
     *  bossId
     */
    private long bossId;

    /**
     * boss血量
     */
    private int hp;


    //=============================================================
    /**
     * 自增ID
     */
    private AtomicLong maxId = new AtomicLong();

    public Boss createBoss(){
        Boss boss = new Boss();
        boss.setId(maxId.incrementAndGet());
        return boss;
    }
}
