package com.linlazy.mmorpg.module.skill.template;

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
}
