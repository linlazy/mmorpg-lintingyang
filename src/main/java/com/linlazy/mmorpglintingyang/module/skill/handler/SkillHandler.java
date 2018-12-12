package com.linlazy.mmorpglintingyang.module.skill.handler;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.common.Result;
import com.linlazy.mmorpglintingyang.module.skill.service.SkillService;
import com.linlazy.mmorpglintingyang.server.route.Cmd;
import com.linlazy.mmorpglintingyang.server.route.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Module("skill")
public class SkillHandler {


    @Autowired
    private SkillService skillService;

    /**
     * 获取所有技能配置信息
     * @return
     */
    @Cmd("getAllSkillInfo")
    public Result<?> getAllSkillInfo(JSONObject jsonObject){
        return skillService.getAllSkillInfo();
    }

    /**
     * 获取玩家技能配置
     * @return
     */
    @Cmd("getSkillInfo")
    public Result<?> getSkillInfo(JSONObject jsonObject){
        long actorId = jsonObject.getLong("actorId");
        return skillService.getSkillInfo(actorId);
    }

    /**
     * 获得技能
     * @return
     */
    @Cmd("gainSkill")
    public Result<?> gainSkill(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        int skillId = jsonObject.getIntValue("skillId");
        return skillService.gainSkill(actorId,skillId);
    }



    /**
     * 使用技能攻击怪物
     * @param jsonObject
     * @return
     */
    @Cmd("attack")
    public Result<?> attack(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        int skillId = jsonObject.getIntValue("skillId");
        int monsterId = jsonObject.getIntValue("monsterId");
        return skillService.attack(actorId,skillId,monsterId);
    }
}
