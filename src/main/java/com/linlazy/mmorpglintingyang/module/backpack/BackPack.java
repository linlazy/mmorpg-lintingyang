package com.linlazy.mmorpglintingyang.module.backpack;

import com.linlazy.mmorpglintingyang.module.item.manager.config.ItemConfigService;
import com.linlazy.mmorpglintingyang.module.item.manager.dao.ItemDao;
import com.linlazy.mmorpglintingyang.module.item.manager.entity.Item;
import com.linlazy.mmorpglintingyang.server.common.GlobalConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 玩家背包
 */
@Component
public class BackPack {

    @Autowired
    private GlobalConfigService globalConfigService;

    @Autowired
    private ItemConfigService itemConfigService;

    @Autowired
    private ItemDao itemDao;

    /**
     * 玩家
     */
    private long actorId;
    /**
     * 背包
     */
    private Set<BackPackLattice> actorBackPack;

    public BackPack(long actorId) {
        this.actorId = actorId;
        Set<Item> itemSet = itemDao.getItemSet(actorId);
        this.actorBackPack = itemSet.stream()
                .map(ItemDo::new)
                .map(BackPackLattice::new)
                .collect(Collectors.toSet());
    }

    /**
     * 获得道具放进背包
     */
    public void pushBackPack(ItemDo itemDo){

        //放置折叠物品进背包
        if(itemDo.isSuperPosition()){
            pushSuperPositionBackPack(itemDo);

            //放置非折叠物品进背包
        }else {
            pushNonSuperPositionBackPack(itemDo);
        }

        //存单背包
        itemDao.deleteActorItems(actorId);
        this.actorBackPack.stream()
                .map(BackPackLattice::getItemDo)
                .map(ItemDo::convertItem)
                .forEachOrdered(item -> itemDao.addItem(item));
    }

    /**
     * 整理背包
     */
    public void arrange(){
        Set<BackPackLattice> arrangeBackPack = new HashSet<>(globalConfigService.getPackageMaxLatticeNum());

        //构建 道具，数量映射
        Map<ItemDo,Integer>  itemDoTotalMap = getItemDoTotalMap();

        //放进整理背包
        int backPackLatticeIndex =0;
        for(Map.Entry<ItemDo,Integer> entry: itemDoTotalMap.entrySet()){
            ItemDo itemDo = entry.getKey();
            //可折叠
            if(itemDo.isSuperPosition()){
                int itemTotal = entry.getValue();
                backPackLatticeIndex = pushSuperPositionArrangeBackPack(backPackLatticeIndex,arrangeBackPack,itemDo,itemTotal);
            }else {
                //不可折叠
                BackPackLattice backPackLattice = new BackPackLattice(backPackLatticeIndex++, itemDo);
                arrangeBackPack.add(backPackLattice);
            }
        }
        this.actorBackPack = arrangeBackPack;

        //存单背包
        itemDao.deleteActorItems(actorId);
        this.actorBackPack.stream()
                .map(BackPackLattice::getItemDo)
                .map(ItemDo::convertItem)
                .forEachOrdered(item -> itemDao.addItem(item));
    }

    /**
     * 放置非折叠物品进背包
     * @param itemDo
     */
    private void pushNonSuperPositionBackPack(ItemDo itemDo) {
        BackPackLattice spaceBackPackLattice = findSpaceBackPackLattice();
        spaceBackPackLattice.setItemDo(itemDo);
        actorBackPack.add(spaceBackPackLattice);
    }

    /**
     * 查找背包空格子
     * @return
     */
    private BackPackLattice findSpaceBackPackLattice() {
        int backPackLatticeIndex = 0;
        while(!isSpaceBackPackLattice(backPackLatticeIndex)){
            backPackLatticeIndex++;
        }
        return new BackPackLattice(backPackLatticeIndex);
    }

    /**
     * 是否为背包空格
     * @param backPackLatticeIndex
     * @return
     */
    private boolean isSpaceBackPackLattice(int backPackLatticeIndex) {
        return actorBackPack.stream()
                .noneMatch(backPackLattice -> backPackLattice.getIndex() == backPackLatticeIndex);
    }

    /**
     * 放置折叠物品进背包
     * @param itemDo
     */
    private void pushSuperPositionBackPack(ItemDo itemDo) {

        Set<BackPackLattice> backPackLatticeList = actorBackPack.stream()
                .filter(itemDo1 -> itemDo1.getItemDo().getBaseItemIdOrderIdKey() == itemDo.getBaseItemIdOrderIdKey())
                .collect(Collectors.toSet());

        //放进已有物品格子
        int addItemNum = itemDo.getCount();

        for(BackPackLattice backPackLattice: backPackLatticeList){
            int count = backPackLattice.getItemDo().getCount();

            //未超过叠加数量
            if(count + addItemNum <= itemDo.getSuperPositionUp()){
                backPackLattice.getItemDo().setCount(count + addItemNum);
                break;
            }

            backPackLattice.getItemDo().setCount(itemDo.getSuperPositionUp());
            addItemNum -= (itemDo.getSuperPositionUp() - count);
            actorBackPack.remove(backPackLattice);
            actorBackPack.add(backPackLattice);
        }
    }


    /**
     * @return  获取背包内道具与总数量映射
     */
    private Map<ItemDo, Integer> getItemDoTotalMap() {
        Map<ItemDo,Integer>  itemDoTotalMap = new HashMap<>();
        for(BackPackLattice backPackLattice: actorBackPack){
            ItemDo itemDo = backPackLattice.getItemDo();
            itemDoTotalMap.putIfAbsent(itemDo, 0);
            Integer total = itemDoTotalMap.get(itemDo);
            total += itemDo.getCount();
            itemDoTotalMap.put(itemDo,total);
        }
        return itemDoTotalMap;
    }

    /**
     * 将可折叠物品放进背包
     * @param arrangeBackPack 整理后的背包
     * @param itemDo 道具
     * @param itemCount 道具数量
     */
    private int pushSuperPositionArrangeBackPack(int backPackLatticeIndex, Set<BackPackLattice> arrangeBackPack, ItemDo itemDo, int itemCount) {
        //计算物品叠加上限所占据格子数
        int times= itemCount/ itemDo.getSuperPositionUp();
        for(int i = 1; i <= times ; i++){

            ItemDo newItemDo = itemDo.clonez();
            newItemDo.setCount(itemDo.getSuperPositionUp());
            BackPackLattice backPackLattice = new BackPackLattice(backPackLatticeIndex++, newItemDo);
            arrangeBackPack.add(backPackLattice);
        }

        //再占据一格子
        int remainCount = itemCount - times * itemDo.getSuperPositionUp();
        if(remainCount > 0){
            ItemDo newItemDo = itemDo.clonez();
            newItemDo.setCount(itemDo.getSuperPositionUp());
            BackPackLattice backPackLattice = new BackPackLattice(backPackLatticeIndex++, newItemDo);
            arrangeBackPack.add(backPackLattice);
        }
        return backPackLatticeIndex;
    }
}
