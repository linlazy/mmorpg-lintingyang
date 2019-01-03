package com.linlazy.mmorpglintingyang.module.backpack.type;

import com.linlazy.mmorpglintingyang.module.backpack.domain.BackpackLattice;
import com.linlazy.mmorpglintingyang.module.backpack.domain.ItemContext;
import com.linlazy.mmorpglintingyang.module.backpack.dto.BackPackDTO;
import com.linlazy.mmorpglintingyang.module.backpack.dto.BackPackLatticeDTO;
import com.linlazy.mmorpglintingyang.module.equip.manager.domain.EquipDo;
import com.linlazy.mmorpglintingyang.module.item.constants.ItemType;
import com.linlazy.mmorpglintingyang.module.item.manager.backpack.domain.ItemDo;
import com.linlazy.mmorpglintingyang.utils.ItemIdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

public abstract class BackPack {

    private  Logger logger = LoggerFactory.getLogger(this.getClass());

    private static Map<Integer, BackPack> map = new HashMap<>();

    protected Set<BackpackLattice> backPack;

    @PostConstruct
    public void init(){
        map.put(backpackType(),this);
    }

    /**
     * 背包类型
     * @return
     */
    protected abstract int backpackType();

    /**
     * 整理背包
     */
    public  BackPackDTO arrangeBackPack(){
        BackPackDTO backPackDTO = new BackPackDTO(backpackType());

        Set<BackpackLattice> arrangeBackPack =newArrangeBackPack();
        //构建 道具，数量映射
        Map<ItemDo,Integer>  itemDoTotalMap = getItemDoTotalMap();

        //返回背包变化结果
        List<BackPackLatticeDTO> backPackLatticeDTOList = new ArrayList<>();
        //放进整理背包
        int backPackLatticeIndex =0;
        for(Map.Entry<ItemDo,Integer> entry: itemDoTotalMap.entrySet()){
            ItemDo itemDo = entry.getKey();
            //可折叠
            if(itemDo.isSuperPosition()){
                int itemTotal = entry.getValue();
                backPackLatticeIndex = pushSuperPositionArrangeBackPack(backPackLatticeIndex,arrangeBackPack,itemDo,itemTotal,backPackLatticeDTOList);
            }else {
                //不可折叠
                ItemDo newItemDo = itemDo.clonez();
                long newItemId = ItemIdUtil.getNewItemId(0, backPackLatticeIndex, itemDo.getBaseItemId());
                newItemDo.setItemId(newItemId);
                newItemDo.setCount(1);
                BackpackLattice backpackLattice = new BackpackLattice(backPackLatticeIndex++,newItemDo);
                arrangeBackPack.add(backpackLattice);
                //返回背包变化结果
                backPackLatticeDTOList.add( new BackPackLatticeDTO(backpackLattice));
            }
        }

        doArrangeBackpack(arrangeBackPack);

        backPackDTO.setBackPackLatticeList(backPackLatticeDTOList);
        return backPackDTO;
    }

    /**
     * 放进背包
     */
    public  BackPackDTO pushBackPack(Set<ItemContext> itemContextSet){
        BackPackDTO backPackDTO = new BackPackDTO(backpackType());

        List<BackPackLatticeDTO> backPackLatticeDTOList = new ArrayList<>();
        for(ItemContext itemContext: itemContextSet){
            //放置折叠物品进背包
            if(itemContext.isSuperPosition()){
                List<BackPackLatticeDTO> superPositionBackPackLatticeDTOList = pushSuperPositionBackPack(itemContext);
                backPackLatticeDTOList.addAll(superPositionBackPackLatticeDTOList);

                //放置非折叠物品进背包
            }else {
                List<BackPackLatticeDTO> noSuperPositionBackPackLatticeDTOList = pushNonSuperPositionBackPack(itemContext);
                backPackLatticeDTOList.addAll(noSuperPositionBackPackLatticeDTOList);
            }
        }

        backPackDTO.setBackPackLatticeList(backPackLatticeDTOList);
        return backPackDTO;
    }

    /**
     * 移出背包
     */
    public  BackPackDTO popBackPack(Set<ItemContext> itemContextSet){
        BackPackDTO backPackDTO = new BackPackDTO(backpackType());

        List<BackPackLatticeDTO> backPackLatticeDTOList = new ArrayList<>();
        for(ItemContext itemContext: itemContextSet){
            if(itemContext.isSuperPosition()){
                List<BackPackLatticeDTO> superPositionBackPackLatticeDTOList = popSuperPositionFromBackPack(itemContext);
                backPackLatticeDTOList.addAll(superPositionBackPackLatticeDTOList);
            }else {
                List<BackPackLatticeDTO> noSuperPositionBackPackLatticeDTOList = popNonSuperPositionFromBackPack(itemContext);
                backPackLatticeDTOList.addAll(noSuperPositionBackPackLatticeDTOList);
            }
        }
        backPackDTO.setBackPackLatticeList(backPackLatticeDTOList);
        return backPackDTO;
    }

