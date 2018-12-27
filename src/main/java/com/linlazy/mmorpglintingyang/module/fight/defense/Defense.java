package com.linlazy.mmorpglintingyang.module.fight.defense;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.fight.defense.actor.ActorDefense;
import com.linlazy.mmorpglintingyang.module.scene.constants.SceneEntityType;

public abstract class Defense {

    public static int computeDefense(int entityType, long entityId, JSONObject jsonObject) {
        switch (entityType){
            case SceneEntityType
                    .Player:
                return ActorDefense.computeFinalDefense(entityId,jsonObject);
            case SceneEntityType
                    .Monster:
                return ActorDefense.computeFinalDefense(entityId,jsonObject);
        }
        return 0;
    }
}
