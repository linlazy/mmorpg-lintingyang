package com.linlazy.mmorpg.module.copy.fighting.event;

import com.linlazy.mmorpg.module.copy.fighting.domain.FightingCopy;
import lombok.Data;

/**
 * @author linlazy
 */
@Data
public class CopyFightingBossDeadEvent {
    private FightingCopy fightingCopy;

    public CopyFightingBossDeadEvent(FightingCopy fightingCopy) {
        this.fightingCopy = fightingCopy;
    }
}
