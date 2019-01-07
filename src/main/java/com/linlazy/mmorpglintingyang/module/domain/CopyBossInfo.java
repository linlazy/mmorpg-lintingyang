package com.linlazy.mmorpglintingyang.module.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * BOSS
 * @author linlazy
 */
public class CopyBossInfo {

    /**
     * 副本ID
     */
    private long copyId;

    /**
     * BOSS
     */
    private Set<Boss> bosseSet = new HashSet<>();
}
