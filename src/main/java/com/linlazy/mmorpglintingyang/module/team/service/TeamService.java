package com.linlazy.mmorpglintingyang.module.team.service;

import com.linlazy.mmorpglintingyang.module.team.manager.TeamManager;
import com.linlazy.mmorpglintingyang.module.team.validator.TeamValidator;
import com.linlazy.mmorpglintingyang.server.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TeamService {

    @Autowired
    private TeamValidator teamValidator;
    @Autowired
    private TeamManager teamManager;

    /**
     * 邀请入队
     * @param actorId
     * @param targetId
     * @return
     */
    public Result<?> inviteJoin(long actorId, long targetId) {

        //人员已满
        Result<?> result = teamValidator.isFull(actorId);
        if(result.isFail()){
            return Result.valueOf(result.getCode());
        }

        //玩家已组队
        result = teamValidator.hasJoinedTeam(targetId);
        if(result.isFail()){
            return Result.valueOf(result.getCode());
        }
        return teamManager.inviteTeam(actorId,targetId);
    }

    /**
     * 同意加入
     * @param actorId
     * @param targetId
     * @return
     */
    public Result<?> acceptJoin(long actorId, long targetId) {
        //队伍已解散
        Result<?> disband = teamValidator.isDisband(targetId);
        if(disband.isFail()){
            return Result.valueOf(disband.getCode());
        }
        return teamManager.acceptJoinTeam(actorId,targetId);
    }

    /**
     * 拒绝加入
     * @param actorId
     * @param targetId
     * @return
     */
    public Result<?> rejectJoin(long actorId, long targetId) {
        return teamManager.rejectJoin(actorId,targetId);
    }

    /**
     * 离开队伍
     * @param actorId
     * @return
     */
    public Result<?> leave(long actorId) {
        return teamManager.leave(actorId);
    }

    /**
     * 任命队长
     * @param actorId
     * @param targetId
     * @return
     */
    public Result<?> appointCaptain(long actorId, long targetId) {

        Result<?> notCaption = teamValidator.isNotCaption(actorId);
        if(notCaption.isFail()){
            return Result.valueOf(notCaption.getCode());
        }
        return teamManager.appointCaptain(actorId,targetId);
    }

    public Result<?> shotOff(long actorId, long targetId) {
        return teamManager.shotOff(actorId,targetId);
    }
}
