package com.linlazy.mmorpg.module.copy.fighting.trigger;

import com.linlazy.mmorpg.module.copy.fighting.domain.FightingCopy;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author linlazy
 */
public abstract class CopyFightTrigger {

    private static Map<Integer,CopyFightTrigger> map = new HashMap<>();

    @PostConstruct
    public void init(){
        map.put(copyLevel(),this);
    }

    protected abstract Integer copyLevel();

    public static CopyFightTrigger getCopyFightTrigger(int copyLevel){
        return map.get(copyLevel);
    }

    public boolean isTriggerNext(FightingCopy fightingCopy){
        return false;
    }

    public void doTriggerNext(FightingCopy fightingCopy) {
        fightingCopy.setCopyLevel(fightingCopy.getCopyLevel() + 1);
    }
}
