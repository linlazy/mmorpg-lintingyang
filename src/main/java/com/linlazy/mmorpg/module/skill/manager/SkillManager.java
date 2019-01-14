package com.linlazy.mmorpg.module.skill.manager;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.dao.SkillDAO;
import com.linlazy.mmorpg.entity.SkillEntity;
import com.linlazy.mmorpg.server.common.Result;
import com.linlazy.mmorpg.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author linlazy
 */
@Component
public class SkillManager {

    @Autowired
    private SkillDAO skillDao;

    /**
     * 穿戴技能
     * @param actorId
     * @param skillId
     * @return
     */
    public Result<?> dressSkill(long actorId, int skillId) {
        SkillEntity skillEntity = skillDao.getEntityByPK(actorId, skillId);
        skillEntity.setDressed(true);
//        skillDao.updateSkill(skillEntity);
        return Result.success();
    }

    /**
     * 升级技能
     * @param actorId
     * @param skillId
     * @param jsonObject
     * @return
     */
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
