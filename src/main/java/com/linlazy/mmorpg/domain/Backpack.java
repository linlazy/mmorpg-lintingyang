package com.linlazy.mmorpg.domain;

import com.linlazy.mmorpg.backpack.BackpackInterface;
import com.linlazy.mmorpg.utils.ItemIdUtil;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 背包结构复用
 * @author linlazy
 */
@Data
public abstract class Backpack implements BackpackInterface {

    /**
     * 背包格子
     */
    protected Lattice[] latticeArr;

    @Override
    public boolean isFull(List<ItemContext> itemList) {
        int needSpace = computeNeedSpace(itemList);
        long count = Arrays.stream(latticeArr)
                .filter(Objects::nonNull)
                .count();
        long totalSpace = latticeArr.length -count;
        return totalSpace < needSpace;
    }

    @Override
    public boolean isNotEnough(List<ItemContext> itemList) {
        //计算道具数量
        for(ItemContext itemContext: itemList){
            int hasNum = computeHasNum(itemContext);
            if(hasNum < itemContext.getCount()){
                return true;
            }
        }
        return false;
    }

    private int computeHasNum(ItemContext itemContext) {
        return Arrays.stream(latticeArr)
                .filter(Objects::nonNull)
                .filter(lattice -> ItemIdUtil.getBaseItemId(lattice.getItem().getItemId()) == ItemIdUtil.getBaseItemId(itemContext.getItemId()))
                .map(lattice -> lattice.getItem().getCount())
                .reduce(0,(a,b) -> a + b);
    }

    private int computeNeedSpace(List<ItemContext> itemList) {
        int needSpace = 0;
        for(int i = 0; i < itemList.size(); i ++){
            needSpace +=computeItemNeedSpace(itemList.get(i));
        }
        return needSpace;
    }

    private int computeItemNeedSpace(ItemContext item) {
        int needSpace = 0;

        int ableNum = Arrays.stream(latticeArr)
                .filter(Objects::nonNull)
                .filter(lattice -> ItemIdUtil.getBaseItemId(lattice.getItem().getItemId()) == ItemIdUtil.getBaseItemId(item.getItemId()))
                .map(lattice -> lattice.getItem().getSuperPositionUp() - lattice.getItem().getCount())
                .reduce(0, (a, b) -> a + b);
        //新放入的道具直接放在已放置的位置已足够
        if(ableNum >= item.getCount()){
            return needSpace;
        }

        needSpace =(item.getCount() -ableNum)/item.getSuperPositionUp();
        if(needSpace * item.getSuperPositionUp() <item.getCount() -ableNum){
            needSpace++;
        }
        return needSpace;
    }
    @Override
    public boolean push(List<ItemContext> itemList) {
        for(ItemContext item: itemList){
            //放置折叠物品进背包
            if(item.isSuperPosition()){
                pushSuperPositionBackPack(item);
                //放置非折叠物品进背包
            }else {
                pushNonSuperPositionBackPack(item);
            }
        }
        return true;
    }

    private boolean pushNonSuperPositionBackPack(ItemContext item) {
        for(int i = 0; i < item.getCount(); i ++){
            Lattice spaceBackPackLattice = findSpaceBackPackLattice();

            int maxOrderId = Arrays.stream(latticeArr)
                    .filter(Objects::nonNull)
                    .map(Lattice::getItem)
                    .filter(item1 -> ItemIdUtil.getBaseItemId(item1.getItemId()) == ItemIdUtil.getBaseItemId(item.getItemId()))
                    .map(item1 -> ItemIdUtil.getOrderId(item1.getItemId()) )
                    .max(Integer::compareTo).orElse(0);
            long newItemId = ItemIdUtil.getNewItemId(maxOrderId + 1, spaceBackPackLattice.getIndex(), ItemIdUtil.getBaseItemId(item.getItemId()));
            Item newItem = new Item(newItemId,1);
            spaceBackPackLattice.setItem(newItem);
            latticeArr[spaceBackPackLattice.getIndex()] = spaceBackPackLattice;
            addItem(newItem);
        }
        return true;
    }

    protected abstract void addItem(Item item);

