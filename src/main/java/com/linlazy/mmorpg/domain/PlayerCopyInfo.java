package com.linlazy.mmorpg.domain;

import lombok.Data;

/**
 * @author linlazy
 */
@Data
public class PlayerCopyInfo {

    /**
     * 副本ID
     */
    private long copyId;

    /**
     * 玩家ID
     */
    private Player player;

    public PlayerCopyInfo(long copyId, Player player) {
        this.copyId = copyId;
        this.player = player;
    }
}
