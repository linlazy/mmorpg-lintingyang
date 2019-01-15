package com.linlazy.mmorpg.domain;

import com.linlazy.mmorpg.entity.ItemEntity;
import com.linlazy.mmorpg.event.type.EquipDamageEvent;
import com.linlazy.mmorpg.module.common.event.EventBusHolder;
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

    private int equipType;

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

    /**
     * 装备
     */
    private boolean isDress;


    public Equip(ItemEntity entity) {
        super(entity);
    }

    public Equip(long itemId, int count) {
        super(itemId, count);
    }


    public void modifyDurability() {
        durability--;
        if(durability <= 0){
            durability = 0;
            EventBusHolder.post(new EquipDamageEvent(this));
        }
    }
}
