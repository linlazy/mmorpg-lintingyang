package com.linlazy.mmorpg.service;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.dao.SkillDAO;
import com.linlazy.mmorpg.domain.Skill;
import com.linlazy.mmorpg.entity.SkillEntity;
import com.linlazy.mmorpg.domain.SceneEntity;
import com.linlazy.mmorpg.server.common.Result;
import com.linlazy.mmorpg.template.skill.BaseSkillTemplate;
import com.linlazy.mmorpg.utils.DateUtils;
import com.linlazy.mmorpg.validator.SkillValidator;
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

    @Autowired
    private SkillValidator skillValidator;

    public Result<?> dressSkill(long actorId, int skillId, JSONObject jsonObject) {

        //未拥有此技能或此技能已穿戴
        if(skillValidator.notHas(actorId,skillId) || skillValidator.isDressed(actorId,skillId)){
            return Result.valueOf("参数错误");
        }
        SkillEntity skillEntity = skillDao.getEntityByPK(actorId, skillId);
        skillEntity.setDressed(true);
//        skillDao.updateSkill(skillEntity);
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

//        skillDao.addSkill(skillEntity);
        return Result.success(skillEntity);
    }
}
