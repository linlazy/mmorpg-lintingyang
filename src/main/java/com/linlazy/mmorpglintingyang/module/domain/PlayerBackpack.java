package com.linlazy.mmorpglintingyang.module.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * 玩家背包信息
 * @author linlazy
 */
public class PlayerBackpack {

    /**
     * 玩家ID
     */
    private long actorId;

    /**
     * 背包格子
     */
    List<Lattice> latticeList = new ArrayList<>();
}
