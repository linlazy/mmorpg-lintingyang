package com.linlazy.mmorpglintingyang.module.fight.service.sceneentity;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.scene.constants.SceneEntityType;
import com.linlazy.mmorpglintingyang.module.scene.domain.SceneEntityDo;
import com.linlazy.mmorpglintingyang.module.scene.domain.ScenePlayerDo;
import com.linlazy.mmorpglintingyang.module.user.manager.UserManager;
import com.linlazy.mmorpglintingyang.module.user.manager.entity.User;
import com.linlazy.mmorpglintingyang.utils.SpringContextUtil;

public abstract class SceneEntityDoFactory {


    public static SceneEntityDo newSceneEntityDo(int entityType, JSONObject jsonObject){
        switch (entityType){
            case  SceneEntityType.Player:
            return newPlayerSceneEntityDo(jsonObject);
        }
        return null;
    }

    private static SceneEntityDo newPlayerSceneEntityDo(JSONObject jsonObject) {
        int entityId = jsonObject.getIntValue("entityId");
        UserManager userManager = SpringContextUtil.getApplicationContext().getBean(UserManager.class);
        User user = userManager.getUser(entityId);
        return new SceneEntityDo(new ScenePlayerDo(user));
    }
}
