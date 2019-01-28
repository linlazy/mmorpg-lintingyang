package com.linlazy.mmorpg.module.copy.fighting.trigger;

import com.linlazy.mmorpg.module.copy.fighting.domain.FightingCopy;

/**
 * 使用技能
 * @author linlazy
 */
public  class CopyFightTriggerLevelOne extends CopyFightTrigger{

    @Override
    protected Integer copyLevel() {
        return 1;
    }

    @Override
    public boolean isTriggerNext(FightingCopy fightingCopy) {
        return fightingCopy.getPlayerFightingCopyProcess().isUseSkill();
    }


}

