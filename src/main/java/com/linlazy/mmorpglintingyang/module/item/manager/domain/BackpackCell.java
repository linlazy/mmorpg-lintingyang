package com.linlazy.mmorpglintingyang.module.item.manager.domain;

import com.linlazy.mmorpglintingyang.module.item.manager.entity.Item;
import com.linlazy.mmorpglintingyang.utils.ItemIdUtil;
import lombok.Data;

import java.util.Objects;

@Data
/**
 * 背包格子
 */
public class BackpackCell{

    private long actorId;
    private long itemId;
    private int baseItemId;
    private int orderId;
    private int backPackIndex;
    private int count;
    private String ext;

    public BackpackCell(int backPackIndex) {
        this.backPackIndex = backPackIndex;
    }

    public BackpackCell(Item item) {
        this.actorId = item.getActorId();
        this.itemId = item.getItemId();
        this.baseItemId = ItemIdUtil.getBaseItemId(itemId);
        this.orderId = ItemIdUtil.getOrderId(itemId);
        this.backPackIndex = ItemIdUtil.getBackPackIndex(itemId);
        this.count = item.getCount();
        this.ext = item.getExt();
    }

    public Item convert2Item(){
        return new Item(this.actorId,this.itemId,this.count,this.ext);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BackpackCell that = (BackpackCell) o;
        return backPackIndex == that.backPackIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(backPackIndex);
    }
}
