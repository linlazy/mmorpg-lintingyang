package com.linlazy.mmorpg.dto;

import com.linlazy.mmorpg.domain.Scene;
import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;

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


    public SceneDTO(Scene scene) {
        this.sceneName = scene.getSceneName();
        this.playerDTOSet = scene.getPlayerSet().stream().map(PlayerDTO::new).collect(Collectors.toSet());
        this.monsterDTOSet =  scene.getMonsterSet().stream().map(MonsterDTO::new).collect(Collectors.toSet());
        this.bossDTOSet = scene.getBossSet().stream().map(BossDTO::new).collect(Collectors.toSet());
    }
}
