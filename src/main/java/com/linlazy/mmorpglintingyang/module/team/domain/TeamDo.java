package com.linlazy.mmorpglintingyang.module.team.domain;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * 队伍领域类
 * @author linlazy
 */
@Data
public class TeamDo {

    private long actorId;
    /**
     * 是否队长
     */
    private boolean isCaptain;

    /**
     * 发送邀请ID集合
     */
    private Set<Long>  inviteIdSet = new HashSet<>();

    /**
     * 队员集合
     */
    private Set<Long> teamIdSet = new HashSet<>();


    /**
     * 玩家是否已组队
     * @param actorId
     * @return
     */
    public  boolean hasJoinedTeam(long actorId) {
        return teamIdSet.size() > 0;
    }

    /**
     * 是否队伍已满
     * @return
     */
    public boolean isFull() {
        return teamIdSet.size() > 5;
    }

    /**
     * 是否队伍已解散
     * @return
     */
    public boolean isDisband() {
        return teamIdSet.size() == 0 && inviteIdSet.size() == 0;
    }
}
