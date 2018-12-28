package com.linlazy.mmorpglintingyang.module.team.logout;

import com.linlazy.mmorpglintingyang.module.team.constants.TeamOperationType;
import com.linlazy.mmorpglintingyang.module.team.domain.TeamDo;
import com.linlazy.mmorpglintingyang.module.team.manager.TeamManager;
import com.linlazy.mmorpglintingyang.module.team.push.TeamPushHelper;
import com.linlazy.mmorpglintingyang.server.common.LogoutListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 队员登出监听器
 */
@Component
public class TeamLogoutListener implements LogoutListener{

    @Autowired
    private TeamManager teamManager;

    @Override
    public void logout(long actorId) {
        TeamDo actorTeamDo = teamManager.getActorTeamDo(actorId);
        //通知队员下线
        Set<Long> teamIdSet = actorTeamDo.getTeamIdSet();
        teamIdSet.stream().forEach(teamId -> {
            TeamDo teamIdTeamDo = teamManager.getActorTeamDo(teamId);
            teamIdTeamDo.getTeamIdSet().remove(actorId);
            TeamPushHelper.pushTeam(teamId, TeamOperationType.LEAVE);
        });
        //清除自身队伍信息
        actorTeamDo.getTeamIdSet().clear();
    }
}
