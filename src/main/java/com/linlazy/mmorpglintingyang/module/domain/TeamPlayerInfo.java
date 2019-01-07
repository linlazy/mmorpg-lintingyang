package com.linlazy.mmorpglintingyang.module.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * 队伍玩家信息
 * @author linlazy
 */
public class TeamPlayerInfo {

    /**
     * 队伍ID
     */
    private long teamId;

    /**
     * 玩家队伍信息
     */
    private Set<PlayerTeamInfo> playerTeamInfo = new HashSet();
}
