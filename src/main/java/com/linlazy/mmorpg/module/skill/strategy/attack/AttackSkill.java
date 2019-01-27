package com.linlazy.mmorpg.module.skill.strategy.attack;

import com.linlazy.mmorpg.module.scene.domain.SceneEntity;
import com.linlazy.mmorpg.module.skill.domain.Skill;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author linlazy
 */
public abstract class AttackSkill {

    private static Map<Integer,AttackSkill> map = new HashMap<>();



    @PostConstruct
    private void init(){
        map.put(sceneEntityType(),this);
    }

    /**
     * 场景实体类型
     * @return
     */
    protected abstract Integer sceneEntityType();

    public  static AttackSkill getAttackSkill(int sceneEntityType){
        return map.get(sceneEntityType);
    }

    public abstract Set<SceneEntity> selectAttackedSceneEntitySet(SceneEntity sceneEntity, Skill skill, Set<SceneEntity> allSceneEntity);

}
