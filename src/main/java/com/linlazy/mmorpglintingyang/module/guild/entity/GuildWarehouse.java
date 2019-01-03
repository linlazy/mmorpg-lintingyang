package com.linlazy.mmorpglintingyang.module.guild.entity;

import com.linlazy.mmorpglintingyang.module.item.manager.backpack.domain.ItemDo;
import lombok.Data;

/**
 * 公会仓库
 * @author linlazy
 */
@Data
public class GuildWarehouse {

    /**
     * 公会ID
     */
    private long guildId;

    /**
     * 道具ID
     */
    private long itemId;

    /**
     * 数量
     */
    private int count;

    /**
     * 扩展属性
     */
    private String ext;

    public ItemDo convertItemDo(){
        ItemDo itemDo = new ItemDo();
        itemDo.setItemId(itemId);
        itemDo.setCount(count);
        itemDo.setExt(ext);
        return itemDo;
    }

}
