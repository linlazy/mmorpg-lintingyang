package com.linlazy.mmorpglintingyang.module.fight.defense;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.fight.defense.actor.BaseActorDefense;
import com.linlazy.mmorpglintingyang.module.scene.constants.SceneEntityType;

/**
 * @author linlazy
 */
public  class Defense {

    public static int computeDefense(int entityType, long entityId, JSONObject jsonObject) {
        switch (entityType){
            case SceneEntityType
                    .PLAYER:
                return BaseActorDefense.computeFinalDefense(entityId,jsonObject);
            case SceneEntityType
                    .MONSTER:
                return 0;
            default:
                return  0;
        }
    }
}
