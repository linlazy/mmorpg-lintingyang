package com.linlazy.mmorpg.template.skill;

import com.google.common.collect.Sets;
import com.linlazy.mmorpg.constants.SkillType;
import com.linlazy.mmorpg.domain.Skill;
import com.linlazy.mmorpg.module.scene.domain.SceneEntity;
import com.linlazy.mmorpg.service.SceneService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 技能模板
 * @author linlazy
 */
public abstract class BaseSkillTemplate {

    private static Map<Integer, BaseSkillTemplate> map = new HashMap<>();

    @Autowired
    private SceneService sceneService;

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
    public void useSkill(SceneEntity sceneEntity,Skill skill){

        if(skill.getType() == SkillType.CALL){
            //召唤技能
            useCallSkill(sceneEntity,skill);
        }else {
            // 获取 预选受攻击,恢复对象
            Set<SceneEntity> allSceneEntity = sceneService.getAllSceneEntity(sceneEntity);
            Set<SceneEntity> sceneEntitySet = selectAttackedSceneEntity(sceneEntity,skill, allSceneEntity);
//            useSkill(sceneEntity,skill,sceneEntitySet);
        }

    }

    /**
     * 使用召唤技能
     * @param sceneEntity
     * @param skill
     */
    protected  void useCallSkill(SceneEntity sceneEntity, Skill skill){

    }

    protected  Set<SceneEntity> selectAttackedSceneEntity(SceneEntity sceneEntity, Skill skill, Set<SceneEntity> allSceneEntity){

        return Sets.newHashSet();
    }


}
