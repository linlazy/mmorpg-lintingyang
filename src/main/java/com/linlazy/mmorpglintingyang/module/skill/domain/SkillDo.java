package com.linlazy.mmorpglintingyang.module.skill.domain;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.skill.config.SkillConfigService;
import com.linlazy.mmorpglintingyang.module.skill.entity.Skill;
import com.linlazy.mmorpglintingyang.utils.SpringContextUtil;
import lombok.Data;

/**
 * @author linlazy
 */
@Data
public class SkillDo {

    /**
     * 玩家ID
     */
    private final long actorId;

    /**
     * 技能ID
     */
    private final int skillId;

    /**
     * 攻击力
     */
    private final int attack;

    private SkillConfigService skillConfigService = SpringContextUtil.getApplicationContext().getBean(SkillConfigService.class);

    public SkillDo(Skill skill) {
        this.actorId = skill.getActorId();
        this.skillId = skill.getSkillId();
        JSONObject skillConfig = skillConfigService.getSkillConfig(this.skillId);
        this.attack = skillConfig.getIntValue("attackPlayerDo");
    }

    public int computeFinalAttack() {
        return this.attack ;
    }

    public Skill convertSkill() {
        return null;
    }
}
