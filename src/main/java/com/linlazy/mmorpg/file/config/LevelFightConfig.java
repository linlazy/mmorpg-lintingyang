package com.linlazy.mmorpg.file.config;

import lombok.Data;

/**
 * 等级战斗属性配置
 * @author linlazy
 */
@Data
public class LevelFightConfig {

    /**
     * 职业ID
     */
    private int professionId;

    /**
     * 攻击力
     */
    private int attack;

    /**
     * 防御力
     */
    private int defense;
}
