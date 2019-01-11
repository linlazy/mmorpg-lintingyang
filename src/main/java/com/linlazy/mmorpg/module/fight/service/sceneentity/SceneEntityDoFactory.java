package com.linlazy.mmorpg.module.fight.service.sceneentity;

import com.linlazy.mmorpg.module.scene.domain.SceneEntity;
import com.linlazy.mmorpg.module.scene.domain.ScenePlayerDo;
import com.linlazy.mmorpg.module.user.manager.UserManager;
import com.linlazy.mmorpg.module.user.manager.entity.User;
import com.linlazy.mmorpg.utils.SpringContextUtil;

/**
 * @author linlazy
 */
public  class SceneEntityDoFactory {

    public static SceneEntity newPlayerSceneEntityDo(long entityId) {
        UserManager userManager = SpringContextUtil.getApplicationContext().getBean(UserManager.class);
        User user = userManager.getUser(entityId);
        return new SceneEntity(new ScenePlayerDo(user));
    }
}
