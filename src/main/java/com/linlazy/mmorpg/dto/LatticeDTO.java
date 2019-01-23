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

    private final String name;

    public LatticeDTO(Lattice lattice) {
        this.backpackIndex = lattice.getIndex();
        this.itemId = lattice.getItem().getItemId();
        this.baseItemId = ItemIdUtil.getBaseItemId(this.itemId);
        this.count = lattice.getItem().getCount();
        this.name = lattice.getItem().getName();
    }


    @Override
    public String toString() {


        return String.format("格子编号【%d】 道具配置ID【%d】 道具ID【%d】 道具数量【%d】道具名称【%s】",backpackIndex,baseItemId,itemId,count,name);
    }
}
