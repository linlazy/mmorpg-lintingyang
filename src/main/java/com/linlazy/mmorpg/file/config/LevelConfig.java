package com.linlazy.mmorpg.file.config;

import lombok.Data;

/**
 * 等级配置
 * @author linlazy
 */
@Data
public class LevelConfig {

    /**
     * 等级
     */
    private int level;

    /**
     * 经验
     */
    private int maxExp;
}
