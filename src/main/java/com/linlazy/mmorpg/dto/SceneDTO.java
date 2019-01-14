package com.linlazy.mmorpg.dto;

import lombok.Data;

import java.util.Set;

/**
 * 场景DTO
 * @author linlazy
 */
@Data
public class SceneDTO {

    private final String sceneName;
    private final Set<PlayerDTO> playerDTOSet;
    private final Set<MonsterDTO> monsterDTOSet;
    private final Set<BossDTO> bossDTOSet;

    public SceneDTO(String sceneName, Set<PlayerDTO> playerDTOSet, Set<MonsterDTO> monsterDTOSet, Set<BossDTO> bossDTOSet) {
        this.sceneName = sceneName;
        this.playerDTOSet = playerDTOSet;
        this.monsterDTOSet = monsterDTOSet;
        this.bossDTOSet = bossDTOSet;
    }
}
