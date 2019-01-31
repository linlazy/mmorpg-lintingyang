package com.linlazy.mmorpg.module.item.effect.mp;

/**
 * @author linlazy
 */
public interface ResumeMpType {
    /**
     * 立即回复HP，通过固定MP
     */
    int immediateResumeByFix =1;

    /**
     * 间隔回复MP
     */
    int durationResume = 2;

}
