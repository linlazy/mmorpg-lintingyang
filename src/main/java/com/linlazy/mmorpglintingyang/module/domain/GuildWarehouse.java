package com.linlazy.mmorpglintingyang.module.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * 公会仓库
 * @author linlazy
 */
public class GuildWarehouse {

    /**
     * 公会ID
     */
    private long guildId;

    /**
     * 仓库格子
     */
    List<Lattice> latticeList = new ArrayList<>();
}
