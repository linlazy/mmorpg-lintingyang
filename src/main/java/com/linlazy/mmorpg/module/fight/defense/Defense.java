package com.linlazy.mmorpg.module.fight.defense;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.module.fight.defense.actor.BaseActorDefense;
import com.linlazy.mmorpg.constants.SceneEntityType;

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
