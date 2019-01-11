package com.linlazy.mmorpg.domain;

import com.linlazy.mmorpg.module.backpack.BackpackInterface;
import lombok.Data;

/**
 * 背包结构复用
 * @author linlazy
 */
@Data
public abstract class Backpack implements BackpackInterface {

    /**
     * 背包格子
     */
    protected Lattice[] latticeArr;
}
