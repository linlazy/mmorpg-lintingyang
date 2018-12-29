package com.linlazy.mmorpglintingyang.module.skill.template;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class SkillDo {

    private long actorId;
    private long skillId;
    /**
     * 技能模板ID
     */
    private long skillTemplateId;
    /**
     * 技能模板参数
     */
    private JSONObject skillTemplateArgs;

    /**
     * CD时间
     */
    private int CDTimes;

    /**
     * 是否群攻
     */
    private boolean groupAttack;

    /**
     * 消耗MP
     */
    private int consumeMP;
}
