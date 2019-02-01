package com.linlazy.mmorpg.module.guild.domain;

import com.linlazy.mmorpg.dao.GuildWarehouseDAO;
import com.linlazy.mmorpg.entity.GuildWarehouseEntity;
import com.linlazy.mmorpg.module.backpack.BackpackInterface;
import com.linlazy.mmorpg.module.backpack.domain.Backpack;
import com.linlazy.mmorpg.module.backpack.domain.Lattice;
import com.linlazy.mmorpg.module.item.domain.Item;
import com.linlazy.mmorpg.utils.SpringContextUtil;
import lombok.Data;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 公会仓库
 * @author linlazy
 */
@Data
public class GuildWarehouse extends Backpack implements BackpackInterface {

    /**
     * 公会ID
     */
    private long guildId;

    public GuildWarehouse(long guildId) {
        this.guildId = guildId;
    }

    /**
     * 公会仓库读写锁
     */
    private ReentrantReadWriteLock  readWriteLock= new ReentrantReadWriteLock();


    @Override
    protected void addItem(Item item) {
        GuildWarehouseDAO guildWarehouseDAO = SpringContextUtil.getApplicationContext().getBean(GuildWarehouseDAO.class);

        GuildWarehouseEntity guildWarehouseEntity = new GuildWarehouseEntity();

        guildWarehouseEntity.setGuildId(guildId);
        guildWarehouseEntity.setCount(item.getCount());
        guildWarehouseEntity.setItemId(item.getItemId());
        guildWarehouseEntity.setExtJsonObject(item.getExt());

        guildWarehouseDAO.insertQueue(guildWarehouseEntity);
    }

    @Override
    protected void updateItem(Item item) {
        GuildWarehouseDAO guildWarehouseDAO = SpringContextUtil.getApplicationContext().getBean(GuildWarehouseDAO.class);

        GuildWarehouseEntity guildWarehouseEntity = new GuildWarehouseEntity();

        guildWarehouseEntity.setGuildId(guildId);
        guildWarehouseEntity.setCount(item.getCount());
        guildWarehouseEntity.setItemId(item.getItemId());
        guildWarehouseEntity.setExtJsonObject(item.getExt());

        guildWarehouseDAO.updateQueue(guildWarehouseEntity);
    }

    @Override
    protected void deleteItem(Item item) {
        GuildWarehouseDAO guildWarehouseDAO = SpringContextUtil.getApplicationContext().getBean(GuildWarehouseDAO.class);

        GuildWarehouseEntity guildWarehouseEntity = new GuildWarehouseEntity();

        guildWarehouseEntity.setGuildId(guildId);
        guildWarehouseEntity.setCount(item.getCount());
        guildWarehouseEntity.setItemId(item.getItemId());


        guildWarehouseDAO.deleteQueue(guildWarehouseEntity);
    }


    @Override
    protected Lattice[] initArrangeBackPack() {
        return new Lattice[0];
    }

}
