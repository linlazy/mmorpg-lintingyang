package com.linlazy.mmorpg.module.team.service;

import com.linlazy.mmorpg.domain.Player;
import com.linlazy.mmorpg.domain.PlayerTeamInfo;
import com.linlazy.mmorpg.domain.Team;
import com.linlazy.mmorpg.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author linlazy
 */
@Component
public class TeamService {




    @Autowired
    private PlayerService playerService;

    //=========================================================================================================
    /**
     * 队伍映射
     */
    private static Map<Long,Team> teamMap = new ConcurrentHashMap<>();
    /**
     * 玩家队伍信息
     */
    private Map<Long,Long> playerTeamIdMap=  new ConcurrentHashMap<>();
    /**
     * 当前最大队伍编号
     */
    private AtomicLong maxTeamId = new AtomicLong();




    public Team getTeam(long teamId){
        return teamMap.get(teamId);
    }

    public PlayerTeamInfo getPlayerTeamInfo(long actorId){
        Long teamId = playerTeamIdMap.get(actorId);
        return getTeam(teamId).getPlayerTeamInfoMap().get(actorId);
    }


    public boolean isSameTeam(long playerId,long otherId){
        long teamId = playerTeamIdMap.get(playerId);
        long otherTeamId = playerTeamIdMap.get(otherId);
        return teamId == otherTeamId;
    }

    //=========================================================================================================

    /**
     * 创建队伍
     * @return
     */
    public Team createTeam(long actorId){
        Team team = new Team();

        //初始化队伍ID
        team.setTeamId(maxTeamId.incrementAndGet());
        //初始化队伍玩家信息
        Player player = playerService.getPlayer(actorId);
        team.getPlayerTeamInfoMap().put(actorId,new PlayerTeamInfo(player,team.getTeamId(),true));

        teamMap.put(team.getTeamId(),team);
        playerTeamIdMap.put(actorId,team.getTeamId());
        return team;
    }

//
//    /**
//     * 邀请入队
//     * @param actorId
//     * @param targetId
//     * @return
//     */
//    public Result<?> inviteJoin(long actorId, long targetId) {
//
//        if(!SessionManager.isOnline(targetId)){
//            return Result.valueOf("玩家不在线");
//        }
//        //人员已满
//        Result<?> result = teamValidator.isNotFull(actorId);
//        if(result.isFail()){
//            return Result.valueOf(result.getCode());
//        }
//
//        //玩家已组队
//        result = teamValidator.hasJoinedTeam(targetId);
//        if(result.isFail()){
//            return Result.valueOf(result.getCode());
//        }
//        return teamManager.inviteTeam(actorId,targetId);
//    }
//
//    /**
//     * 同意加入
//     * @param actorId
//     * @param targetId
//     * @return
//     */
//    public Result<?> acceptJoin(long actorId, long targetId) {
//        //队伍已解散
//        Result<?> disband = teamValidator.isDisband(targetId);
//        if(disband.isFail()){
//            return Result.valueOf(disband.getCode());
//        }
//        return teamManager.acceptJoinTeam(actorId,targetId);
//    }
//
//    /**
//     * 拒绝加入
//     * @param actorId
//     * @param targetId
//     * @return
//     */
//    public Result<?> rejectJoin(long actorId, long targetId) {
//        return teamManager.rejectJoin(actorId,targetId);
//    }
//
//    /**
//     * 离开队伍
//     * @param actorId
//     * @return
//     */
//    public Result<?> leave(long actorId) {
//        return teamManager.leave(actorId);
//    }
//
//    /**
//     * 任命队长
//     * @param actorId
//     * @param targetId
//     * @return
//     */
//    public Result<?> appointCaptain(long actorId, long targetId) {
//
//        Result<?> notCaption = teamValidator.isNotCaption(actorId);
//        if(notCaption.isFail()){
//            return Result.valueOf(notCaption.getCode());
//        }
//        return teamManager.appointCaptain(actorId,targetId);
//    }
//
//    public Result<?> shotOff(long actorId, long targetId) {
//        return teamManager.shotOff(actorId,targetId);
//    }


}
