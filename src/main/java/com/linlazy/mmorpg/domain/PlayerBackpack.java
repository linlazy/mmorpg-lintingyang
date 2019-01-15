package com.linlazy.mmorpg.domain;

import com.linlazy.mmorpg.dao.ItemDAO;
import com.linlazy.mmorpg.entity.ItemEntity;
import com.linlazy.mmorpg.server.common.GlobalConfigService;
import com.linlazy.mmorpg.utils.ItemIdUtil;
import com.linlazy.mmorpg.utils.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

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
    public Lattice[] getBackPack() {
        return latticeArr;
    }










    @Override
    public boolean pop(List<ItemContext> itemContextList) {
        for(ItemContext itemContext: itemContextList){
            if(itemContext.isSuperPosition()){
                popSuperPositionFromBackPack(itemContext);
            }else {
                 popNonSuperPositionFromBackPack(itemContext);
            }
        }
        return true;
    }

    private void popNonSuperPositionFromBackPack(ItemContext itemContext) {
        for(Lattice backPackLattice: Arrays.asList(latticeArr)){
            if(backPackLattice.getItem().getItemId() == itemContext.getItemId()){
                latticeArr[backPackLattice.getIndex()] = null;
            }
        }
    }

    private void popSuperPositionFromBackPack(ItemContext itemContext) {
        List<Lattice> hasBaseItemIdBackPackLatticeList = Arrays.stream(latticeArr)
                .filter(backPackLattice -> ItemIdUtil.getBaseItemId(backPackLattice.getItem().getItemId()) == itemContext.getBaseItemId())
                .collect(Collectors.toList());

        int consumeNum = itemContext.getCount();
        for(Lattice backPackLattice: hasBaseItemIdBackPackLatticeList){
            //当前格子满足消耗
            if(backPackLattice.getItem().getCount() > consumeNum){
                backPackLattice.getItem().setCount(backPackLattice.getItem().getCount() - consumeNum);
                break;

            }
            //否则，更新背包
            consumeNum -=backPackLattice.getItem().getCount();
            latticeArr[backPackLattice.getIndex()] = null;
            logger.debug("backPackLattice：{}",backPackLattice);
        }
    }

    @Override
    public Lattice[] arrangeBackPack() {
        readWriteLock.writeLock().lock();
        try {
            Lattice[] arrangeBackPack = new Lattice[globalConfigService.getMainPackageMaxLatticeNum()];
            //构建 道具，数量映射
            Map<Item,Integer> itemTotalMap = getItemTotalMap();

            //放进整理背包
            int backPackLatticeIndex =0;
            for(Map.Entry<Item,Integer> entry: itemTotalMap.entrySet()){
                Item item = entry.getKey();
                //可折叠
                if(item.isSuperPosition()){
                    int itemTotal = entry.getValue();
                    backPackLatticeIndex = pushSuperPositionArrangeBackPack(backPackLatticeIndex,arrangeBackPack,item,itemTotal);
                }else {
                    //不可折叠
                    Item newItem = item.clonez();
                    long newItemId = ItemIdUtil.getNewItemId(0, backPackLatticeIndex, ItemIdUtil.getBaseItemId(item.getItemId()));
                    newItem.setItemId(newItemId);
                    newItem.setCount(1);
                    Lattice lattice = new Lattice(newItem);
                    arrangeBackPack[ backPackLatticeIndex++] = lattice;
                }
            }
            latticeArr =arrangeBackPack;
            return latticeArr;
        }finally {
            readWriteLock.writeLock().unlock();
        }
    }


    private int pushSuperPositionArrangeBackPack(int backPackLatticeIndex, Lattice[] arrangeBackPack, Item item, int itemTotal) {
        //计算物品叠加上限所占据格子数
        int times= itemTotal/ item.getSuperPositionUp();
        for(int i = 1; i <= times ; i++){

            Item newItem = item.clonez();
            long newItemId = ItemIdUtil.getNewItemId(0, backPackLatticeIndex,ItemIdUtil.getBaseItemId(item.getItemId()));
            newItem.setItemId(newItemId);
            newItem.setCount(item.getSuperPositionUp());
            Lattice lattice = new Lattice(newItem);
            arrangeBackPack[ backPackLatticeIndex++] = lattice;
        }

        //再占据一格子
        int remainCount = itemTotal - times * item.getSuperPositionUp();
        if(remainCount > 0){
            Item newItem = item.clonez();
            long newItemId = ItemIdUtil.getNewItemId(0, backPackLatticeIndex, ItemIdUtil.getBaseItemId(item.getItemId()));
            newItem.setItemId(newItemId);
            newItem.setCount(item.getSuperPositionUp());
            Lattice lattice = new Lattice(newItem);
            arrangeBackPack[ backPackLatticeIndex++] = lattice;
        }
        return backPackLatticeIndex;
    }

    private Map<Item, Integer> getItemTotalMap() {
        Map<Item,Integer>  itemTotalMap = new HashMap<>(latticeArr.length);
        for(Lattice lattice : Arrays.asList(latticeArr)){
            Item item = lattice.getItem();
            itemTotalMap.putIfAbsent(item, 0);
            Integer total = itemTotalMap.get(item);
            total += item.getCount();
            itemTotalMap.put(item,total);
        }
        return itemTotalMap;
    }

    @Override
    protected void addItem(Lattice lattice) {
        ItemDAO itemDAO = SpringContextUtil.getApplicationContext().getBean(ItemDAO.class);
        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setActorId(actorId);
        itemEntity.setItemId(lattice.getItem().getItemId());
        itemEntity.setCount(lattice.getItem().getCount());
        itemDAO.insertQueue(itemEntity);
    }

    @Override
    protected void updateItem(Lattice backPackLattice) {

    }
}
