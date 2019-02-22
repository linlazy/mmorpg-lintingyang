package com.linlazy.mmorpg.module.player.domain;

import com.linlazy.mmorpg.dao.ItemDAO;
import com.linlazy.mmorpg.entity.ItemEntity;
import com.linlazy.mmorpg.module.backpack.domain.Backpack;
import com.linlazy.mmorpg.module.backpack.domain.Lattice;
import com.linlazy.mmorpg.module.item.domain.Item;
import com.linlazy.mmorpg.server.common.GlobalConfigService;
import com.linlazy.mmorpg.utils.SpringContextUtil;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 玩家背包信息
 * @author linlazy
 */
@Data
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
    protected void addItem(Item item) {
        ItemDAO itemDAO = SpringContextUtil.getApplicationContext().getBean(ItemDAO.class);
        ItemEntity itemEntity = item.convertItemEntity();
        itemEntity.setActorId(actorId);
        itemDAO.insertQueue(itemEntity);
    }

    @Override
    protected void updateItem(Item item) {
        ItemDAO itemDAO = SpringContextUtil.getApplicationContext().getBean(ItemDAO.class);
        ItemEntity itemEntity = item.convertItemEntity();
        itemEntity.setActorId(actorId);
        itemDAO.updateQueue(itemEntity);
    }

    @Override
    protected void deleteItem(Item item) {
        ItemDAO itemDAO = SpringContextUtil.getApplicationContext().getBean(ItemDAO.class);
        ItemEntity itemEntity = item.convertItemEntity();
        itemEntity.setActorId(actorId);
        itemDAO.deleteQueue(itemEntity);
    }

    public Item getItem(long itemId) {
        return Arrays.stream(latticeArr)
                .filter(Objects::nonNull)
                .filter(lattice -> lattice.getItem().getItemId() == itemId)
                .map(lattice ->  lattice.getItem())
                .findFirst().get();
    }

    public int getItemCount(Item item) {
        Map<Item, Integer> itemTotalMap = getItemTotalMap();
        return itemTotalMap.get(item) == null? 0:itemTotalMap.get(item);
}
}