    protected  List<BackPackLatticeDTO> popNonSuperPositionFromBackPack(ItemContext itemContext){
        List<BackPackLatticeDTO> backPackLatticeDTOList = new ArrayList<>();

        for(BackpackLattice backPackLattice: backPack){
            if(backPackLattice.getItemDo().getItemId() == itemContext.getItemId()){
                backPack.remove(backPackLattice);

                deleteLattice(backPackLattice);
                backPackLattice.getItemDo().setCount(0);
                backPackLatticeDTOList.add(new BackPackLatticeDTO(backPackLattice));
            }
        }

        return backPackLatticeDTOList;
    }

    protected abstract void deleteLattice(BackpackLattice backPackLattice);

    protected  List<BackPackLatticeDTO> popSuperPositionFromBackPack(ItemContext itemContext){
        List<BackPackLatticeDTO> backPackLatticeDTOList = new ArrayList<>();

        List<BackpackLattice> hasBaseItemIdBackPackLatticeList = backPack.stream()
                .filter(backPackLattice -> backPackLattice.getItemDo().getBaseItemId() == itemContext.getBaseItemId())
                .collect(Collectors.toList());

        int consumeNum = itemContext.getCount();
        for(BackpackLattice backPackLattice: hasBaseItemIdBackPackLatticeList){
            //当前格子满足消耗
            if(backPackLattice.getItemDo().getCount() > consumeNum){
                backPackLattice.getItemDo().setCount(backPackLattice.getItemDo().getCount() - consumeNum);
                updateLattice(backPackLattice);
                logger.debug("backPackLattice：{}",backPackLattice);
                backPackLatticeDTOList.add(new BackPackLatticeDTO(backPackLattice));
                break;

            }
            //否则，更新背包
            consumeNum -=backPackLattice.getItemDo().getCount();
            backPackLattice.getItemDo().setCount(0);
            updateLattice(backPackLattice);
            logger.debug("backPackLattice：{}",backPackLattice);
            backPackLatticeDTOList.add(new BackPackLatticeDTO(backPackLattice));
        }

        return backPackLatticeDTOList;
    }


    protected abstract void addLattice(BackpackLattice spaceBackPackLattice);

    protected abstract void updateLattice(BackpackLattice backPackLattice);

    protected abstract Set<BackpackLattice> newArrangeBackPack();

    protected abstract void doArrangeBackpack(Set<BackpackLattice> arrangeBackPack);


    private List<BackPackLatticeDTO> pushNonSuperPositionBackPack(ItemContext itemContext){
        List<BackPackLatticeDTO> backPackLatticeDTOList = new ArrayList<>();

        for(int i = 0; i < itemContext.getCount(); i ++){
            BackpackLattice spaceBackPackLattice = findSpaceBackPackLattice();

            int maxOrderId = backPack.stream()
                    .map(BackpackLattice::getItemDo)
                    .filter(itemDo -> itemDo.getBaseItemId() == itemContext.getBaseItemId())
                    .map(ItemDo::getOrderId)
                    .max(Integer::compareTo).orElse(0);
            ItemDo itemDo = new ItemDo();
            long newItemId = ItemIdUtil.getNewItemId(maxOrderId + 1, spaceBackPackLattice.getBackpackIndex(), itemContext.getBaseItemId());
            itemDo.setItemId(newItemId);
            itemDo.setCount(1);
            if(itemDo.getItemType() == ItemType.Equip){
                itemDo = new EquipDo(itemDo).convertItemDo();
            }
            spaceBackPackLattice.setItemDo(itemDo);
            addLattice(spaceBackPackLattice);
            backPack.add(spaceBackPackLattice);
            backPackLatticeDTOList.add(new BackPackLatticeDTO(spaceBackPackLattice));
        }
        return backPackLatticeDTOList;
    }

