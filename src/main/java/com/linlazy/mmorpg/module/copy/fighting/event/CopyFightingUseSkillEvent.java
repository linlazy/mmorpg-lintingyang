package com.linlazy.mmorpg.module.copy.fighting.event;

import com.linlazy.mmorpg.module.copy.fighting.domain.FightingCopy;
import lombok.Data;

/**
 *
 * @author linlazy
 */
@Data
public class CopyFightingUseSkillEvent {
    private FightingCopy fightingCopy;

    public CopyFightingUseSkillEvent(FightingCopy fightingCopy) {
        this.fightingCopy = fightingCopy;
    }
}