    private boolean pushSuperPositionBackPack(ItemContext item) {

        List<Lattice> hasBaseItemIdLattice = Arrays.asList(latticeArr).stream()
                .filter(Objects::nonNull)
                .filter(lattice -> (ItemIdUtil.getBaseItemId(lattice.getItem().getItemId()) == ItemIdUtil.getBaseItemId(item.getItemId())))
                .collect(Collectors.toList());

        int addItemNum = item.getCount();
        //放进已有物品格子
        for(Lattice backPackLattice: hasBaseItemIdLattice){
            int count = backPackLattice.getItem().getCount();

            //未超过叠加数量
            if(count + addItemNum <= item.getSuperPositionUp()){
                backPackLattice.getItem().setCount(count + addItemNum);
                updateItem(backPackLattice.getItem());
                return true;
            }

            backPackLattice.getItem().setCount(item.getSuperPositionUp());
            updateItem(backPackLattice.getItem());
            addItemNum -= (item.getSuperPositionUp() - count);

        }

        //放进空格子
        int times = addItemNum /item.getSuperPositionUp();
        for(int i =1 ; i<= times; i ++){
            Lattice spaceBackPackLattice = findSpaceBackPackLattice();

            long newItemId = ItemIdUtil.getNewItemId(0,spaceBackPackLattice.getIndex(),ItemIdUtil.getBaseItemId(item.getItemId()));
            int count = item.getSuperPositionUp();
            Item newItem = new Item(newItemId,count);
            spaceBackPackLattice.setItem(newItem);
            latticeArr[spaceBackPackLattice.getIndex()] = spaceBackPackLattice;
            addItem(newItem);
        }

        //还有剩余,再放置一格
        int remainCount = addItemNum- item.getSuperPositionUp() * times;
        if(remainCount > 0 ){
            Lattice spaceBackPackLattice = findSpaceBackPackLattice();
            long newItemId = ItemIdUtil.getNewItemId(0,spaceBackPackLattice.getIndex(),ItemIdUtil.getBaseItemId(item.getItemId()));
            int count = remainCount;
            Item newItem = new Item(newItemId,count);
            spaceBackPackLattice.setItem(newItem);
            latticeArr[spaceBackPackLattice.getIndex()] = spaceBackPackLattice;
            addItem(newItem);
        }
        return true;
    }

    protected abstract void updateItem(Item item);

    protected abstract void deleteItem(Item item);

    private Lattice findSpaceBackPackLattice() {
        for(int i = 0;i < latticeArr.length ; i++){
            if(latticeArr[i] == null){
                return new Lattice(i);
            }
        }
        return null;
    }

    @Override
    public Lattice[] arrangeBackPack() {

        Lattice[] arrangeBackPack =initArrangeBackPack();
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
        refreshBackpack(latticeArr,arrangeBackPack);
        latticeArr =arrangeBackPack;
        return latticeArr;
    }

    protected abstract Lattice[] initArrangeBackPack();


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

    @Override
    public boolean pop(List<ItemContext> items) {
        for(ItemContext item: items){
            if(item.isSuperPosition()){
                popSuperPositionFromBackPack(item);
            }else {
                popNonSuperPositionFromBackPack(item);
            }
        }
        return true;
    }

    private void popNonSuperPositionFromBackPack(ItemContext item) {
        for(Lattice backPackLattice: Arrays.asList(latticeArr)){
            if(backPackLattice.getItem().getItemId() == item.getItemId()){
                latticeArr[backPackLattice.getIndex()] = null;
                updateItem( backPackLattice.getItem());
            }
        }
    }

    private void popSuperPositionFromBackPack(ItemContext item) {
        List<Lattice> hasBaseItemIdBackPackLatticeList = Arrays.stream(latticeArr)
                .filter(Objects::nonNull)
                .filter(backPackLattice -> ItemIdUtil.getBaseItemId(backPackLattice.getItem().getItemId()) == ItemIdUtil.getBaseItemId(backPackLattice.getItem().getItemId()))
                .collect(Collectors.toList());

        int consumeNum = item.getCount();
        for(Lattice backPackLattice: hasBaseItemIdBackPackLatticeList){
            //当前格子满足消耗
            if(backPackLattice.getItem().getCount() > consumeNum){
                backPackLattice.getItem().setCount(backPackLattice.getItem().getCount() - consumeNum);
                updateItem( backPackLattice.getItem());
                break;

            }
            //否则，更新背包
            consumeNum -=backPackLattice.getItem().getCount();
            latticeArr[backPackLattice.getIndex()] = null;
            deleteItem(backPackLattice.getItem());
        }
    }

    @Override
    public Lattice[] getBackPack() {
        return latticeArr;
    }


    private void refreshBackpack(Lattice[] latticeArr, Lattice[] arrangeBackPack) {
        Arrays.stream(latticeArr)
                .filter(Objects::nonNull)
                .map(Lattice::getItem)
                .forEach(item -> {
                    deleteItem(item);
                });

        Arrays.stream(arrangeBackPack)
                .filter(Objects::nonNull)
                .map(Lattice::getItem)
                .forEach(item -> {
                    addItem(item);
                });
    }
}
