package com.linlazy.mmorpg.domain;

import com.linlazy.mmorpg.dao.ItemDAO;
import com.linlazy.mmorpg.entity.ItemEntity;
import com.linlazy.mmorpg.server.common.GlobalConfigService;
import com.linlazy.mmorpg.utils.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 玩家背包信息
 * @author linlazy
 */
public class PlayerBackpack extends Backpack {

    private Logger logger = LoggerFactory.getLogger(PlayerBackpack.class);

    /**
     * 玩家ID
     */
    private long actorId;

    /**
     * 玩家背包读写锁
     */
    private ReentrantReadWriteLock readWriteLock= new ReentrantReadWriteLock();

    private static GlobalConfigService globalConfigService =  SpringContextUtil.getApplicationContext().getBean(GlobalConfigService.class);

    public PlayerBackpack(Long actorId) {
        this.actorId =actorId;
    }

    @Override
    protected Lattice[] initArrangeBackPack() {
        return new Lattice[globalConfigService.getMainPackageMaxLatticeNum()];
    }

    @Override
    protected void refreshBackpack(Lattice[] latticeArr, Lattice[] arrangeBackPack) {

    }

    @Override
    protected void addItem(Item item) {
        ItemDAO itemDAO = SpringContextUtil.getApplicationContext().getBean(ItemDAO.class);
        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setActorId(actorId);
        itemEntity.setItemId(item.getItemId());
        itemEntity.setCount(item.getCount());
        itemDAO.insertQueue(itemEntity);
    }

    @Override
    protected void updateItem(Item item) {
        ItemDAO itemDAO = SpringContextUtil.getApplicationContext().getBean(ItemDAO.class);
        itemDAO.updateQueue(item.convertItemEntity());
    }

    @Override
    protected void deleteItem(Item item) {
        ItemDAO itemDAO = SpringContextUtil.getApplicationContext().getBean(ItemDAO.class);
        itemDAO.deleteQueue(item.convertItemEntity());
    }
}
