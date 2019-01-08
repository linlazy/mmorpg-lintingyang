package com.linlazy.mmorpglintingyang.module.domain;

import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

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
    private Map<Long,PlayerTeamInfo> teamPlayerInfo=  new ConcurrentHashMap<>();


    // ============================================================================================

    /**
     * 玩家队伍映射
     */
    private static final Map<Long, Team>  actorIdTeamMap= new ConcurrentHashMap<>();
    /**
     * 队伍映射
     */
    private static final Map<Long, Team>  teamMap= new ConcurrentHashMap<>();
    /**
     * 当前最大队伍编号
     */
    private AtomicLong maxTeamId = new AtomicLong();

    /**
     * 玩家队伍信息
     * @param actorId
     * @return
     */
    public static PlayerTeamInfo getPlayerTeamInfo(long actorId) {
        return actorIdTeamMap.get(actorId).getTeamPlayerInfo().values().stream()
                .filter(playerTeamInfo -> playerTeamInfo.getActorId() == actorId)
                .findFirst().get();
    }

    /**
     * 获取队伍信息
     * @param teamId
     * @return
     */
    public static Team getTeam(long teamId) {
        return teamMap.get(teamId);
    }

    /**
     * 创建队伍
     * @return
     */
    public Team createTeam(long actorId){
        Team team = new Team();

        //初始化队伍ID
        team.setTeamId(maxTeamId.incrementAndGet());
        //初始化队伍玩家信息
        team.getTeamPlayerInfo().put(actorId,new PlayerTeamInfo(actorId,team.getTeamId(),true));

        teamMap.put(team.getTeamId(),team);
        actorIdTeamMap.put(actorId,team);
        return team;
    }

    /**
     * 加入队伍
     * @return
     */
    public boolean joinTeam(long teamId, long actorId){
        Team team = getTeam(teamId);
        //加入队伍玩家信息
        team.getTeamPlayerInfo().put(actorId,new PlayerTeamInfo(actorId,team.getTeamId(),false));
        actorIdTeamMap.put(actorId,team);
        return true;
    }

    /**
     * 离开队伍
     * @return
     */
    public boolean leaveTeam(long teamId, long actorId){
        Team team = getTeam(teamId);
        //离开队伍玩家信息
        team.getTeamPlayerInfo().remove(actorId);
        actorIdTeamMap.remove(actorId);
        return true;
    }


}
