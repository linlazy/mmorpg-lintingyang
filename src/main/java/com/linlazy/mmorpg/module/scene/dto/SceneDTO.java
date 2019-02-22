package com.linlazy.mmorpg.module.scene.dto;

import com.google.common.collect.Sets;
import com.linlazy.mmorpg.module.item.dto.ItemDTO;
import com.linlazy.mmorpg.module.scene.domain.Boss;
import com.linlazy.mmorpg.module.scene.copy.domain.Copy;
import com.linlazy.mmorpg.module.scene.domain.Scene;
import com.linlazy.mmorpg.module.playercall.dto.PlayerCallDTO;
import com.linlazy.mmorpg.module.player.dto.PlayerDTO;
import com.linlazy.mmorpg.file.config.SceneConfig;
import com.linlazy.mmorpg.file.service.SceneConfigService;
import com.linlazy.mmorpg.utils.SpringContextUtil;
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
    private  Set<PlayerCallDTO> playerCallDTOSet;
    private  List<ItemDTO> itemDTOList;


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
        this.playerCallDTOSet =scene.getPlayerCallMap().values().stream().map(PlayerCallDTO::new).collect(Collectors.toSet());


        this.itemDTOList = scene.getItemMap().values().stream().map(ItemDTO::new).collect(Collectors.toList());
    }

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("场景名称：").append(sceneName).append("\r\n");

        SceneConfigService sceneConfigService = SpringContextUtil.getApplicationContext().getBean(SceneConfigService.class);
        neighborSet.forEach(sceneId->{
            SceneConfig sceneConfig = sceneConfigService.getSceneConfig(sceneId);
            stringBuilder.append("相邻场景名称: ").append(sceneConfig.getName()).append(" 场景ID:").append(sceneConfig.getSceneId()).append("\r\n");
        });

        for(PlayerDTO playerDTO: playerDTOSet){
            stringBuilder.append("场景玩家：").append(playerDTO.toString()).append("\r\n");
        }
        for(MonsterDTO monsterDTO: monsterDTOSet){
            stringBuilder.append("场景小怪：").append(monsterDTO.toString()).append("\r\n");
        }

        for(BossDTO bossDTO: bossDTOSet){
            stringBuilder.append("场景BOSS：").append(bossDTO.toString()).append("\r\n");
        }

        for(NpcDTO npcDTO: npcDTOSet){
            stringBuilder.append("场景NPC：").append(npcDTO.toString()).append("\r\n");
        }
        for(PlayerCallDTO playerCallDTO: playerCallDTOSet){
            stringBuilder.append("玩家召唤兽：").append(playerCallDTO.toString()).append("\r\n");
        }

        for(ItemDTO itemDTO: itemDTOList){
            stringBuilder.append("掉落道具：").append(itemDTO.toString()).append("\r\n");
        }
        return stringBuilder.toString();
    }
}
