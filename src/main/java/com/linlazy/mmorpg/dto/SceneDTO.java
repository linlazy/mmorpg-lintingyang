package com.linlazy.mmorpg.dto;

import com.google.common.collect.Sets;
import com.linlazy.mmorpg.domain.Boss;
import com.linlazy.mmorpg.domain.Copy;
import com.linlazy.mmorpg.domain.Scene;
import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 场景DTO
 * @author linlazy
 */
@Data
public class SceneDTO {

    private  String sceneName;
    private  List<Integer> neighborSet;
    private  Set<PlayerDTO> playerDTOSet;
    private  Set<MonsterDTO> monsterDTOSet;
    private  Set<BossDTO> bossDTOSet;
    private  Set<NpcDTO> npcDTOSet;


    public SceneDTO(Scene scene) {
        this.sceneName = scene.getSceneName();
        this.playerDTOSet = scene.getPlayerMap().values().stream().map(PlayerDTO::new).collect(Collectors.toSet());
        this.monsterDTOSet =  scene.getMonsterMap().values().stream().map(MonsterDTO::new).collect(Collectors.toSet());
        if(scene instanceof Copy){
            Copy copy = (Copy) scene;
            Boss boss = copy.getBossList().get(copy.getCurrentBossIndex());
            this.bossDTOSet = Sets.newHashSet(new BossDTO(boss));
        }else {
            this.bossDTOSet =scene .getBossList().stream().map(BossDTO::new).collect(Collectors.toSet());
        }
        this.npcDTOSet = scene.getNpcSet().stream().map(NpcDTO::new).collect(Collectors.toSet());
         this.neighborSet =scene.getNeighborSet();
    }


}
