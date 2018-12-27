package com.linlazy.mmorpglintingyang.module.fight.service.attackmode;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.fight.attack.actor.ActorAttack;
import com.linlazy.mmorpglintingyang.module.fight.service.canattacked.CanAttacked;
import com.linlazy.mmorpglintingyang.module.fight.service.sceneentity.SceneEntityDoFactory;
import com.linlazy.mmorpglintingyang.module.scene.constants.SceneEntityType;
import com.linlazy.mmorpglintingyang.module.scene.domain.SceneDo;
import com.linlazy.mmorpglintingyang.module.scene.domain.SceneEntityDo;
import com.linlazy.mmorpglintingyang.module.scene.manager.SceneManager;
import com.linlazy.mmorpglintingyang.server.common.Result;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class AttackMode {

    private static Map<Integer,AttackMode> map = new HashMap<>();

    @PostConstruct
    public void init(){
        map.put(attackMode(),this);
    }

    protected abstract int attackMode();

    public static AttackMode getAttackMode(int attackMode){
        return map.get(attackMode);
    }

    @Autowired
    private SceneManager sceneManager;

    public  Result<?> attack(long actorId, JSONObject jsonObject){
        Result<?> result = isCanAttack(actorId,jsonObject);
        if(result.isFail()){
            return Result.valueOf(result.getCode());
        }
        //计算玩家攻击力
        int finalAttack = ActorAttack.computeFinalAttack(actorId, jsonObject);
        // 受攻击场景实体集合集合
        Set<SceneEntityDo> attackedSceneEntity = getAttackedSceneEntity(actorId, jsonObject);
        attackedSceneEntity.stream()
                .forEach(sceneEntityDo -> sceneEntityDo.attacked(finalAttack,jsonObject));
        //存档
        attackAfter(actorId,jsonObject);
        return Result.success();
    }



    protected  Set<SceneEntityDo> getAttackedSceneEntity(long actorId, JSONObject jsonObject){
        Set SceneEntityDoSet = new HashSet();

        int entityType = jsonObject.getIntValue("entityType");
        long entityId = jsonObject.getLongValue("entityId");

        SceneEntityDo sceneEntityDo = null;
        if(entityType == SceneEntityType.Player){
             sceneEntityDo =SceneEntityDoFactory.newPlayerSceneEntityDo(entityId);
        }else {
            SceneDo sceneDo = sceneManager.getSceneDo(actorId);
            sceneEntityDo = sceneDo.getSceneEntityDoSet().stream()
                    .filter(sceneEntityDo1 -> sceneEntityDo1.getSceneEntityId() == entityId && sceneEntityDo1.getSceneEntityType() == entityType)
                    .findFirst().get();
        }
        SceneEntityDoSet.add(sceneEntityDo);

        return SceneEntityDoSet;
    }

    public Result<?> isCanAttack(long actorId, JSONObject jsonObject){
        int entityType = jsonObject.getIntValue("entityType");
        return CanAttacked.getCanAttacked(entityType).canAttacked(actorId,jsonObject);
    }
    public  void attackAfter(long actorId, JSONObject jsonObject){

    }
}
