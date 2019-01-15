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
    protected void addItem(Lattice lattice) {
        GuildWarehouseDAO guildWarehouseDAO = SpringContextUtil.getApplicationContext().getBean(GuildWarehouseDAO.class);
        GuildWarehouseEntity guildWarehouseEntity = new GuildWarehouseEntity();
        guildWarehouseEntity.setGuildId(guildId);
        guildWarehouseEntity.setCount(lattice.getItem().getCount());
        guildWarehouseEntity.setItemId(lattice.getItem().getItemId());
        guildWarehouseDAO.insertQueue(guildWarehouseEntity);
    }

    @Override
    protected void updateItem(Lattice backPackLattice) {

    }

    @Override
    public boolean pop(List<ItemContext> itemContextList) {
        return true;
    }

    @Override
    public Lattice[] arrangeBackPack() {
        return latticeArr;
    }

}
