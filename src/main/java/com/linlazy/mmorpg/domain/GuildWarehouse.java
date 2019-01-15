package com.linlazy.mmorpg.domain;

import com.linlazy.mmorpg.dao.GuildWarehouseDAO;
import com.linlazy.mmorpg.entity.GuildWarehouseEntity;
import com.linlazy.mmorpg.module.backpack.BackpackInterface;
import com.linlazy.mmorpg.utils.SpringContextUtil;
import lombok.Data;

import java.util.List;
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
    public Lattice[] getBackPack() {
            return latticeArr;

    }


    @Override
    protected void addItem(Item item) {
        GuildWarehouseDAO guildWarehouseDAO = SpringContextUtil.getApplicationContext().getBean(GuildWarehouseDAO.class);
        GuildWarehouseEntity guildWarehouseEntity = new GuildWarehouseEntity();
        guildWarehouseEntity.setGuildId(guildId);
        guildWarehouseEntity.setCount(item.getCount());
        guildWarehouseEntity.setItemId(item.getItemId());
        guildWarehouseDAO.insertQueue(guildWarehouseEntity);
    }

    @Override
    protected void updateItem(Item backPackLattice) {

    }

    @Override
    protected void deleteItem(Item item) {

    }

    @Override
    public boolean pop(List<ItemContext> itemContextList) {
        return true;
    }

    @Override
    public Lattice[] arrangeBackPack() {
        return latticeArr;
    }

    @Override
    protected void refreshBackpack(Lattice[] latticeArr, Lattice[] arrangeBackPack) {

    }

    @Override
    protected Lattice[] initArrangeBackPack() {
        return new Lattice[0];
    }

}
