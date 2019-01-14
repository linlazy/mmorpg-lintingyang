package com.linlazy.mmorpg.module.backpack.dto;

import com.linlazy.mmorpg.domain.Lattice;
import lombok.Data;

/**
 * 背包格子
 * @author linlazy
 */
@Data
public class LatticeDTO {

    private final Long itemId;
    private final Integer backpackIndex;
    private final Integer count;

    public LatticeDTO(Lattice lattice) {
        this.backpackIndex = lattice.getIndex();
        this.itemId = lattice.getItem().getItemId();
        this.count = lattice.getItem().getCount();
    }
}
