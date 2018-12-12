package com.linlazy.mmorpglintingyang.module.skill.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.common.Result;
import com.linlazy.mmorpglintingyang.module.scene.config.MonsterConfigService;
import com.linlazy.mmorpglintingyang.module.scene.dao.SceneEntityDao;
import com.linlazy.mmorpglintingyang.module.scene.entity.Scene;
import com.linlazy.mmorpglintingyang.module.scene.entity.model.SceneEntityInfo;
import com.linlazy.mmorpglintingyang.module.scene.push.ScenePushHelper;
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
    @Autowired
    private SceneEntityDao sceneEntityDao;


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
        if(skill == null){
            skill = new Skill();
            skill.setActorId(actorId);
        }
        Set<SkillInfo> skillInfoSet = skill.getSkillInfoSet();
        if( !skillInfoSet.contains(new SkillInfo(skillId))){
            return Result.valueOf("玩家不具备该技能");
        }

        SceneEntityInfo monsterInfo = sceneService.getMonsterInfo(actorId,monsterId);
        if(monsterInfo == null){
            return Result.valueOf("当前场景无此怪物");
        }

        //获取技能攻击力
        int attack = skillConfig.getIntValue("attack");
        //怪物被攻击
        monsterInfo.attacked(attack);
        //存档
        Scene scene = sceneService.getScene(actorId);
        sceneService.updateSceneEntityInfo(scene.getSceneId(),monsterInfo);
        Set<Long> actorIds = sceneService.getCurrentSceneOnlineActorIds(scene.getSceneId());

        //通知其它玩家
        for(Long pushActorId: actorIds){
            if(pushActorId != actorId){
                ScenePushHelper.pushMonster(pushActorId,monsterInfo);
            }
        }
        return Result.success(monsterInfo);
    }



    public Result<?> getSkillInfo(long actorId) {
        Skill skill = skillDao.getSkill(actorId);
        if(skill == null){
            skill = new Skill();
        }
        return Result.success(skill.getSkillInfoSet());
    }

    /**
     * 获得技能
     * @param actorId
     * @param skillId
     * @return
     */
    public Result<?> gainSkill(long actorId, int skillId) {

        Skill skill = skillDao.getSkill(actorId);
        if( skill == null){
            skill = new Skill();
            skill.setActorId(actorId);
            skillDao.addSkill(skill);
        }

        Set<SkillInfo> skillInfoSet = skill.getSkillInfoSet();
        skillInfoSet.add(new SkillInfo(skillId,1));
        System.out.println(skillInfoSet);
        skill.setSkills(JSONObject.toJSONString(skillInfoSet));
        skillDao.updateSkill(skill);
        return Result.success(skillInfoSet);
    }
}
