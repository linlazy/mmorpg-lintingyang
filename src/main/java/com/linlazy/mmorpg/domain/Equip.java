package com.linlazy.mmorpg.domain;

import lombok.Data;

/**
 * 装备领域类
 * @author linlazy
 */
@Data
public class Equip extends Item{
    /**
     * 道具标识
     */
    private long id;

    /**
     * 等级
     */
    private int level;

    /**
     * 耐久度
     */
    private int durability;

    /**
     * 攻击力
     */
    private int attack;

    /**
     * 防御力
     */
    private int defense;


    public Equip(long itemId, int count) {
        super(itemId, count);
    }
}
