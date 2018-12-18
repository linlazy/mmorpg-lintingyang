package com.linlazy.mmorpglintingyang.module.item.handler.dto;

import com.linlazy.mmorpglintingyang.module.item.manager.domain.BackpackCell;
import lombok.Data;

import java.util.Objects;

@Data
/**
 * 背包格子
 */
public class BackpackCellDTO {

    private long itemId;
    private int baseItemId;
    private int backPackIndex;
    private int count;
    private String ext;

    public BackpackCellDTO(BackpackCell backpackCell) {
        this.itemId = backpackCell.getItemId();
        this.baseItemId =backpackCell.getBaseItemId();
        this.backPackIndex = backpackCell.getBackPackIndex();
        this.count = backpackCell.getCount();
        this.ext = backpackCell.getExt();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BackpackCellDTO that = (BackpackCellDTO) o;
        return backPackIndex == that.backPackIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(backPackIndex);
    }
}
