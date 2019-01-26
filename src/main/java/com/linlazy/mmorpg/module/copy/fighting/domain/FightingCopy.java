package com.linlazy.mmorpg.module.copy.fighting.domain;

import com.linlazy.mmorpg.module.common.reward.Reward;
import com.linlazy.mmorpg.module.scene.domain.Scene;

/**
 * 斗罗大陆副本
 * @author linlazy
 */
public class FightingCopy extends Scene {

    /**
     * 副本层级
     */
    private int copyLevel;

    /**
     * 通关奖励
     */
    private Reward reward;


    public boolean isTriggerNext() {
        return false;
    }

    public void triggerNext() {

    }
}
