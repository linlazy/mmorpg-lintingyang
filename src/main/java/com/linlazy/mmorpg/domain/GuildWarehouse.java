package com.linlazy.mmorpg.domain;

import com.linlazy.mmorpg.module.backpack.BackpackInterface;
import com.linlazy.mmorpg.module.backpack.domain.ItemContext;

import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 公会仓库
 * @author linlazy
 */
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
        readWriteLock.readLock().lock();
        try {
            return latticeArr;
        }finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public boolean addItem(List<ItemContext> itemContextList) {
        readWriteLock.writeLock().lock();
        try {
            return true;
        }finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public boolean consumeItem(List<ItemContext> itemContextList) {
        readWriteLock.writeLock().lock();
        try {
            return true;
        }finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public Lattice[] arrangeBackPack() {
        readWriteLock.writeLock().lock();
        try {

            return latticeArr;
        }finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public boolean moveItem() {
        readWriteLock.writeLock().lock();
        try {
            return true;
        }finally {
            readWriteLock.writeLock().unlock();
        }
    }
}
