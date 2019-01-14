package com.linlazy.mmorpg.module.scene.validator;

import com.linlazy.mmorpg.domain.Player;
import com.linlazy.mmorpg.module.scene.config.NpcConfigService;
import com.linlazy.mmorpg.module.scene.config.SceneConfigService;
import com.linlazy.mmorpg.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 场景校验器
 * @author linlazy
 */
@Component
public class SceneValidator {

    @Autowired
    private SceneConfigService sceneConfigService;

    @Autowired
    private NpcConfigService npcConfigService;

    @Autowired
    private PlayerService playerService;

    public boolean hasScene(int sceneId){
        return sceneConfigService.hasScene(sceneId);
    }

    public boolean canMoveToScene(long actorId,int sceneId){
        Player player = playerService.getPlayer(actorId);
        return sceneConfigService.canMoveToTarget(player.getSceneId(),sceneId);
    }

    public boolean hasNPC(long actorId, int npcId) {
        Player player = playerService.getPlayer(actorId);
        int sceneId = player.getSceneId();
        return npcConfigService.hasNPC(sceneId,npcId);
    }

//    public boolean isDifferentScene(long actorId, int entityType, long entityId) {
////        Player player = playerService.getPlayer(actorId);
////        Player player1 = playerService.getPlayer(entityId);
////        SceneDo sceneDo = sceneManager.getSceneDo(actorId);
////        if(entityType == SceneEntityType.PLAYER){
////            return player1.getSceneId() != player.getSceneId();
////        }else {
////            Set<SceneEntity> sceneEntitySet = sceneDo.getSceneEntitySet();
////           return sceneEntitySet.stream()
////                    .noneMatch(sceneEntityDo -> sceneEntityDo.getSceneEntityType() ==entityType && sceneEntityDo.getSceneEntityId() == entityId);
////        }
//    }
}
