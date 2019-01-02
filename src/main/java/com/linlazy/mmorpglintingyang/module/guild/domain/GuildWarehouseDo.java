package com.linlazy.mmorpglintingyang.module.guild.domain;

import com.linlazy.mmorpglintingyang.module.guild.entity.GuildWarehouse;
import com.linlazy.mmorpglintingyang.module.item.manager.backpack.domain.ItemDo;
import lombok.Data;

@Data
public class GuildWarehouseDo {

    private long guildId;
    private ItemDo itemDo;

   public  GuildWarehouseDo(GuildWarehouse guildWarehouse){
        guildId = guildWarehouse.getGuildId();

        long itemId = guildWarehouse.getItemId();
        itemDo = new ItemDo(itemId);
        itemDo.setCount(guildWarehouse.getCount());
        itemDo.setExt(guildWarehouse.getExt());
    }

    public GuildWarehouse convertGuildWarehouse(){
        GuildWarehouse guildWarehouse = new GuildWarehouse();

        guildWarehouse.setGuildId(guildId);
        guildWarehouse.setItemId(itemDo.getItemId());
        guildWarehouse.setCount(itemDo.getCount());
        guildWarehouse.setExt(itemDo.getExt());

        return guildWarehouse;
    }
}
