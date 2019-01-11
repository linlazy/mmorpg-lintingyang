package com.linlazy.mmorpg.domain;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 副本
 * @author linlazy
 */
@Data
public class Copy {

    /**
     * 副本ID
     */
    private long copyId;

    private int sceneId;

    /**
     * 副本Boss信息
     */
    private Map<Long,Boss> copyBossInfoMap = new ConcurrentHashMap<>();

    /**
     * 副本玩家信息
     */
    private Map<Long,PlayerCopyInfo> playerCopyInfoMap = new ConcurrentHashMap<>();

    /**
     * 初始化副本boss信息
     * @return
     * @param copyBoss
     */
    public void initCopyBossInfo(List<Boss> copyBoss) {
        copyBoss.stream()
                .forEach(boss -> {
                    copyBossInfoMap.put(boss.getId(),boss);
                });
    }

    /**
     * 初始化副本玩家信息
     * @return
     * @param team
     */
    public void initCopyPlayerInfo(Team team) {

        Map<Long, PlayerTeamInfo> playerTeamInfoMap = team.getPlayerTeamInfoMap();
        playerTeamInfoMap.values().stream()
                .forEach(playerTeamInfo -> {
                    playerCopyInfoMap.put(playerTeamInfo.getPlayer().getActorId(),new PlayerCopyInfo(copyId,playerTeamInfo.getPlayer()));
                });
    }

    //=================================================================

    /***
     * 是否所有副本玩家都已死亡
     * @return
     */
    public boolean isAllActorDead(){
        return playerCopyInfoMap.values().stream()
                .allMatch(playerCopyInfo -> playerCopyInfo.getPlayer().getHp() == 0);
    }

}
