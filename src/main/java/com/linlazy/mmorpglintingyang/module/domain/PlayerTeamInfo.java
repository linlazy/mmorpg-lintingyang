package com.linlazy.mmorpglintingyang.module.domain;

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
    private long actorId;
    /**
     * 队伍ID
     */
    private long teamId;

    /**是否是队长
     *
     */
    private boolean isCaption;

    public PlayerTeamInfo(long actorId, long teamId, boolean isCaption) {
        this.actorId = actorId;
        this.teamId = teamId;
        this.isCaption = isCaption;
    }
}
