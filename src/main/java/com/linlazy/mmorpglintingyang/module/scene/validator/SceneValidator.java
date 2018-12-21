package com.linlazy.mmorpglintingyang.module.scene.validator;

import com.linlazy.mmorpglintingyang.module.scene.config.NpcConfigService;
import com.linlazy.mmorpglintingyang.module.scene.config.SceneConfigService;
import com.linlazy.mmorpglintingyang.module.user.manager.dao.UserDao;
import com.linlazy.mmorpglintingyang.module.user.manager.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 场景校验器
 */
@Component
public class SceneValidator {

    @Autowired
    private SceneConfigService sceneConfigService;

    @Autowired
    private NpcConfigService npcConfigService;

    @Autowired
    private UserDao userDao;

    public boolean hasScene(int sceneId){
        return sceneConfigService.hasScene(sceneId);
    }

    public boolean canMoveToScene(long actorId,int sceneId){
        User user = userDao.getUser(actorId);
        return sceneConfigService.canMoveToTarget(user.getSceneId(),sceneId);
    }

    public boolean hasNPC(long actorId, int npcId) {
        int sceneId = userDao.getUser(actorId).getSceneId();
        return npcConfigService.hasNPC(sceneId,npcId);
    }
}
