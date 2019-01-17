package com.linlazy.mmorpg.logout;


import com.linlazy.mmorpg.domain.Player;
import com.linlazy.mmorpg.server.common.LogoutListener;
import com.linlazy.mmorpg.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

        }

    }
}
