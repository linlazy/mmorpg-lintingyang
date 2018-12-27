package com.linlazy.mmorpglintingyang.module.fight.service.canattacked;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.scene.constants.SceneEntityType;
import com.linlazy.mmorpglintingyang.server.common.Result;
import org.springframework.stereotype.Component;

@Component
public class MonsterCanAttacked extends CanAttacked {
    @Override
    protected int entityType() {
        return SceneEntityType.Monster;
    }

    @Override
    public Result<?> canAttacked(long actorId, JSONObject jsonObject) {
        return null;
    }
}
