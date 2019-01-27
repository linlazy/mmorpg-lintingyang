package com.linlazy.mmorpg.module.team.logout;


import com.linlazy.mmorpg.module.player.domain.Player;
import com.linlazy.mmorpg.domain.PlayerTeamInfo;
import com.linlazy.mmorpg.module.team.push.TeamPushHelper;
import com.linlazy.mmorpg.server.common.LogoutListener;
import com.linlazy.mmorpg.module.player.service.PlayerService;
import com.linlazy.mmorpg.utils.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 队员登出监听器
 * @author linlazy
 */
@Component
public class TeamLogoutListener implements LogoutListener{

    @Autowired
    private PlayerService playerService;

    @Override
    public void logout(long actorId) {
        Player player = playerService.getPlayer(actorId);
        if(player.isTeam()){
            Map<Long, PlayerTeamInfo> playerTeamInfoMap = player.getTeam().getPlayerTeamInfoMap();
            PlayerTeamInfo remove = playerTeamInfoMap.remove(actorId);
            if(remove.isCaption()){
                PlayerTeamInfo playerTeamInfo = RandomUtils.randomElement(playerTeamInfoMap.values());
                playerTeamInfo.setCaption(true);
                playerTeamInfoMap.values().stream()
                        .forEach(playerTeamInfo1 -> {
                            TeamPushHelper.pushTeamCaption(playerTeamInfo1.getPlayer().getActorId(),String.format("玩家【%s】成为了队长",playerTeamInfo1.getPlayer().getName()));
                        });
            }
            playerTeamInfoMap.values().stream()
                    .forEach(playerTeamInfo1 -> {
                        TeamPushHelper.pushLeaveTeam(playerTeamInfo1.getPlayer().getActorId(),String.format("玩家【%s】离开了队伍",remove.getPlayer().getName()));
                    });
        }

    }
}
