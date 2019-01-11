package com.linlazy.mmorpg.module.skill.domain;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.module.skill.config.SkillConfigService;
import com.linlazy.mmorpg.module.skill.entity.SkillEntity;
import com.linlazy.mmorpg.utils.SpringContextUtil;
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

    public SkillDo(SkillEntity skillEntity) {
        this.actorId = skillEntity.getActorId();
        this.skillId = skillEntity.getSkillId();
        JSONObject skillConfig = skillConfigService.getSkillConfig(this.skillId);
        this.attack = skillConfig.getIntValue("attackPlayerDo");
    }

    public int computeFinalAttack() {
        return this.attack ;
    }

    public SkillEntity convertSkill() {
        return null;
    }
}
