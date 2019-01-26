package com.linlazy.mmorpg.module.copy.fighting.event;

import com.linlazy.mmorpg.module.copy.fighting.domain.FightingCopy;
import lombok.Data;

/**
 *
 * @author linlazy
 */
@Data
public class CopyFightingSuccessEvent {

    private FightingCopy fightingCopy;

    public CopyFightingSuccessEvent(FightingCopy fightingCopy) {
        this.fightingCopy = fightingCopy;
    }
}
