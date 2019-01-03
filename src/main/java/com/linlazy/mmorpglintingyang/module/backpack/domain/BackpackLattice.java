package com.linlazy.mmorpglintingyang.module.backpack.domain;

import com.linlazy.mmorpglintingyang.module.item.manager.backpack.domain.ItemDo;
import lombok.Data;

/**
 * 背包格子
 * @author linlazy
 */
@Data
public class BackpackLattice {
    private int backpackIndex;
    private ItemDo itemDo;

    public BackpackLattice(int backpackIndex, ItemDo itemDo) {
        this.backpackIndex = backpackIndex;
        this.itemDo = itemDo;
    }


    public BackpackLattice(int backPackLatticeIndex) {
        this.backpackIndex = backPackLatticeIndex;
    }
}
