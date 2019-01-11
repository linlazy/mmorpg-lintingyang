//package com.linlazy.mmorpg.module.backpack.type;
//
//import com.alibaba.fastjson.JSONObject;
//import com.linlazy.mmorpg.module.backpack.constants.BackPackType;
//import com.linlazy.mmorpg.module.backpack.domain.Lattice;
//import com.linlazy.mmorpg.module.backpack.domain.ItemContext;
//import com.linlazy.mmorpg.module.backpack.dto.PlayerBackPackDTO;
//import com.linlazy.mmorpg.module.backpack.dto.LatticeDTO;
//import com.linlazy.mmorpg.module.equip.manager.domain.EquipDo;
//import com.linlazy.mmorpg.module.item.constants.ItemType;
//import com.linlazy.mmorpg.module.item.manager.backpack.domain.ItemDo;
//import com.linlazy.mmorpg.utils.ItemIdUtil;
//import lombok.Data;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.*;
//import java.util.stream.Collectors;
///**
// * @author linlazy
// */
//@Data
//public abstract class BaseBackPack {
//
//    private static Logger logger = LoggerFactory.getLogger(BaseBackPack.class);
//
//    protected Set<Lattice> backPack;
//
//    /**
//     * 背包类型
//     * @return
//     */
//    protected abstract int backpackType();
//
//    public static BaseBackPack getBackPack(int backpackType, JSONObject jsonObject){
//        switch (backpackType){
//            case BackPackType.MAIN:
//                return MainBackPack.getMainBackPack(jsonObject.getLongValue("actorId"));
//            case BackPackType.GUILD:
//                return GuildBackPack.getGuildBackPack(jsonObject.getLongValue("guildId"));
//            default:
//                logger.error("[backpackType] not implement");
//                return null;
//        }
//
//    }
//
//
//    /**
//     * 整理背包
//     */
//    public PlayerBackPackDTO arrangeBackPack(){
//        PlayerBackPackDTO playerBackPackDTO = new PlayerBackPackDTO(backpackType());
//
//        Set<Lattice> arrangeBackPack =newArrangeBackPack();
//        //构建 道具，数量映射
//        Map<ItemDo,Integer>  itemDoTotalMap = getItemDoTotalMap();
//
//        //返回背包变化结果
//        List<LatticeDTO> latticeDTOList = new ArrayList<>();
//        //放进整理背包
//        int backPackLatticeIndex =0;
//        for(Map.Entry<ItemDo,Integer> entry: itemDoTotalMap.entrySet()){
//            ItemDo itemDo = entry.getKey();
//            //可折叠
//            if(itemDo.isSuperPosition()){
//                int itemTotal = entry.getValue();
//                backPackLatticeIndex = pushSuperPositionArrangeBackPack(backPackLatticeIndex,arrangeBackPack,itemDo,itemTotal, latticeDTOList);
//            }else {
//                //不可折叠
//                ItemDo newItemDo = itemDo.clonez();
//                long newItemId = ItemIdUtil.getNewItemId(0, backPackLatticeIndex, itemDo.getBaseItemId());
//                newItemDo.setItemId(newItemId);
//                newItemDo.setCount(1);
//                Lattice lattice = new Lattice(backPackLatticeIndex++,newItemDo);
//                arrangeBackPack.add(lattice);
//                //返回背包变化结果
//                latticeDTOList.add( new LatticeDTO(lattice));
//            }
//        }
//
//        doArrangeBackpack(arrangeBackPack);
//
//        playerBackPackDTO.setBackPackLatticeList(latticeDTOList);
//        return playerBackPackDTO;
//    }
//
//    /**
//     * 放进背包
//     */
//    public PlayerBackPackDTO pushBackPack(Set<ItemContext> itemContextSet){
//        PlayerBackPackDTO playerBackPackDTO = new PlayerBackPackDTO(backpackType());
//
//        List<LatticeDTO> latticeDTOList = new ArrayList<>();
//        for(ItemContext itemContext: itemContextSet){
//            //放置折叠物品进背包
//            if(itemContext.isSuperPosition()){
//                List<LatticeDTO> superPositionLatticeDTOList = pushSuperPositionBackPack(itemContext);
//                latticeDTOList.addAll(superPositionLatticeDTOList);
//
//                //放置非折叠物品进背包
//            }else {
//                List<LatticeDTO> noSuperPositionLatticeDTOList = pushNonSuperPositionBackPack(itemContext);
//                latticeDTOList.addAll(noSuperPositionLatticeDTOList);
//            }
//        }
//
//        playerBackPackDTO.setBackPackLatticeList(latticeDTOList);
//        return playerBackPackDTO;
//    }
//
//    /**
//     * 移出背包
//     */
//    public PlayerBackPackDTO popBackPack(Set<ItemContext> itemContextSet){
//        PlayerBackPackDTO playerBackPackDTO = new PlayerBackPackDTO(backpackType());
//
//        List<LatticeDTO> latticeDTOList = new ArrayList<>();
//        for(ItemContext itemContext: itemContextSet){
//            if(itemContext.isSuperPosition()){
//                List<LatticeDTO> superPositionLatticeDTOList = popSuperPositionFromBackPack(itemContext);
//                latticeDTOList.addAll(superPositionLatticeDTOList);
//            }else {
//                List<LatticeDTO> noSuperPositionLatticeDTOList = popNonSuperPositionFromBackPack(itemContext);
//                latticeDTOList.addAll(noSuperPositionLatticeDTOList);
//            }
//        }
//        playerBackPackDTO.setBackPackLatticeList(latticeDTOList);
//        return playerBackPackDTO;
//    }
//
//    protected  List<LatticeDTO> popNonSuperPositionFromBackPack(ItemContext itemContext){
//        List<LatticeDTO> latticeDTOList = new ArrayList<>();
//
//        for(Lattice backPackLattice: backPack){
//            if(backPackLattice.getItemDo().getItemId() == itemContext.getItemId()){
//                backPack.remove(backPackLattice);
//
//                deleteLattice(backPackLattice);
//                backPackLattice.getItemDo().setCount(0);
//                latticeDTOList.add(new LatticeDTO(backPackLattice));
//            }
//        }
//
//        return latticeDTOList;
//    }
//
//    /**
//     * 删除格子
//     * @param backPackLattice 格子信息
//     */
//    protected abstract void deleteLattice(Lattice backPackLattice);
//
//    protected  List<LatticeDTO> popSuperPositionFromBackPack(ItemContext itemContext){
//        List<LatticeDTO> latticeDTOList = new ArrayList<>();
//
//        List<Lattice> hasBaseItemIdBackPackLatticeList = backPack.stream()
//                .filter(backPackLattice -> backPackLattice.getItemDo().getBaseItemId() == itemContext.getBaseItemId())
//                .collect(Collectors.toList());
//
//        int consumeNum = itemContext.getCount();
//        for(Lattice backPackLattice: hasBaseItemIdBackPackLatticeList){
//            //当前格子满足消耗
//            if(backPackLattice.getItemDo().getCount() > consumeNum){
//                backPackLattice.getItemDo().setCount(backPackLattice.getItemDo().getCount() - consumeNum);
//                updateLattice(backPackLattice);
//                logger.debug("backPackLattice：{}",backPackLattice);
//                latticeDTOList.add(new LatticeDTO(backPackLattice));
//                break;
//
//            }
//            //否则，更新背包
//            consumeNum -=backPackLattice.getItemDo().getCount();
//            backPackLattice.getItemDo().setCount(0);
//            updateLattice(backPackLattice);
//            logger.debug("backPackLattice：{}",backPackLattice);
//            latticeDTOList.add(new LatticeDTO(backPackLattice));
//        }
//
//        return latticeDTOList;
//    }
//
//
//    /**
//     * 增加格子信息
//     * @param spaceBackPackLattice 格子信息
//     */
//    protected abstract void addLattice(Lattice spaceBackPackLattice);
//
//    /**
//     * 更新格子信息
//     * @param backPackLattice 格子信息
//     */
//    protected abstract void updateLattice(Lattice backPackLattice);
//
//    /**
//     * 新建整理好的背包容器
//     * @return 返回新建整理好的背包容器
//     */
//    protected abstract Set<Lattice> newArrangeBackPack();
//
//    /**
//     * 执行背包整理
//     * @param arrangeBackPack 整理好的背包容器
//     */
//    protected abstract void doArrangeBackpack(Set<Lattice> arrangeBackPack);
//
//
//    private List<LatticeDTO> pushNonSuperPositionBackPack(ItemContext itemContext){
//        List<LatticeDTO> latticeDTOList = new ArrayList<>();
//
//        for(int i = 0; i < itemContext.getCount(); i ++){
//            Lattice spaceBackPackLattice = findSpaceBackPackLattice();
//
//            int maxOrderId = backPack.stream()
//                    .map(Lattice::getItemDo)
//                    .filter(itemDo -> itemDo.getBaseItemId() == itemContext.getBaseItemId())
//                    .map(ItemDo::getOrderId)
//                    .max(Integer::compareTo).orElse(0);
//            ItemDo itemDo = new ItemDo();
//            long newItemId = ItemIdUtil.getNewItemId(maxOrderId + 1, spaceBackPackLattice.getBackpackIndex(), itemContext.getBaseItemId());
//            itemDo.setItemId(newItemId);
//            itemDo.setCount(1);
//            if(itemDo.getItemType() == ItemType.EQUIP){
//                itemDo = new EquipDo(itemDo).convertItemDo();
//            }
//            spaceBackPackLattice.setItemDo(itemDo);
//            addLattice(spaceBackPackLattice);
//            backPack.add(spaceBackPackLattice);
//            latticeDTOList.add(new LatticeDTO(spaceBackPackLattice));
//        }
//        return latticeDTOList;
//    }
//
//    private List<LatticeDTO> pushSuperPositionBackPack(ItemContext itemContext){
//        List<LatticeDTO> latticeDTOList = new ArrayList<>();
//
//        Set<Lattice> hasBaseItemIdLattice = backPack.stream()
//                .filter(backPackLattice -> (backPackLattice.getItemDo().getBaseItemId() == itemContext.getBaseItemId()))
//                .collect(Collectors.toSet());
//
//        int addItemNum = itemContext.getCount();
//        //放进已有物品格子
//        for(Lattice backPackLattice: hasBaseItemIdLattice){
//            int count = backPackLattice.getItemDo().getCount();
//
//            //未超过叠加数量
//            if(count + addItemNum <= itemContext.getSuperPositionUp()){
//                backPackLattice.getItemDo().setCount(count + addItemNum);
//                updateLattice(backPackLattice);
//                //增加返回结果
//                latticeDTOList.add(new LatticeDTO(backPackLattice));
//                break;
//            }
//
//            backPackLattice.getItemDo().setCount(itemContext.getSuperPositionUp());
//            addItemNum -= (itemContext.getSuperPositionUp() - count);
//            backPack.remove(backPackLattice);
//            backPack.add(backPackLattice);
//
//            //增加返回结果
//            latticeDTOList.add(new LatticeDTO(backPackLattice));
//        }
//
//        //放进空格子
//        int times = addItemNum /itemContext.getSuperPositionUp();
//        for(int i =1 ; i<= times; i ++){
//            Lattice spaceBackPackLattice = findSpaceBackPackLattice();
//            ItemDo newItemDo = new ItemDo();
//            spaceBackPackLattice.setItemDo(newItemDo);
//            backPack.add(spaceBackPackLattice);
//            addLattice(spaceBackPackLattice);
//            //增加返回结果
//            latticeDTOList.add(new LatticeDTO(spaceBackPackLattice));
//        }
//
//        //还有剩余,再放置一格
//        int remainCount = addItemNum- itemContext.getSuperPositionUp() * times;
//        if(remainCount > 0 ){
//            Lattice spaceBackPackLattice = findSpaceBackPackLattice();
//            ItemDo newItemDo = new ItemDo();
//            spaceBackPackLattice.setItemDo(newItemDo);
//            backPack.add(spaceBackPackLattice);
//            addLattice(spaceBackPackLattice);
//            //增加返回结果
//            latticeDTOList.add(new LatticeDTO(spaceBackPackLattice));
//        }
//
//        return latticeDTOList;
//    }
//
//    private int pushSuperPositionArrangeBackPack(int backPackLatticeIndex, Set<Lattice> arrangeBackPack, ItemDo itemDo, int itemCount, List<LatticeDTO> latticeDTOList){
//        //计算物品叠加上限所占据格子数
//        int times= itemCount/ itemDo.getSuperPositionUp();
//        for(int i = 1; i <= times ; i++){
//
//            ItemDo newItemDo = itemDo.clonez();
//            long newItemId = ItemIdUtil.getNewItemId(0, backPackLatticeIndex, itemDo.getBaseItemId());
//            newItemDo.setItemId(newItemId);
//            newItemDo.setCount(itemDo.getSuperPositionUp());
//            Lattice lattice = new Lattice(backPackLatticeIndex++,newItemDo);
//            backPack.add(lattice);
//
//            //返回变化结果
//            latticeDTOList.add(new LatticeDTO(lattice));
//        }
//
//        //再占据一格子
//        int remainCount = itemCount - times * itemDo.getSuperPositionUp();
//        if(remainCount > 0){
//            ItemDo newItemDo = itemDo.clonez();
//            long newItemId = ItemIdUtil.getNewItemId(0, backPackLatticeIndex, itemDo.getBaseItemId());
//            newItemDo.setItemId(newItemId);
//            newItemDo.setCount(itemDo.getSuperPositionUp());
//            Lattice lattice = new Lattice(backPackLatticeIndex++,newItemDo);
//            backPack.add(lattice);
//
//            //返回变化结果
//            latticeDTOList.add(new LatticeDTO(lattice));
//        }
//        return backPackLatticeIndex;
//    }
//
//    private Map<ItemDo, Integer> getItemDoTotalMap() {
//        Map<ItemDo,Integer>  itemDoTotalMap = new HashMap<>(backPack.size());
//        for(Lattice lattice : backPack){
//            ItemDo itemDo = lattice.getItemDo();
//            itemDoTotalMap.putIfAbsent(itemDo, 0);
//            Integer total = itemDoTotalMap.get(itemDo);
//            total += itemDo.getCount();
//            itemDoTotalMap.put(itemDo,total);
//        }
//        return itemDoTotalMap;
//    }
//
//    /**
//     * 查找背包空格子
//     * @return
//     */
//    private Lattice findSpaceBackPackLattice() {
//        int backPackLatticeIndex = 0;
//        while(!isSpaceBackPackLattice(backPackLatticeIndex)){
//            backPackLatticeIndex++;
//        }
//        return new Lattice(backPackLatticeIndex);
//    }
//
//    /**
//     * 是否为背包空格
//     * @param backPackLatticeIndex
//     * @return
//     */
//    private boolean isSpaceBackPackLattice(int backPackLatticeIndex) {
//        return backPack.stream()
//                .noneMatch(backPackLattice -> backPackLattice.getBackpackIndex() == backPackLatticeIndex);
//    }
//
//}