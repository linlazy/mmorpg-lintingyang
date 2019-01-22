package com.linlazy.mmorpg.template.skill;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.domain.SceneEntity;
import com.linlazy.mmorpg.domain.Skill;
import com.linlazy.mmorpg.service.SceneService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

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

//    /**
//     * 使用技能
//     * @param sceneEntity
//     * @param skill
//     */
//    public void useSkill(SceneEntity sceneEntity,Skill skill){
//
//        if(skill.getType() == SkillType.CALL){
//            //召唤技能
//            SkillTypeStrategy skillTypeStrategy = SkillTypeStrategy.getSkillTypeStrategy(skill.getType());
//            skillTypeStrategy.useSkill(sceneEntity,skill);
//            //嘲讽技能
//        }else if(skill.getType() == SkillType.TAUNT){
//            Set<SceneEntity> allSceneEntity = sceneService.getAllSceneEntity(sceneEntity);
//            Set<SceneEntity> sceneEntitySet = selectAttackedSceneEntity(sceneEntity,skill, allSceneEntity);
//            sceneEntitySet.stream().forEach(sceneEntity1 -> {
//                sceneEntity1.setTauntStatus(true);
//            });
//        } else {
//            // 获取 预选受攻击,恢复对象
//            Set<SceneEntity> allSceneEntity = sceneService.getAllSceneEntity(sceneEntity);
//            Set<SceneEntity> sceneEntitySet = selectAttackedSceneEntity(sceneEntity,skill, allSceneEntity);
//            for(SceneEntity targetSceneEntity: sceneEntitySet){
//                targetSceneEntity.attacked(sceneEntity,skill);
//            }
//        }
//
//    }

    /**
     * 使用技能
     * @param sceneEntity
     * @param skill
     */
    public abstract void useSkill(SceneEntity sceneEntity, Skill skill, JSONObject jsonObject);


}
