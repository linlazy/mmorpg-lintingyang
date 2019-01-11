package com.linlazy.mmorpg.module.scene.validator;

import com.linlazy.mmorpg.module.scene.config.NpcConfigService;
import com.linlazy.mmorpg.module.scene.config.SceneConfigService;
import com.linlazy.mmorpg.module.scene.constants.SceneEntityType;
import com.linlazy.mmorpg.module.scene.domain.SceneDo;
import com.linlazy.mmorpg.module.scene.domain.SceneEntity;
import com.linlazy.mmorpg.module.scene.manager.SceneManager;
import com.linlazy.mmorpg.module.user.manager.UserManager;
import com.linlazy.mmorpg.module.user.manager.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

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
    private UserManager userManager;
    @Autowired
    private SceneManager sceneManager;

    public boolean hasScene(int sceneId){
        return sceneConfigService.hasScene(sceneId);
    }

    public boolean canMoveToScene(long actorId,int sceneId){
        User user = userManager.getUser(actorId);
        return sceneConfigService.canMoveToTarget(user.getSceneId(),sceneId);
    }

    public boolean hasNPC(long actorId, int npcId) {
        int sceneId = userManager.getUser(actorId).getSceneId();
        return npcConfigService.hasNPC(sceneId,npcId);
    }

    public boolean isDifferentScene(long actorId, int entityType, long entityId) {
        SceneDo sceneDo = sceneManager.getSceneDo(actorId);
        if(entityType == SceneEntityType.PLAYER){
            return userManager.getUser(entityId).getSceneId() != userManager.getUser(actorId).getSceneId();
        }else {
            Set<SceneEntity> sceneEntitySet = sceneDo.getSceneEntitySet();
           return sceneEntitySet.stream()
                    .noneMatch(sceneEntityDo -> sceneEntityDo.getSceneEntityType() ==entityType && sceneEntityDo.getSceneEntityId() == entityId);
        }
    }
}
