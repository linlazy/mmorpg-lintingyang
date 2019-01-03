package com.linlazy.mmorpglintingyang.module.fight.service.attackmode;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.fight.attack.actor.BaseActorAttack;
import com.linlazy.mmorpglintingyang.module.fight.service.canattacked.BaseCanAttacked;
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

/**
 * @author linlazy
 */
public abstract class BaseAttackMode {

    private static Map<Integer, BaseAttackMode> map = new HashMap<>();

    @PostConstruct
    public void init(){
        map.put(attackMode(),this);
    }

    /**
     * 返回玩家攻击的方式
     * @return 返回玩家攻击的方式（普通攻击，技能攻击）
     */
    protected abstract int attackMode();

    public static BaseAttackMode getAttackMode(int attackMode){
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
        int finalAttack = BaseActorAttack.computeFinalAttack(actorId, jsonObject);
        // 受攻击场景实体集合集合
        Set<SceneEntityDo> attackedSceneEntity = getAttackedSceneEntity(actorId, jsonObject);
        attackedSceneEntity.stream()
                .forEach(sceneEntityDo -> sceneEntityDo.attacked(finalAttack,jsonObject));
        //存档
        attackAfter(actorId,jsonObject);
        return Result.success();
    }



    protected  Set<SceneEntityDo> getAttackedSceneEntity(long actorId, JSONObject jsonObject){
        Set sceneEntityDoSet = new HashSet();

        int entityType = jsonObject.getIntValue("entityType");
        long entityId = jsonObject.getLongValue("entityId");

        SceneEntityDo sceneEntityDo = null;
        if(entityType == SceneEntityType.PLAYER){
             sceneEntityDo =SceneEntityDoFactory.newPlayerSceneEntityDo(entityId);
        }else {
            SceneDo sceneDo = sceneManager.getSceneDo(actorId);
            sceneEntityDo = sceneDo.getSceneEntityDoSet().stream()
                    .filter(sceneEntityDo1 -> sceneEntityDo1.getSceneEntityId() == entityId && sceneEntityDo1.getSceneEntityType() == entityType)
                    .findFirst().get();
        }

        if(sceneEntityDo.getHp() >0){
            sceneEntityDoSet.add(sceneEntityDo);
        }

        return sceneEntityDoSet;
    }

    public Result<?> isCanAttack(long actorId, JSONObject jsonObject){
        int entityType = jsonObject.getIntValue("entityType");
        return BaseCanAttacked.getCanAttacked(entityType).canAttacked(actorId,jsonObject);
    }
    public  void attackAfter(long actorId, JSONObject jsonObject){

    }
}
