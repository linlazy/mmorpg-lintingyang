package com.linlazy.mmorpglintingyang.module.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * 副本玩家信息
 * @author linlazy
 */
public class CopyPlayerInfo {

    /**
     * 副本ID
     */
    private long copyId;

    /**
     * 副本玩家信息
     */
    private Set<Player> playerSet = new HashSet<>();
}
