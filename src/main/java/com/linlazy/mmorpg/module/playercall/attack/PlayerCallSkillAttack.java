package com.linlazy.mmorpg.module.playercall.attack;

import com.linlazy.mmorpg.module.playercall.domain.PlayerCall;
import com.linlazy.mmorpg.module.skill.domain.Skill;
import com.linlazy.mmorpg.file.service.SkillConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 玩家召唤兽技能攻击类
 * @author linlazy
 */
@Component
public class PlayerCallSkillAttack extends PlayerCallAttack{

    @Autowired
    private SkillConfigService skillConfigService;

    @Override
    protected int attackType() {
        return 1;
    }

    @Override
    protected int computeAttack(PlayerCall playerCall, Skill skill) {
        return skill.getSkillTemplateArgs().getIntValue("useSkill");
    }


}
