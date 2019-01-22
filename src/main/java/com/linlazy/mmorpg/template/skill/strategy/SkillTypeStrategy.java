package com.linlazy.mmorpg.template.skill.strategy;

import com.linlazy.mmorpg.domain.SceneEntity;
import com.linlazy.mmorpg.domain.Skill;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * 技能类型策略
 * @author linlazy
 */
public abstract class SkillTypeStrategy {

    private static Map<Integer,SkillTypeStrategy> map = new HashMap<>();


    @PostConstruct
    public void init(){
        map.put(skillType(),this);
    }

    protected abstract Integer skillType();

    public static SkillTypeStrategy getSkillTypeStrategy(int skillType){
        return map.get(skillType);
    }

    public abstract void useSkill(SceneEntity sceneEntity, Skill skill);

}
