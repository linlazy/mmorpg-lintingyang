package com.linlazy.mmorpglintingyang.module.backpack.type;

import com.linlazy.mmorpglintingyang.module.backpack.constants.BackPackType;
import com.linlazy.mmorpglintingyang.module.backpack.domain.BackpackLattice;
import com.linlazy.mmorpglintingyang.module.item.manager.backpack.domain.ItemDo;
import com.linlazy.mmorpglintingyang.module.item.manager.dao.ItemDao;
import com.linlazy.mmorpglintingyang.module.item.manager.entity.Item;
import com.linlazy.mmorpglintingyang.server.common.GlobalConfigService;
import com.linlazy.mmorpglintingyang.utils.ItemIdUtil;
import com.linlazy.mmorpglintingyang.utils.SpringContextUtil;
import lombok.Data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 玩家主背包
 * @author linlazy
 */
@Data
public class MainBackPack extends BaseBackPack {

    private static ItemDao itemDao = SpringContextUtil.getApplicationContext().getBean(ItemDao.class);
    private static GlobalConfigService globalConfigService =  SpringContextUtil.getApplicationContext().getBean(GlobalConfigService.class);

    private static Map<Long, MainBackPack> actorIdBackPackMap = new HashMap<>();

    /**
     * 玩家ID
     */
    private long actorId;

    /**
     * 获取玩家主背包背包
     * @param actorId 玩家ID
     * @return  玩家主背包背包
     */
    static MainBackPack getMainBackPack(long actorId){
        if(actorIdBackPackMap.get(actorId) == null){
            MainBackPack mainBackPack = new MainBackPack();
            Set<Item> itemSet = itemDao.getItemSet(actorId);
            Set<BackpackLattice> backpackLatticeSet = itemSet.stream()
                    .map(ItemDo::new)
                    .map(itemDo -> {
                        int backPackIndex = ItemIdUtil.getBackPackIndex(itemDo.getItemId());
                        return new BackpackLattice(backPackIndex, itemDo);
                    })
                    .collect(Collectors.toSet());
            mainBackPack.setBackPack(backpackLatticeSet);
            mainBackPack.setActorId(actorId);
            actorIdBackPackMap.put(actorId,mainBackPack);
        }

        return actorIdBackPackMap.get(actorId);
    }
    @Override
    protected int backpackType() {
        return BackPackType.MAIN;
    }
    @Override
    protected void doArrangeBackpack(Set<BackpackLattice> arrangeBackPack) {
        backPack = arrangeBackPack;
        //存单背包
        itemDao.deleteActorItems(actorId);
        this.backPack.stream()
                .map(BackpackLattice::getItemDo)
                .map(ItemDo::convertItem)
                .forEachOrdered(item -> itemDao.addItem(item));
    }

    @Override
    protected Set<BackpackLattice> newArrangeBackPack() {
        return new HashSet<>(globalConfigService.getMainPackageMaxLatticeNum());
    }
    @Override
    protected void addLattice(BackpackLattice spaceBackPackLattice) {
        itemDao.addItem(spaceBackPackLattice.getItemDo().convertItem());
    }

