package com.linlazy.mmorpg.module.skill.template;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.module.scene.domain.SceneEntity;
import com.linlazy.mmorpg.module.skill.domain.Skill;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * 技能模板
 * @author linlazy
 */
public abstract class BaseSkillTemplate {

    private static Map<Integer, BaseSkillTemplate> map = new HashMap<>();

    @PostConstruct
    public void init(){
        map.put(skillTemplateId(),this);
    }

    /**
     * 技能模板ID 由子类决定
     * @return
     */
    protected abstract int skillTemplateId();


    public static BaseSkillTemplate getSkillTemplate(int skillTemplateId){
        return map.get(skillTemplateId);
    }

    /**
     * 使用技能
     * @param sceneEntity
     * @param skill
     */
    public abstract void useSkill(SceneEntity sceneEntity, Skill skill, JSONObject jsonObject);


}
