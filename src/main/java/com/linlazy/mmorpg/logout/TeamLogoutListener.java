//package com.linlazy.mmorpg.logout;
//
//
//import com.linlazy.mmorpg.constants.TeamOperationType;
//import com.linlazy.mmorpg.module.team.push.TeamPushHelper;
//import com.linlazy.mmorpg.server.common.LogoutListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.Set;
//
///**
// * 队员登出监听器
// * @author linlazy
// */
//@Component
//public class TeamLogoutListener implements LogoutListener{
//
//    @Autowired
//    private TeamManager teamManager;
//
//    @Override
//    public void logout(long actorId) {
//        TeamDo actorTeamDo = teamManager.getActorTeamDo(actorId);
//        //通知队员下线
//        Set<Long> teamIdSet = actorTeamDo.getTeamIdSet();
//        teamIdSet.stream().forEach(teamId -> {
//            TeamDo teamIdTeamDo = teamManager.getActorTeamDo(teamId);
//            teamIdTeamDo.getTeamIdSet().remove(actorId);
//            TeamPushHelper.pushTeam(teamId, TeamOperationType.LEAVE);
//        });
//        //清除自身队伍信息
//        actorTeamDo.getTeamIdSet().clear();
//    }
//}
