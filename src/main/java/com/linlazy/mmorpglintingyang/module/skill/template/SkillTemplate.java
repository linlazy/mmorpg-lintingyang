package com.linlazy.mmorpglintingyang.module.skill.template;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * 技能模板
 */
public abstract class SkillTemplate {

    public static Map<Integer,SkillTemplate> map = new HashMap<>();

    @PostConstruct
    public void init(){
        map.put(skillTemplateId(),this);
    }

    protected abstract int skillTemplateId();


    public static SkillTemplate getSkillTemplate(int skillTemplateId){
        return map.get(skillTemplateId);
    }
}
