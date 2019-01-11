package com.linlazy.mmorpg.module.skill.template;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @author linlazy
 */
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
    private int cdTimes;

    /**
     * 是否群攻
     */
    private boolean groupAttack;

    /**
     * 消耗MP
     */
    private int consumeMP;
}