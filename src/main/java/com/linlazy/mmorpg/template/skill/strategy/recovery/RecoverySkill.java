package com.linlazy.mmorpg.template.skill.strategy.recovery;

import com.linlazy.mmorpg.domain.SceneEntity;
import com.linlazy.mmorpg.domain.Skill;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author linlazy
 */
public abstract class RecoverySkill {

    private static Map<Integer, RecoverySkill> map = new HashMap<>();



    @PostConstruct
    private void init(){
        map.put(sceneEntityType(),this);
    }

    /**
     * 场景实体类型
     * @return
     */
    protected abstract Integer sceneEntityType();

    public  static RecoverySkill getRecoverySkill(int sceneEntityType){
        return map.get(sceneEntityType);
    }

    public abstract Set<SceneEntity> selectRecoverySceneEntitySet(SceneEntity sceneEntity, Skill skill, Set<SceneEntity> allSceneEntity);

}
