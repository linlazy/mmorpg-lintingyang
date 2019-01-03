package com.linlazy.mmorpglintingyang.module.backpack.dto;

import com.linlazy.mmorpglintingyang.module.backpack.domain.BackpackLattice;
import lombok.Data;

/**
 * 背包格子
 * @author linlazy
 */
@Data
public class BackPackLatticeDTO {

    private Long itemId;
    private Integer backpackIndex;
    private Integer count;

    public BackPackLatticeDTO(BackpackLattice backpackLattice) {
        this.backpackIndex = backpackLattice.getBackpackIndex();
        this.itemId = backpackLattice.getItemDo().getItemId();
        this.itemId = backpackLattice.getItemDo().getItemId();
    }
}
