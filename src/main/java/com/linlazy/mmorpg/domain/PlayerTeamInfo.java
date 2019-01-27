package com.linlazy.mmorpg.domain;

import com.linlazy.mmorpg.module.player.domain.Player;
import lombok.Data;

/**
 * 玩家队伍信息
 * @author linlazy
 */
@Data
public class PlayerTeamInfo {

    /**
     * 玩家ID
     */
    private Player player;
    /**
     * 队伍ID
     */
    private long teamId;

    /**是否是队长
     *
     */
    private boolean isCaption;

    public PlayerTeamInfo(Player player, long teamId, boolean isCaption) {
        this.player = player;
        this.teamId = teamId;
        this.isCaption = isCaption;
    }
}
