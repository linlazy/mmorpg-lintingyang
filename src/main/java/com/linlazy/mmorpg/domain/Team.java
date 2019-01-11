package com.linlazy.mmorpg.domain;

import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 组队领域类
 * @author linlazy
 */
@Data
public class Team {

    /**
     * 队伍ID
     */
    private long teamId;

    /**
     * 队伍玩家信息
     */
    private Map<Long,PlayerTeamInfo> playerTeamInfoMap = new ConcurrentHashMap<>();





}
