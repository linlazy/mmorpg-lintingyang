package com.linlazy.mmorpglintingyang.module.item.manager.backpack.response;

import com.linlazy.mmorpglintingyang.module.item.manager.backpack.domain.BackPackLattice;
import lombok.Data;

/**
 * @author linlazy
 */
@Data
public class BackPackLatticeDTO {

    /**
     * 格子位置
     */
    private int index;

    /**
     * 道具唯一ID
     */
    private long itemId;
    /**
     * 道具数量
     */
    private int count;


    public BackPackLatticeDTO(BackPackLattice backPackLattice) {
        this.index = backPackLattice.getIndex();
        this.itemId = backPackLattice.getItemDo().getCount();
        this.count = backPackLattice.getItemDo().getCount();
    }
}
