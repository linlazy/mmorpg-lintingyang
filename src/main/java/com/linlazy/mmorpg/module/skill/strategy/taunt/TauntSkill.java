package com.linlazy.mmorpg.module.skill.strategy.taunt;

import com.linlazy.mmorpg.module.scene.domain.SceneEntity;
import com.linlazy.mmorpg.module.skill.domain.Skill;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 嘲讽技能
 * @author linlazy
 */
public abstract class TauntSkill {

    private static Map<Integer, TauntSkill> map = new HashMap<>();



    @PostConstruct
    private void init(){
        map.put(sceneEntityType(),this);
    }

    /**
     * 场景实体类型
     * @return
     */
    protected abstract Integer sceneEntityType();

    public  static TauntSkill getTauntSkill(int sceneEntityType){
        return map.get(sceneEntityType);
    }

    public abstract Set<SceneEntity> selectTauntedSceneEntitySet(SceneEntity sceneEntity, Skill skill, Set<SceneEntity> allSceneEntity);

}
