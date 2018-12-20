package com.linlazy.mmorpglintingyang.module.fight.skill.domain;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.fight.skill.config.SkillConfigService;
import com.linlazy.mmorpglintingyang.module.fight.skill.entity.Skill;
import lombok.Data;

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

    private SkillConfigService skillConfigService;

    public SkillDo(Skill skill) {
        this.actorId = skill.getActorId();
        this.skillId = skill.getSkillId();
        JSONObject skillConfig = skillConfigService.getSkillConfig(this.skillId);
        this.attack = skillConfig.getIntValue("attack");
    }

    public int computeFinalAttack() {
        return this.attack ;
    }

    public Skill convertSkill() {
        return null;
    }
}
