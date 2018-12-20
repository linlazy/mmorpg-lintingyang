package com.linlazy.mmorpglintingyang.module.item.manager.backpack.domain;

import com.linlazy.mmorpglintingyang.module.item.manager.backpack.response.BackPackLatticeDTO;
import lombok.Data;

import java.util.Objects;

/**
 * 背包格子
 */
@Data
public class BackPackLattice {
    /**
     * 格子位置
     */
    private int index;

    /**
     * 道具
     */
    ItemDo itemDo;

    public BackPackLattice(ItemDo itemDo) {
        this.itemDo = itemDo;
    }

    public BackPackLattice(int index, ItemDo itemDo) {
        this.index = index;
        this.itemDo = itemDo;
    }

    public BackPackLattice(int backPackLatticeIndex) {
        this.index =backPackLatticeIndex;
    }


    public BackPackLatticeDTO convertBackPackLatticeDTO(){
        BackPackLatticeDTO backPackLatticeDTO = new BackPackLatticeDTO(this);
        return backPackLatticeDTO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BackPackLattice that = (BackPackLattice) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }
}
