package com.linlazy.mmorpglintingyang.module.item.manager.backpack.domain;

import com.linlazy.mmorpglintingyang.module.item.manager.backpack.response.BackPackInfo;
import com.linlazy.mmorpglintingyang.module.item.manager.backpack.response.BackPackLatticeDTO;
import com.linlazy.mmorpglintingyang.module.item.manager.dao.ItemDao;
import com.linlazy.mmorpglintingyang.module.item.manager.entity.Item;
import com.linlazy.mmorpglintingyang.server.common.GlobalConfigService;
import com.linlazy.mmorpglintingyang.utils.SpringContextUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 玩家背包
 */
public class BackPack {

    private GlobalConfigService globalConfigService = SpringContextUtil.getApplicationContext().getBean(GlobalConfigService.class);

    private ItemDao itemDao = SpringContextUtil.getApplicationContext().getBean(ItemDao.class);;

    /**
     * 玩家
     */
    private long actorId;
    /**
     * 背包
     */
    private Set<BackPackLattice> actorBackPack;


    public BackPack() {
    }

    public BackPack(long actorId) {
        this.actorId = actorId;
        Set<Item> itemSet = itemDao.getItemSet(actorId);
        this.actorBackPack = itemSet.stream()
                .map(ItemDo::new)
                .map(BackPackLattice::new)
                .collect(Collectors.toSet());
    }


    /**
     * 获取玩家背包信息
     * @return
     */
    public BackPackInfo getInfo() {
        BackPackInfo backPackInfo = new BackPackInfo();

        //返回数据
        List<BackPackLatticeDTO> backPackLatticeDTOList = actorBackPack.stream()
                .map(BackPackLattice::convertBackPackLatticeDTO)
                .collect(Collectors.toList());
        backPackInfo.setBackPackLatticeDTOS(backPackLatticeDTOList);
        return backPackInfo;
    }

    /**
     * 是否背包已满
     * @param itemDo
     * @return
     */
    public boolean isFullBackPack(ItemDo itemDo){
        if(itemDo.isSuperPosition()){
            //可叠加
            return isFullBackPackForSuperPosition(itemDo);
        }else {
            //不可叠加
            return isFullBackPackForNonSuperPosition(itemDo);
        }
    }

    /**
     * 放置不可叠放物品是否满背包
     * @param itemDo
     * @return
     */
    private boolean isFullBackPackForNonSuperPosition(ItemDo itemDo) {
        int spaceNum =globalConfigService.getPackageMaxLatticeNum() - actorBackPack.size();
        return spaceNum <itemDo.getCount();
    }

    /**
     * 放置可叠放物品是否满背包
     * @param itemDo
     * @return
     */
    private boolean isFullBackPackForSuperPosition(ItemDo itemDo) {
        //计算玩家背包可放置该物品的总数量 =
        //空格子数量 * 可叠放上限 + 已存放位置可放置数量总和
        int spaceNum = globalConfigService.getPackageMaxLatticeNum() -actorBackPack.size();
        long totalNum = spaceNum * itemDo.getSuperPositionUp() + actorBackPack.stream()
                .filter(backPackLattice -> backPackLattice.getItemDo().getBaseItemId() == itemDo.getBaseItemId())
                .map(backPackLattice -> itemDo.getSuperPositionUp() - backPackLattice.getItemDo().getCount())
                .reduce(0,(a,b)->a+b);
        return totalNum < itemDo.getCount();
    }


    /**
     * 从背包中移除道具
     * @param itemDo
     * @return
     */
    public BackPackInfo popBackPack(ItemDo itemDo){
        BackPackInfo backPackInfo = new BackPackInfo();

        if(itemDo.isSuperPosition()){
            popSuperPositionFromBackPack(itemDo);
        }else {
            popNonSuperPositionFromBackPack(itemDo);
        }

        //存单背包
        itemDao.deleteActorItems(actorId);
        this.actorBackPack.stream()
                .map(BackPackLattice::getItemDo)
                .map(ItemDo::convertItem)
                .forEachOrdered(item -> itemDao.addItem(item));

        //返回数据
        List<BackPackLatticeDTO> backPackLatticeDTOList = actorBackPack.stream()
                .map(BackPackLattice::convertBackPackLatticeDTO)
                .collect(Collectors.toList());
        backPackInfo.setBackPackLatticeDTOS(backPackLatticeDTOList);

        return backPackInfo;
    }

    private void popNonSuperPositionFromBackPack(ItemDo itemDo) {
        for(BackPackLattice backPackLattice: actorBackPack){
            if(backPackLattice.getItemDo().getBackPackIndex() == itemDo.getBackPackIndex()){
                actorBackPack.remove(backPackLattice);
            }
        }
    }

    private void popSuperPositionFromBackPack(ItemDo itemDo) {
        List<BackPackLattice> backPackLatticeList = actorBackPack.stream()
                .filter(backPackLattice -> backPackLattice.getItemDo().getBaseItemId() == itemDo.getBaseItemId())
                .collect(Collectors.toList());

        int consumeNum = itemDo.getCount();
        for(BackPackLattice backPackLattice: backPackLatticeList){
            //当前格子满足消耗
            if(backPackLattice.getItemDo().getCount() > itemDo.getCount()){
                backPackLattice.getItemDo().setCount(backPackLattice.getItemDo().getCount() - consumeNum);
                break;
            }
            //否则，更新背包
            consumeNum -=backPackLattice.getItemDo().getCount();
            backPackLattice.getItemDo().setCount(0);
        }
    }

    /**
     * 获得道具放进背包
     */
    public BackPackInfo pushBackPack(ItemDo itemDo){
        BackPackInfo backPackInfo = new BackPackInfo();

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

        //返回数据
        List<BackPackLatticeDTO> backPackLatticeDTOList = actorBackPack.stream()
                .map(BackPackLattice::convertBackPackLatticeDTO)
                .collect(Collectors.toList());
        backPackInfo.setBackPackLatticeDTOS(backPackLatticeDTOList);
        return backPackInfo;
    }

    /**
     * 整理背包
     */
    public BackPackInfo arrange(){
        BackPackInfo backPackInfo = new BackPackInfo();

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

        //返回数据
        List<BackPackLatticeDTO> backPackLatticeDTOList = actorBackPack.stream()
                .map(BackPackLattice::convertBackPackLatticeDTO)
                .collect(Collectors.toList());
        backPackInfo.setBackPackLatticeDTOS(backPackLatticeDTOList);
        return backPackInfo;
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
                .filter(itemDo1 -> itemDo1.getItemDo().getBaseItemIdOrderIdKey().equals(itemDo.getBaseItemIdOrderIdKey()))
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

        //放进空格子
        int times = itemDo.getCount() /itemDo.getSuperPositionUp();
        for(int i =1 ; i<= times; i ++){
            BackPackLattice spaceBackPackLattice = findSpaceBackPackLattice();
            ItemDo clonez = itemDo.clonez();
            clonez.setCount(itemDo.getSuperPositionUp());
            spaceBackPackLattice.setItemDo(clonez);
            actorBackPack.add(spaceBackPackLattice);
        }

        //还有剩余,再放置一格
        int remainCount = itemDo.getCount() - itemDo.getSuperPositionUp() * times;
        if(remainCount > 0 ){
            BackPackLattice spaceBackPackLattice = findSpaceBackPackLattice();
            ItemDo clonez = itemDo.clonez();
            clonez.setCount(remainCount);
            spaceBackPackLattice.setItemDo(clonez);
            actorBackPack.add(spaceBackPackLattice);
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
