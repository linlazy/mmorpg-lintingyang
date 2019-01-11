package com.linlazy.mmorpg.domain;

import lombok.Data;

import java.util.Set;

/**
 * 副本玩家信息
 * @author linlazy
 */
@Data
public class CopyPlayerInfo {

    /**
     * 副本ID
     */
    private long copyId;

    /**
     * 副本玩家信息
     */
    private Set<Player> playerSet;
}
