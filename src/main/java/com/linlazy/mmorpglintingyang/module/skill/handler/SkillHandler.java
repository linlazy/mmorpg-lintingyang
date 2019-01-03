package com.linlazy.mmorpglintingyang.module.skill.handler;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.skill.service.SkillService;
import com.linlazy.mmorpglintingyang.server.common.Result;
import com.linlazy.mmorpglintingyang.server.route.Cmd;
import com.linlazy.mmorpglintingyang.server.route.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author linlazy
 */
@Module("skill")
@Component
public class SkillHandler {

    @Autowired
    private SkillService skillService;

    /**
     * 穿戴技能
     * @param jsonObject
     * @return
     */
    @Cmd("dress")
    public Result<?> dress(JSONObject jsonObject){
        int skillId = jsonObject.getIntValue("skillId");
        long actorId = jsonObject.getLongValue("actorId");
        return skillService.dressSkill(actorId,skillId,jsonObject);
    }

    /**
     * 技能升级
     * @param jsonObject
     * @return
     */
    @Cmd("levelUp")
    public Result<?> levelUp(JSONObject jsonObject){
        int skillId = jsonObject.getIntValue("skillId");
        long actorId = jsonObject.getLongValue("actorId");
        return skillService.levelUp(actorId,skillId,jsonObject);
    }
}
