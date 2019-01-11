package com.linlazy.mmorpg.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * 竞技场
 * @author linlazy
 */
public class Arena {

    /**
     * 竞技场ID
     */
    private long arenaId;

    /**
     * 玩家竞技场信息
     */
    private Map<Long, PlayerArenaInfo> playerArenaInfoMap = new HashMap<>();
}
