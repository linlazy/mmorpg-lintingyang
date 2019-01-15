package com.linlazy.mmorpg.service;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.dao.SkillDAO;
import com.linlazy.mmorpg.domain.SceneEntity;
import com.linlazy.mmorpg.domain.Skill;
import com.linlazy.mmorpg.entity.SkillEntity;
import com.linlazy.mmorpg.server.common.Result;
import com.linlazy.mmorpg.template.skill.BaseSkillTemplate;
import com.linlazy.mmorpg.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 技能服务类
 * @author linlazy
 */
@Component
public class SkillService {

    @Autowired
    private SkillDAO skillDao;

    public void  attack(SceneEntity sceneEntity, Skill skill){
        BaseSkillTemplate skillTemplate = BaseSkillTemplate.getSkillTemplate(skill.getSkillTemplateId());
        skillTemplate.useSkill(sceneEntity,skill);
    }


    public Result<?> dressSkill(long actorId, int skillId, JSONObject jsonObject) {

        //未拥有此技能或此技能已穿戴
        if(notHas(actorId,skillId) || isDressed(actorId,skillId)){
            return Result.valueOf("参数错误");
        }
        SkillEntity skillEntity = skillDao.getEntityByPK(actorId, skillId);
        skillEntity.setDressed(true);
        skillDao.updateQueue(skillEntity);
        return Result.success();
    }

    public Result<?> levelUp(long actorId, int skillId, JSONObject jsonObject) {

        SkillEntity skillEntity = skillDao.getEntityByPK(actorId, skillId);
        if(skillEntity != null){
            return Result.valueOf("已拥有技能");
        }

        skillEntity = new SkillEntity();
        skillEntity.setActorId(actorId);
        skillEntity.setSkillId(skillId);
        skillEntity.setDressed(false);
        skillEntity.setLevel(1);
        skillEntity.setNextCDResumeTimes(DateUtils.getNowMillis());

        skillDao.insertQueue(skillEntity);
        return Result.success(skillEntity);
    }

    public boolean notHas(long actorId,int skillId){
        return skillDao.getEntityByPK(actorId, skillId) == null;
    }
    public boolean isDressed(long actorId,int skillId){
        SkillEntity skillEntity = skillDao.getEntityByPK(actorId, skillId);
        if(skillEntity != null && skillEntity.isDressed()){
            return true;
        }
        return false;
    }
}
