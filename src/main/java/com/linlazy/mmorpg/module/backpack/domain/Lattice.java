package com.linlazy.mmorpg.module.backpack.domain;

import com.linlazy.mmorpg.module.item.manager.backpack.domain.ItemDo;
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
