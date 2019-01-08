package com.linlazy.mmorpglintingyang.module.backpack.domain;

import com.linlazy.mmorpglintingyang.module.item.manager.backpack.domain.ItemDo;
import lombok.Data;

/**
 * 背包格子
 * @author linlazy
 */
@Data
public class Lattice {
    private int backpackIndex;
    private ItemDo itemDo;

    public Lattice(int backpackIndex, ItemDo itemDo) {
        this.backpackIndex = backpackIndex;
        this.itemDo = itemDo;
    }


    public Lattice(int backPackLatticeIndex) {
        this.backpackIndex = backPackLatticeIndex;
    }
}
