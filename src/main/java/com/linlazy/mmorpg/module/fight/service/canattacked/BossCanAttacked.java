package com.linlazy.mmorpg.module.fight.service.canattacked;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.module.scene.constants.SceneEntityType;
import com.linlazy.mmorpg.module.scene.validator.SceneValidator;
import com.linlazy.mmorpg.server.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author linlazy
 */
@Component
public class BossCanAttacked extends BaseCanAttacked {

    @Autowired
    private SceneValidator sceneValidator;

    @Override
    protected int entityType() {
        return SceneEntityType.BOSS;
    }

    @Override
    public Result<?> canAttacked(long actorId, JSONObject jsonObject) {
        long entityId = jsonObject.getLongValue("entityId");
        int entityType = jsonObject.getIntValue("entityType");
        if(sceneValidator.isDifferentScene(actorId,entityType,entityId)){
            return Result.valueOf("处于不同场景");
        }
        return Result.success();
    }
}