    private List<BackPackLatticeDTO> pushSuperPositionBackPack(ItemContext itemContext){
        List<BackPackLatticeDTO> backPackLatticeDTOList = new ArrayList<>();

        Set<BackpackLattice> hasBaseItemIdBackpackLattice = backPack.stream()
                .filter(backPackLattice -> (backPackLattice.getItemDo().getBaseItemId() == itemContext.getBaseItemId()))
                .collect(Collectors.toSet());

        int addItemNum = itemContext.getCount();
        //放进已有物品格子
        for(BackpackLattice backPackLattice: hasBaseItemIdBackpackLattice){
            int count = backPackLattice.getItemDo().getCount();

            //未超过叠加数量
            if(count + addItemNum <= itemContext.getSuperPositionUp()){
                backPackLattice.getItemDo().setCount(count + addItemNum);
//                itemDao.updateItem(backPackLattice.getItemDo().convertItem());
                updateLattice(backPackLattice);
                //增加返回结果
                backPackLatticeDTOList.add(new BackPackLatticeDTO(backPackLattice));
                break;
            }

            backPackLattice.getItemDo().setCount(itemContext.getSuperPositionUp());
            addItemNum -= (itemContext.getSuperPositionUp() - count);
            backPack.remove(backPackLattice);
            backPack.add(backPackLattice);
//            itemDao.updateItem(backPackLattice.getItemDo().convertItem());

            //增加返回结果
            backPackLatticeDTOList.add(new BackPackLatticeDTO(backPackLattice));
        }

        //放进空格子
        int times = addItemNum /itemContext.getSuperPositionUp();
        for(int i =1 ; i<= times; i ++){
            BackpackLattice spaceBackPackLattice = findSpaceBackPackLattice();
            ItemDo newItemDo = new ItemDo();
            spaceBackPackLattice.setItemDo(newItemDo);
            backPack.add(spaceBackPackLattice);
            addLattice(spaceBackPackLattice);
            //增加返回结果
            backPackLatticeDTOList.add(new BackPackLatticeDTO(spaceBackPackLattice));
        }

        //还有剩余,再放置一格
        int remainCount = addItemNum- itemContext.getSuperPositionUp() * times;
        if(remainCount > 0 ){
            BackpackLattice spaceBackPackLattice = findSpaceBackPackLattice();
            ItemDo newItemDo = new ItemDo();
            spaceBackPackLattice.setItemDo(newItemDo);
            backPack.add(spaceBackPackLattice);
            addLattice(spaceBackPackLattice);
            //增加返回结果
            backPackLatticeDTOList.add(new BackPackLatticeDTO(spaceBackPackLattice));
        }

        return backPackLatticeDTOList;
    }

    private int pushSuperPositionArrangeBackPack(int backPackLatticeIndex, Set<BackpackLattice> arrangeBackPack, ItemDo itemDo, int itemCount, List<BackPackLatticeDTO> backPackLatticeDTOList){
        //计算物品叠加上限所占据格子数
        int times= itemCount/ itemDo.getSuperPositionUp();
        for(int i = 1; i <= times ; i++){

            ItemDo newItemDo = itemDo.clonez();
            long newItemId = ItemIdUtil.getNewItemId(0, backPackLatticeIndex, itemDo.getBaseItemId());
            newItemDo.setItemId(newItemId);
            newItemDo.setCount(itemDo.getSuperPositionUp());
            BackpackLattice backpackLattice = new BackpackLattice(backPackLatticeIndex++,newItemDo);
            backPack.add(backpackLattice);

            //返回变化结果
            backPackLatticeDTOList.add(new BackPackLatticeDTO(backpackLattice));
        }

        //再占据一格子
        int remainCount = itemCount - times * itemDo.getSuperPositionUp();
        if(remainCount > 0){
            ItemDo newItemDo = itemDo.clonez();
            long newItemId = ItemIdUtil.getNewItemId(0, backPackLatticeIndex, itemDo.getBaseItemId());
            newItemDo.setItemId(newItemId);
            newItemDo.setCount(itemDo.getSuperPositionUp());
            BackpackLattice backpackLattice = new BackpackLattice(backPackLatticeIndex++,newItemDo);
            backPack.add(backpackLattice);

            //返回变化结果
            backPackLatticeDTOList.add(new BackPackLatticeDTO(backpackLattice));
        }
        return backPackLatticeIndex;
    }

    private Map<ItemDo, Integer> getItemDoTotalMap() {
        Map<ItemDo,Integer>  itemDoTotalMap = new HashMap<>();
        for(BackpackLattice backpackLattice: backPack){
            ItemDo itemDo = backpackLattice.getItemDo();
            itemDoTotalMap.putIfAbsent(itemDo, 0);
            Integer total = itemDoTotalMap.get(itemDo);
            total += itemDo.getCount();
            itemDoTotalMap.put(itemDo,total);
        }
        return itemDoTotalMap;
    }

    /**
     * 查找背包空格子
     * @return
     */
    private BackpackLattice findSpaceBackPackLattice() {
        int backPackLatticeIndex = 0;
        while(!isSpaceBackPackLattice(backPackLatticeIndex)){
            backPackLatticeIndex++;
        }
        return new BackpackLattice(backPackLatticeIndex);
    }

    /**
     * 是否为背包空格
     * @param backPackLatticeIndex
     * @return
     */
    private boolean isSpaceBackPackLattice(int backPackLatticeIndex) {
        return backPack.stream()
                .noneMatch(backPackLattice -> backPackLattice.getBackpackIndex() == backPackLatticeIndex);
    }

}
