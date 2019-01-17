package com.linlazy.mmorpg.service;

import com.linlazy.mmorpg.domain.Player;
import com.linlazy.mmorpg.domain.PlayerTeamInfo;
import com.linlazy.mmorpg.domain.Team;
import com.linlazy.mmorpg.dto.TeamDTO;
import com.linlazy.mmorpg.push.TeamPushHelper;
import com.linlazy.mmorpg.server.common.Result;
import com.linlazy.mmorpg.utils.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private Logger logger = LoggerFactory.getLogger(TeamService.class);


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


    public Team getTeamByactorId(long actorId){
        Long teamId = playerTeamIdMap.get(actorId);
        return teamMap.get(teamId);
    }

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


    /**
     * 邀请入队
     * @param actorId
     * @param targetId
     * @return
     */
    public Result<?> inviteJoin(long actorId, long targetId) {
        Player player = playerService.getPlayer(actorId);
        if(!SessionManager.isOnline(targetId)){
            return Result.valueOf("玩家不在线");
        }
        //人员已满
//        Result<?> result = isNotFull(actorId);
//        if(result.isFail()){
//            return Result.valueOf(result.getCode());
//        }

        //玩家已组队
        Result<?> result = notHasJoinedTeam(targetId);
        if(result.isFail()){
            return Result.valueOf(result.getCode());
        }

        TeamPushHelper.pushTeam(targetId,String.format("玩家【%s】邀请你加入队伍",player.getName()));
        return Result.success();
    }

    /**
     * 同意加入
     * @param actorId
     * @param targetId
     * @return
     */
    public Result<?> acceptJoin(long actorId, long targetId) {
        Player player = playerService.getPlayer(actorId);


        Long teamId = playerTeamIdMap.get(targetId);
        if(teamId == null){
            Team team = createTeam(targetId);
            team.addPlayer(playerService.getPlayer(actorId));
            playerTeamIdMap.put(actorId,team.getTeamId());
        }

        //推送通知对方
        TeamPushHelper.pushTeam(targetId, String.format("玩家【%s】接受了你的队伍邀请",player.getName()));

        return Result.success();
    }
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


    /**
     * 人员已满
     * @param actorId
     * @return
     */
    public Result<?> isNotFull(long actorId) {
        Long teamId = playerTeamIdMap.get(actorId);
        Team team = teamMap.get(teamId);
        if(team.isFull()){
            return Result.valueOf("队伍人员已满");
        }
        return Result.success();
    }

    /**a
     * 玩家已组队
     * @param actorId
     * @return
     */
    public Result<?> notHasJoinedTeam(long actorId) {
        Long teamId = playerTeamIdMap.get(actorId) ;
        if(teamId != null){
            return Result.valueOf("玩家已组队");
        }
        return Result.success();
    }

    public Result<?> teamInfo(long actorId) {
        Team team = playerService.getPlayer(actorId).getTeam();

        return Result.success(new TeamDTO(team).toString());
    }

    public boolean isTeam(long actorId) {
        return playerTeamIdMap.get(actorId) != null;
    }
}
