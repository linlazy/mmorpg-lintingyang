package com.linlazy.mmorpglintingyang.module.skill.manager;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.skill.manager.config.SkillConfigService;
import com.linlazy.mmorpglintingyang.module.skill.manager.dao.SkillDao;
import com.linlazy.mmorpglintingyang.module.skill.manager.entity.Skill;
import com.linlazy.mmorpglintingyang.module.skill.manager.entity.model.SkillInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class SkillManager {

    @Autowired
    private SkillConfigService skillConfigService;
    @Autowired
    private SkillDao skillDao;

    /**
     * 获取玩家技能信息
     * @param skillId
     * @return
     */
    public SkillInfo getActorSkillInfo(long actorId,int skillId) {
        Set<SkillInfo> actorSkillInfoSet = this.getActorSkillInfoSet(actorId);
        for(SkillInfo skillInfo: actorSkillInfoSet){
            if(skillInfo.getSkillId() == skillId){
                return skillInfo;
            }
        }
        return null;
    }

    /**
     * 获取玩家技能信息集
     * @param actorId
     * @return
     */
    public Set<SkillInfo> getActorSkillInfoSet(long actorId) {
        Skill skill = skillDao.getSkill(actorId);
        if(skill == null){
            skill = new Skill();
            skill.setActorId(actorId);
            skillDao.addSkill(skill);
        }
        return skill.getSkillInfoSet();
    }

    /**
     * 获取技能攻击力
     * @param actorId
     * @param skillId
     * @return
     */
    public int getSkillAttack(long actorId,int skillId){
        JSONObject skillConfig = skillConfigService.getSkillConfig(skillId);
        return skillConfig.getIntValue("attack");
    }

    /**
     * 获取技能cd时间（毫秒）
     * @param skillId
     * @return
     */
    public long getSkillCDMills(int skillId){
        JSONObject skillConfig = skillConfigService.getSkillConfig(skillId);
        return skillConfig.getIntValue("cd") * 1000;
    }

    /**
     * 更新技能信息
     * @param skillInfo
     */
    public void updateSkillInfo(long actorId,SkillInfo skillInfo) {
        Skill skill = skillDao.getSkill(actorId);
        Set<SkillInfo> skillInfoSet = skill.getSkillInfoSet();
        skillInfoSet.remove(skillInfo);
        skillInfoSet.add(skillInfo);
        skill.setSkills(JSONObject.toJSONString(skillInfoSet));
        skillDao.updateSkill(skill);
    }

    /**
     * 升级技能
     * @param actorId
     * @param skillId
     * @return
     */
    public SkillInfo upgradeSkill(long actorId, int skillId) {

        //空处理
        Skill skill = skillDao.getSkill(actorId);
        if( skill == null){
            skill = new Skill();
            skill.setActorId(actorId);
            skillDao.addSkill(skill);
        }

        //升级
        Set<SkillInfo> skillInfoSet = skill.getSkillInfoSet();
        for(SkillInfo skillInfo: skillInfoSet){
            if(skillInfo.getSkillId() == skillId){
                skillInfo.setLevel(skillInfo.getLevel() + 1);
                return skillInfo;
            }
        }

        //没有存储则新添加技能存档
        SkillInfo skillInfo = new SkillInfo(skillId, 1);
        skillInfoSet.add(skillInfo);
        skill.setSkills(JSONObject.toJSONString(skillInfoSet));
        skillDao.updateSkill(skill);
        return skillInfo;
    }

    /**
     * 获取所有技能配置信息
     * @return
     */
    public JSONArray getSkillConfigInfo() {
        return skillConfigService.getSkillConfigInfo();
    }

    public int getConsumeMP(int skillId) {
        return skillConfigService.getSkillConfig(skillId).getIntValue("mp");
    }
}
