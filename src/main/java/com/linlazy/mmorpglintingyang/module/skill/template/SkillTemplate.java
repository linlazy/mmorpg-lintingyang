package com.linlazy.mmorpglintingyang.module.skill.template;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * 技能模板
 * @author linlazy
 */
public abstract class SkillTemplate {

    private static Map<Integer,SkillTemplate> map = new HashMap<>();

    @PostConstruct
    public void init(){
        map.put(skillTemplateId(),this);
    }

    /**
     * 技能模板ID 由子类决定
     * @return
     */
    protected abstract int skillTemplateId();


    public static SkillTemplate getSkillTemplate(int skillTemplateId){
        return map.get(skillTemplateId);
    }
}
