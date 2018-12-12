package com.linlazy.mmorpglintingyang.module.skill.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.common.Result;
import com.linlazy.mmorpglintingyang.module.scene.config.MonsterConfigService;
import com.linlazy.mmorpglintingyang.module.scene.constants.SceneEntityType;
import com.linlazy.mmorpglintingyang.module.scene.entity.model.SceneEntityInfo;
import com.linlazy.mmorpglintingyang.module.scene.service.SceneService;
import com.linlazy.mmorpglintingyang.module.skill.config.SkillConfigService;
import com.linlazy.mmorpglintingyang.module.skill.dao.SkillDao;
import com.linlazy.mmorpglintingyang.module.skill.entity.Skill;
import com.linlazy.mmorpglintingyang.module.skill.entity.model.SkillInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class SkillService {

    @Autowired
    private SkillConfigService skillConfigService;

    @Autowired
    private MonsterConfigService monsterConfigService;

    @Autowired
    private SkillDao skillDao;
    @Autowired
    private SceneService sceneService;


    /**
     * 获取技能配置信息
     * @return
     */
    public Result<?> getAllSkillInfo() {
        JSONArray skillConfigInfo = skillConfigService.getSkillConfigInfo();
        return Result.success(skillConfigInfo);
    }

    /**
     * 使用技能攻击怪物
     * @param actorId
     * @param skillId
     * @param monsterId
     * @return
     */
    public Result<?> attack(long actorId, int skillId, int monsterId) {

        //参数校验
        JSONObject skillConfig = skillConfigService.getSkillConfig(skillId);
        if(skillConfig == null){
            return Result.valueOf("参数错误");
        }

        //玩家是否具备该技能
        Skill skill = skillDao.getSkill(actorId);
        Set<SkillInfo> skillInfoSet = skill.getSkillInfoSet();
        if( !skillInfoSet.contains(new SkillInfo(skillId))){
            return Result.valueOf("玩家不具备该技能");
        }

        SceneEntityInfo monsterInfo = getMonsterInfo(actorId,monsterId);
        if(monsterInfo == null){
            return Result.valueOf("当前场景无此怪物");
        }

        //获取技能攻击力
        int attack = skillConfig.getIntValue("attack");
        //怪物被攻击
        monsterInfo.attacked(attack);



        return Result.success(monsterInfo);
    }

    /**
     * 获取怪物当前信息
     * @param actorId
     * @param monsterId
     * @return
     */
    private SceneEntityInfo getMonsterInfo(long actorId,int monsterId) {

        Set<SceneEntityInfo> currentInfo = sceneService.getCurrentInfo(actorId);
        for(SceneEntityInfo sceneEntityInfo: currentInfo){
            if(sceneEntityInfo.getEntityId() == monsterId
                    && sceneEntityInfo.getEntityType() == SceneEntityType.Monster){
                return sceneEntityInfo;
            }
        }
        return null;
    }

    public Result<?> getSkillInfo(long actorId) {
        Skill skill = skillDao.getSkill(actorId);
        if(skill == null){
            skill = new Skill();
        }
        return Result.success(skill.getSkillInfoSet());
    }

    public Result<?> gainSkill(long actorId, int skillId) {

        Skill skill = skillDao.getSkill(actorId);
        if( skill == null){
            skill = new Skill();
            skill.setActorId(actorId);
        }
        skill.getSkillInfoSet().add(new SkillInfo(skillId));
        skill.setSkills(JSONObject.toJSONString(skill.getSkillInfoSet()));
        skillDao.updateSkill(skill);
        return Result.success();
    }
}
