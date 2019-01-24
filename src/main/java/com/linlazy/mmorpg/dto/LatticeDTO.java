package com.linlazy.mmorpg.dto;

import com.linlazy.mmorpg.domain.Item;
import com.linlazy.mmorpg.domain.Lattice;
import lombok.Data;

/**
 * 背包格子
 * @author linlazy
 */
@Data
public class LatticeDTO {

    private final Integer backpackIndex;

    private final Item item;

    public LatticeDTO(Lattice lattice) {
        this.backpackIndex = lattice.getIndex();
        this.item = lattice.getItem();
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(String.format("格子编号【%d】",backpackIndex));

        stringBuilder.append(new ItemDTO(item).toString());

        return stringBuilder.toString();
    }
}
