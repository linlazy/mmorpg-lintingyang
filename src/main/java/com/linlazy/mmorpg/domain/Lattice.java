package com.linlazy.mmorpg.domain;

import com.linlazy.mmorpg.utils.ItemIdUtil;
import lombok.Data;

/**
 * 格子
 * @author linlazy
 */
@Data
public class Lattice {

    /**
     * 位置
     */
    private int index;

    /**
     * 道具
     */
    private Item item;

    public Lattice(Item item){
        this.index = ItemIdUtil.getBackPackIndex(item.getItemId());
        this.item = item;
    }

    public Lattice(int index) {
        this.index = index;
    }
}
