package com.linlazy.mmorpg.dto;

import com.linlazy.mmorpg.domain.Lattice;
import com.linlazy.mmorpg.utils.ItemIdUtil;
import lombok.Data;

/**
 * 背包格子
 * @author linlazy
 */
@Data
public class LatticeDTO {

    private final Long itemId;
    private final Integer baseItemId;
    private final Integer backpackIndex;
    private final Integer count;

    public LatticeDTO(Lattice lattice) {
        this.backpackIndex = lattice.getIndex();
        this.itemId = lattice.getItem().getItemId();
        this.baseItemId = ItemIdUtil.getBaseItemId(this.itemId);
        this.count = lattice.getItem().getCount();
    }
}
