package com.linlazy.mmorpglintingyang.module.fight.service.sceneentity;

import com.linlazy.mmorpglintingyang.module.scene.domain.SceneEntityDo;
import com.linlazy.mmorpglintingyang.module.scene.domain.ScenePlayerDo;
import com.linlazy.mmorpglintingyang.module.user.manager.UserManager;
import com.linlazy.mmorpglintingyang.module.user.manager.entity.User;
import com.linlazy.mmorpglintingyang.utils.SpringContextUtil;

public  class SceneEntityDoFactory {

    public static SceneEntityDo newPlayerSceneEntityDo(long entityId) {
        UserManager userManager = SpringContextUtil.getApplicationContext().getBean(UserManager.class);
        User user = userManager.getUser(entityId);
        return new SceneEntityDo(new ScenePlayerDo(user));
    }
}
