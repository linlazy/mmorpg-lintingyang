package com.linlazy.mmorpglintingyang.module.fight.service.canattacked;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.fight.validator.FightValidtor;
import com.linlazy.mmorpglintingyang.module.scene.constants.SceneEntityType;
import com.linlazy.mmorpglintingyang.server.common.Result;
import com.linlazy.mmorpglintingyang.utils.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ActorCanAttacked extends CanAttacked {
    @Override
    protected int entityType() {
        return SceneEntityType.Player;
    }

    @Autowired
    private FightValidtor fightValidtor;

    @Override
    public Result<?> canAttacked(long actorId, JSONObject jsonObject) {
        long entityId = jsonObject.getLongValue("entityId");
        if(fightValidtor.isDifferentScene(actorId,entityId)){
                return Result.valueOf("处于不同场景");
        }
        if(!SessionManager.isOnline(entityId)){
            return Result.valueOf("被攻击玩家不在线");
        }
        return Result.success();
    }
}
