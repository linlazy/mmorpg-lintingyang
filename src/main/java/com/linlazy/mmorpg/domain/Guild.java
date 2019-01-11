package com.linlazy.mmorpg.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * 公会
 * @author linlazy
 */
public class Guild {

    /**
     * 公会ID
     */
    private long guild;

    /**
     * 公会等级
     */
    private int level;

    /**
     * 公会仓库
     */
    private GuildWarehouse guildWarehouse;

    /**
     * 公会玩家信息
     */
    private Map<Long, PlayerGuildInfo> guildPlayerMap = new HashMap<>();
}
