package com.linlazy.mmorpglintingyang.module.fight.handler;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.fight.service.FightService;
import com.linlazy.mmorpglintingyang.server.common.Result;
import com.linlazy.mmorpglintingyang.server.route.Cmd;
import com.linlazy.mmorpglintingyang.server.route.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Module("fight")
@Component
public class FightHandler {


    @Autowired
    private FightService fightService;

    /**
     * 攻击
     * @param jsonObject
     * @return
     */
    @Cmd("attack")
    public Result<?> attack(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        return fightService.attack(actorId,jsonObject);
    }

    /**
     *  受到攻击
     * @param jsonObject
     * @return
     */
    @Cmd("attacked")
    public Result<?> attacked(JSONObject jsonObject){
         int sourceId = jsonObject.getIntValue("sourceId");
         long actorId = jsonObject.getLongValue("actorId");
        return Result.success(fightService.attacked(actorId,sourceId,jsonObject));
    }

    /**
     * 穿戴技能
     * @param jsonObject
     * @return
     */
    @Cmd("dressSkill")
    public Result<?> dressSkill(JSONObject jsonObject){
        int skillId = jsonObject.getIntValue("skillId");
        long actorId = jsonObject.getLongValue("actorId");
        return fightService.dressSkill(actorId,skillId,jsonObject);
    }

    /**
     * 获得技能
     * @param jsonObject
     * @return
     */
    @Cmd("gainSkill")
    public Result<?> gainSkill(JSONObject jsonObject){
        int skillId = jsonObject.getIntValue("skillId");
        long actorId = jsonObject.getLongValue("actorId");
        return fightService.gainSkill(actorId,skillId,jsonObject);
    }
}
