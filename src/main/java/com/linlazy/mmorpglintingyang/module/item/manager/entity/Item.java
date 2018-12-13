package com.linlazy.mmorpglintingyang.module.item.manager.entity;

import lombok.Data;

@Data
public class Item {

    private long actorId;
    /**
     * order(序号32位) + index(10位背包位置索引) + baseItemId(2位策划配置ID2)
     */
    private long itemId;

    private int count;

    /**
     * 扩展属性
     */
    private String ext;

    public Item(long actorId, long itemId, int count) {
        this.actorId = actorId;
        this.itemId = itemId;
        this.count = count;
    }
}
