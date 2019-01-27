package com.linlazy.mmorpg.module.team.dto;

import com.linlazy.mmorpg.module.player.domain.Player;
import com.linlazy.mmorpg.domain.PlayerTeamInfo;
import com.linlazy.mmorpg.module.team.domain.Team;
import lombok.Data;

import java.util.Collection;

/**
 * @author linlazy
 */
@Data
public class TeamDTO {

     private Collection<PlayerTeamInfo> playerTeamInfos;

    public TeamDTO(Team team) {
        playerTeamInfos = team.getPlayerTeamInfoMap().values();
    }

    @Override
    public String toString() {
         StringBuilder stringBuilder = new StringBuilder("队伍信息\n");
        playerTeamInfos.stream()
                .forEach(playerTeamInfo -> {
                    Player player = playerTeamInfo.getPlayer();
                    stringBuilder.append("玩家：")
                            .append("【").append(player.getName()).append("】")
                            .append("血量：").append(player.getHp());
                            if(playerTeamInfo.isCaption()){
                                stringBuilder.append(" 队长");
                            }
                    stringBuilder.append("\n");
                });
        return stringBuilder.toString();
    }
}
