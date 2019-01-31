package com.linlazy.mmorpg.module.item.effect.hp;

/**
 * @author linlazy
 */
public interface ResumeHpType {
    /**
     * 立即回复HP，通过固定HP
     */
    int immediateResumeByFix =1;

    /**
     * 间隔回复HP
     */
    int durationResume = 2;

    /**
     * 立即回复HP，通过固定比例
     */
    int immediateResumeByRatio =3;
}
