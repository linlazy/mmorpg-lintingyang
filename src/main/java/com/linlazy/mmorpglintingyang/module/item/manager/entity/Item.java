package com.linlazy.mmorpglintingyang.module.item.manager.entity;

import lombok.Data;

@Data
public class Item {

    private long actorId;
    /**
     * order(序号24位) + index(12位背包位置索引) + baseItemId(28位策划配置ID)
     */
    private long itemId;

    private int count;

    /**
     * 扩展属性
     */
    protected String ext;

    public Item() {
    }

    public Item(long actorId, long itemId, int count) {
        this.actorId = actorId;
        this.itemId = itemId;
        this.count = count;
    }

    public Item(long actorId, long itemId, int count, String ext) {
        this.actorId = actorId;
        this.itemId = itemId;
        this.count = count;
        this.ext = ext;
    }


}