    @Override
    protected void updateLattice(BackpackLattice backPackLattice) {
        itemDao.updateItem(backPackLattice.getItemDo().convertItem());
    }
    @Override
    protected void deleteLattice(BackpackLattice backPackLattice) {
        itemDao.deleteItem(backPackLattice.getItemDo().convertItem());
    }

//    public BackPackDTO arrangeBackPack() {
//        BackPackDTO backPackDTO = new BackPackDTO(backpackType());
//
//        Set<BackpackLattice> arrangeBackPack = new HashSet<>(globalConfigService.getMainPackageMaxLatticeNum());
//        //构建 道具，数量映射
//        Map<ItemDo,Integer>  itemDoTotalMap = getItemDoTotalMap();
//
//
//        //返回背包变化结果
//        List<BackPackLatticeDTO> backPackLatticeDTOList = new ArrayList<>();
//        //放进整理背包
//        int backPackLatticeIndex =0;
//        for(Map.Entry<ItemDo,Integer> entry: itemDoTotalMap.entrySet()){
//            ItemDo itemDo = entry.getKey();
//            //可折叠
//            if(itemDo.isSuperPosition()){
//                int itemTotal = entry.getValue();
//                backPackLatticeIndex = pushSuperPositionArrangeBackPack(backPackLatticeIndex,arrangeBackPack,itemDo,itemTotal,backPackLatticeDTOList);
//            }else {
//                //不可折叠
//                ItemDo newItemDo = itemDo.clonez();
//                long newItemId = ItemIdUtil.getNewItemId(0, backPackLatticeIndex, itemDo.getBaseItemId());
//                newItemDo.setItemId(newItemId);
//                newItemDo.setCount(1);
//                BackpackLattice backpackLattice = new BackpackLattice(backPackLatticeIndex++,newItemDo);
//                backPack.add(backpackLattice);
//                //返回背包变化结果
//                backPackLatticeDTOList.add( new BackPackLatticeDTO(backpackLattice));
//            }
//        }
//
//        backPackDTO.setBackPackLatticeList(backPackLatticeDTOList);
//        return backPackDTO;
//    }

//    @Override
//    public BackPackDTO pushBackPack(Set<ItemContext> itemContextSet) {
//        BackPackDTO backPackDTO = new BackPackDTO(backpackType());
//
//        List<BackPackLatticeDTO> backPackLatticeDTOList = new ArrayList<>();
//        for(ItemContext itemContext: itemContextSet){
//            //放置折叠物品进背包
//            if(itemContext.isSuperPosition()){
//                List<BackPackLatticeDTO> superPositionBackPackLatticeDTOList = pushSuperPositionBackPack(itemContext);
//                backPackLatticeDTOList.addAll(superPositionBackPackLatticeDTOList);
//
//            //放置非折叠物品进背包
//            }else {
//                List<BackPackLatticeDTO> noSuperPositionBackPackLatticeDTOList = pushNonSuperPositionBackPack(itemContext);
//                backPackLatticeDTOList.addAll(noSuperPositionBackPackLatticeDTOList);
//            }
//        }
//
//        backPackDTO.setBackPackLatticeList(backPackLatticeDTOList);
//        return backPackDTO;
//    }

//    private List<BackPackLatticeDTO> pushNonSuperPositionBackPack(ItemContext itemContext) {
//        List<BackPackLatticeDTO> backPackLatticeDTOList = new ArrayList<>();
//
//        for(int i = 0; i < itemContext.getCount(); i ++){
//            BackpackLattice spaceBackPackLattice = findSpaceBackPackLattice();
//
//            int maxOrderId = backPack.stream()
//                    .map(BackpackLattice::getItemDo)
//                    .filter(itemDo -> itemDo.getBaseItemId() == itemContext.getBaseItemId())
//                    .map(ItemDo::getOrderId)
//                    .max(Integer::compareTo).orElse(0);
//            ItemDo itemDo = new ItemDo();
//            long newItemId = ItemIdUtil.getNewItemId(maxOrderId + 1, spaceBackPackLattice.getBackpackIndex(), itemContext.getBaseItemId());
//            itemDo.setItemId(newItemId);
//            itemDo.setCount(1);
//            if(itemDo.getItemType() == ItemType.EQUIP){
//                itemDao.addItem(new EquipDo(itemDo).convertItemDo().convertItem());
//            }else {
//                itemDao.addItem(itemDo.convertItem());
//            }
//
//            spaceBackPackLattice.setItemDo(itemDo);
//            backPack.add(spaceBackPackLattice);
//            backPackLatticeDTOList.add(new BackPackLatticeDTO(spaceBackPackLattice));
//        }
//        return backPackLatticeDTOList;
//    }

//    private List<BackPackLatticeDTO> pushSuperPositionBackPack(ItemContext itemContext) {
//        List<BackPackLatticeDTO> backPackLatticeDTOList = new ArrayList<>();
//
//        Set<BackpackLattice> hasBaseItemIdBackpackLattice = backPack.stream()
//                .filter(backPackLattice -> (backPackLattice.getItemDo().getBaseItemId() == itemContext.getBaseItemId()))
//                .collect(Collectors.toSet());
//
//        int addItemNum = itemContext.getCount();
//        //放进已有物品格子
//        for(BackpackLattice backPackLattice: hasBaseItemIdBackpackLattice){
//            int count = backPackLattice.getItemDo().getCount();
//
//            //未超过叠加数量
//            if(count + addItemNum <= itemContext.getSuperPositionUp()){
//                backPackLattice.getItemDo().setCount(count + addItemNum);
//                itemDao.updateItem(backPackLattice.getItemDo().convertItem());
//
//                //增加返回结果
//                backPackLatticeDTOList.add(new BackPackLatticeDTO(backPackLattice));
//                break;
//            }
//
//            backPackLattice.getItemDo().setCount(itemContext.getSuperPositionUp());
//            addItemNum -= (itemContext.getSuperPositionUp() - count);
//            backPack.remove(backPackLattice);
//            backPack.add(backPackLattice);
//            itemDao.updateItem(backPackLattice.getItemDo().convertItem());
//
//            //增加返回结果
//            backPackLatticeDTOList.add(new BackPackLatticeDTO(backPackLattice));
//        }
//
//        //放进空格子
//        int times = addItemNum /itemContext.getSuperPositionUp();
//        for(int i =1 ; i<= times; i ++){
//            BackpackLattice spaceBackPackLattice = findSpaceBackPackLattice();
//            ItemDo newItemDo = new ItemDo();
//            spaceBackPackLattice.setItemDo(newItemDo);
//            backPack.add(spaceBackPackLattice);
//            itemDao.addItem(spaceBackPackLattice.getItemDo().convertItem());
//
//            //增加返回结果
//            backPackLatticeDTOList.add(new BackPackLatticeDTO(spaceBackPackLattice));
//        }
//
//        //还有剩余,再放置一格
//        int remainCount = addItemNum- itemContext.getSuperPositionUp() * times;
//        if(remainCount > 0 ){
//            BackpackLattice spaceBackPackLattice = findSpaceBackPackLattice();
//            ItemDo newItemDo = new ItemDo();
//            spaceBackPackLattice.setItemDo(newItemDo);
//            backPack.add(spaceBackPackLattice);
//            itemDao.addItem(spaceBackPackLattice.getItemDo().convertItem());
//
//            //增加返回结果
//            backPackLatticeDTOList.add(new BackPackLatticeDTO(spaceBackPackLattice));
//        }
//
//        return backPackLatticeDTOList;
//    }



//
//    @Override
//    public BackPackDTO popBackPack(Set<ItemContext> itemContextSet) {
//        BackPackDTO backPackDTO = new BackPackDTO(backpackType());
//
//        List<BackPackLatticeDTO> backPackLatticeDTOList = new ArrayList<>();
//        for(ItemContext itemContext: itemContextSet){
//            if(itemContext.isSuperPosition()){
//                List<BackPackLatticeDTO> superPositionBackPackLatticeDTOList = popSuperPositionFromBackPack(itemContext);
//                backPackLatticeDTOList.addAll(superPositionBackPackLatticeDTOList);
//            }else {
//                List<BackPackLatticeDTO> noSuperPositionBackPackLatticeDTOList = popNonSuperPositionFromBackPack(itemContext);
//                backPackLatticeDTOList.addAll(noSuperPositionBackPackLatticeDTOList);
//            }
//        }
//        backPackDTO.setBackPackLatticeList(backPackLatticeDTOList);
//        return backPackDTO;
//    }

//    private List<BackPackLatticeDTO> popNonSuperPositionFromBackPack(ItemContext itemContext) {
//        List<BackPackLatticeDTO> backPackLatticeDTOList = new ArrayList<>();
//
//        for(BackpackLattice backPackLattice: backPack){
//            if(backPackLattice.getItemDo().getItemId() == itemContext.getItemId()){
//                backPack.remove(backPackLattice);
//                itemDao.deleteItem(backPackLattice.getItemDo().convertItem());
//                backPackLattice.getItemDo().setCount(0);
//                backPackLatticeDTOList.add(new BackPackLatticeDTO(backPackLattice));
//            }
//        }
//
//        return backPackLatticeDTOList;
//    }



//    private List<BackPackLatticeDTO> popSuperPositionFromBackPack(ItemContext itemContext) {
//        List<BackPackLatticeDTO> backPackLatticeDTOList = new ArrayList<>();
//
//        List<BackpackLattice> hasBaseItemIdBackPackLatticeList = backPack.stream()
//                .filter(backPackLattice -> backPackLattice.getItemDo().getBaseItemId() == itemContext.getBaseItemId())
//                .collect(Collectors.toList());
//
//        int consumeNum = itemContext.getCount();
//        for(BackpackLattice backPackLattice: hasBaseItemIdBackPackLatticeList){
//            //当前格子满足消耗
//            if(backPackLattice.getItemDo().getCount() > consumeNum){
//                backPackLattice.getItemDo().setCount(backPackLattice.getItemDo().getCount() - consumeNum);
//                itemDao.updateItem(backPackLattice.getItemDo().convertItem());
//                logger.debug("backPackLattice：{}",backPackLattice);
//                backPackLatticeDTOList.add(new BackPackLatticeDTO(backPackLattice));
//                break;
//
//            }
//            //否则，更新背包
//            consumeNum -=backPackLattice.getItemDo().getCount();
//            backPackLattice.getItemDo().setCount(0);
//            itemDao.updateItem(backPackLattice.getItemDo().convertItem());
//            logger.debug("backPackLattice：{}",backPackLattice);
//            backPackLatticeDTOList.add(new BackPackLatticeDTO(backPackLattice));
//        }
//
//        return backPackLatticeDTOList;
//    }

//    private int pushSuperPositionArrangeBackPack(int backPackLatticeIndex, Set<BackpackLattice> backPack, ItemDo itemDo, int itemCount, List<BackPackLatticeDTO> backPackLatticeDTOList) {
//        //计算物品叠加上限所占据格子数
//        int times= itemCount/ itemDo.getSuperPositionUp();
//        for(int i = 1; i <= times ; i++){
//
//            ItemDo newItemDo = itemDo.clonez();
//            long newItemId = ItemIdUtil.getNewItemId(0, backPackLatticeIndex, itemDo.getBaseItemId());
//            newItemDo.setItemId(newItemId);
//            newItemDo.setCount(itemDo.getSuperPositionUp());
//            BackpackLattice backpackLattice = new BackpackLattice(backPackLatticeIndex++,newItemDo);
//            backPack.add(backpackLattice);
//
//            //返回变化结果
//            backPackLatticeDTOList.add(new BackPackLatticeDTO(backpackLattice));
//        }
//
//        //再占据一格子
//        int remainCount = itemCount - times * itemDo.getSuperPositionUp();
//        if(remainCount > 0){
//            ItemDo newItemDo = itemDo.clonez();
//            long newItemId = ItemIdUtil.getNewItemId(0, backPackLatticeIndex, itemDo.getBaseItemId());
//            newItemDo.setItemId(newItemId);
//            newItemDo.setCount(itemDo.getSuperPositionUp());
//            BackpackLattice backpackLattice = new BackpackLattice(backPackLatticeIndex++,newItemDo);
//            backPack.add(backpackLattice);
//
//            //返回变化结果
//            backPackLatticeDTOList.add(new BackPackLatticeDTO(backpackLattice));
//        }
//        return backPackLatticeIndex;
//    }

//    private Map<ItemDo, Integer> getItemDoTotalMap() {
//        Map<ItemDo,Integer>  itemDoTotalMap = new HashMap<>();
//        for(BackpackLattice backpackLattice: backPack){
//            ItemDo itemDo = backpackLattice.getItemDo();
//            itemDoTotalMap.putIfAbsent(itemDo, 0);
//            Integer total = itemDoTotalMap.get(itemDo);
//            total += itemDo.getCount();
//            itemDoTotalMap.put(itemDo,total);
//        }
//        return itemDoTotalMap;
//    }


}
