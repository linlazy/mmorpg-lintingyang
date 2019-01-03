package com.linlazy.mmorpglintingyang.module.backpack.dto;

import com.linlazy.mmorpglintingyang.module.backpack.domain.BackpackLattice;
import lombok.Data;

/**
 * 背包格子
 */
@Data
public class BackPackLatticeDTO {

    private long itemId;
    private int backpackIndex;
    private int count;

    public BackPackLatticeDTO(BackpackLattice backpackLattice) {
        this.backpackIndex = backpackLattice.getBackpackIndex();
        this.itemId = backpackLattice.getItemDo().getItemId();
        this.itemId = backpackLattice.getItemDo().getItemId();
    }
}
