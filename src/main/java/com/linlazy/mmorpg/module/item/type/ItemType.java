package com.linlazy.mmorpg.module.item.type;

/**
 * @author linlazy
 */
public interface ItemType {
    /**
     * 消耗道具
     */
    int CONSUME = 1;
    /**
     * 装备道具
     */
    int EQUIP = 2;
    /**
     * 技能道具
     */
    int SKILL = 3;

    /**
     * 普通道具
     */
    int ORDINARY = 4;
}
