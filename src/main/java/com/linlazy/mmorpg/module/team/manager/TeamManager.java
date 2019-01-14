//package com.linlazy.mmorpg.module.team.manager;
//
//import com.linlazy.mmorpg.module.team.constants.TeamOperationType;
//import com.linlazy.mmorpg.module.team.domain.TeamDo;
//import com.linlazy.mmorpg.module.team.push.TeamPushHelper;
//import com.linlazy.mmorpg.server.common.Result;
//import com.linlazy.mmorpg.utils.RandomUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import java.util.Map;
//import java.util.Set;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.stream.Collectors;
//
///**
// * @author linlazy
// */
//@Component
//public class TeamManager {
//
//    private Logger logger = LoggerFactory.getLogger(TeamManager.class);
//
//    private static final Map<Long, TeamDo> ACTOR_ID_TEAMDO_MAP = new ConcurrentHashMap<>();
//
//    /**
//     * actorId邀请targetId入队
//     * @param actorId
//     * @param targetId
//     * @return
//     */
//    public Result<?> inviteTeam(long actorId, long targetId){
//        TeamDo teamDo = getActorTeamDo(actorId);
//        teamDo.getInviteIdSet().add(targetId);
//
//        //推送通知对方
//        TeamPushHelper.pushTeam(targetId,TeamOperationType.INVITE_JOIN);
//        logger.debug("actorId:{},teamInfo:{}",actorId,teamDo);
//        System.out.println("aa");
//        return Result.success();
//    }
//
//    /**
//     * actorId同意加入targetId的队伍
//     * @param actorId
//     * @param targetId
//     * @return
//     */
//    public Result<?> acceptJoinTeam(long actorId , long targetId){
//
//        TeamDo actorTeamDo = getActorTeamDo(actorId);
//        actorTeamDo.getTeamIdSet().add(targetId);
//
//        TeamDo targetTeamDo = getActorTeamDo(targetId);
//        Set<Long> teamIdSet = targetTeamDo.getTeamIdSet();
//        if(teamIdSet.size() == 0){
//            targetTeamDo.setCaptain(true);
//        }
//        teamIdSet.add(actorId);
//        targetTeamDo.getInviteIdSet().remove(actorId);
//
//        //推送通知对方
//        TeamPushHelper.pushTeam(targetId,TeamOperationType.ACCEPT_JOIN);
//
//        logger.debug("actorId:{},teamInfo:{}",actorId,actorTeamDo);
//        logger.debug("actorId:{},teamInfo:{}",targetId,targetTeamDo);
//        return Result.success();
//    }
//
//    /**
//     * actorId拒绝加入targetId的队伍
//     * @param actorId
//     * @param targetId
//     * @return
//     */
//    public Result<?> rejectJoin(long actorId, long targetId) {
//        TeamDo actorTeamDo = getActorTeamDo(targetId);
//        actorTeamDo.getInviteIdSet().remove(actorId);
//
//        //推送通知对方
//        TeamPushHelper.pushTeam(targetId,TeamOperationType.REJECT_JOIN);
//        return Result.success();
//    }
//
//    public TeamDo getActorTeamDo(long actorId) {
//        TeamDo teamDo = ACTOR_ID_TEAMDO_MAP.get(actorId);
//        if(teamDo == null){
//            teamDo = new TeamDo();
//            teamDo.setActorId(actorId);
//            ACTOR_ID_TEAMDO_MAP.put(actorId,teamDo);
//        }
//        return teamDo;
//    }
//
//    public Result<?> leave(long actorId) {
//        TeamDo actorTeamDo = getActorTeamDo(actorId);
//        Set<Long> teamIdSet = actorTeamDo.getTeamIdSet();
//
//        for(Long teamId: teamIdSet){
//            TeamDo targetTeamDo = getActorTeamDo(teamId);
//            targetTeamDo.getTeamIdSet().remove(actorId);
//            //推送通知对方
//            TeamPushHelper.pushTeam(teamId,TeamOperationType.LEAVE);
//        }
//
//        //队长离开，随机给随机给队员
//        if(actorTeamDo.isCaptain()){
//            Long newCaptain = RandomUtils.randomElement(teamIdSet);
//            TeamDo targetTeamDo = getActorTeamDo(newCaptain);
//
//            targetTeamDo.setCaptain(true);
//            actorTeamDo.setCaptain(false);
//
//        }
//
//        actorTeamDo.getTeamIdSet().clear();
//        return Result.success();
//    }
//
//    public Result<?> appointCaptain(long actorId, long targetId) {
//        TeamDo actorTeamDo = getActorTeamDo(actorId);
//        actorTeamDo.setCaptain(false);
//        TeamDo targetTeamDo = getActorTeamDo(targetId);
//        targetTeamDo.setCaptain(true);
//
//        return Result.success();
//    }
//
//    public Result<?> shotOff(long actorId, long targetId) {
//        TeamDo targetTeamDo = getActorTeamDo(targetId);
//        targetTeamDo.getTeamIdSet().clear();
//        //推送通知给踢出对象
//        TeamPushHelper.pushShotOffTeam(targetId,actorId,targetId,TeamOperationType.SHOT_OFF);
//
//        TeamDo actorTeamDo = getActorTeamDo(actorId);
//        actorTeamDo.getTeamIdSet().remove(targetId);
//
//        //推送通知给其他队员
//        Set<Long> teamIdSet = actorTeamDo.getTeamIdSet().stream()
//                .filter(aLong ->aLong != targetId).collect(Collectors.toSet());
//        for(Long targetId1:teamIdSet){
//            TeamDo targetTeamDo1 = getActorTeamDo(targetId1);
//            targetTeamDo1.getTeamIdSet().remove(targetId);
//            TeamPushHelper.pushShotOffTeam(targetId1,actorId,targetId,TeamOperationType.SHOT_OFF);
//        }
//        return Result.success();
//    }
//}
