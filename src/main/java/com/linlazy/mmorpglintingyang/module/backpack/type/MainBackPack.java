//package com.linlazy.mmorpglintingyang.module.backpack.type;
//
//import com.linlazy.mmorpglintingyang.module.backpack.constants.BackPackType;
//import com.linlazy.mmorpglintingyang.module.backpack.domain.Lattice;
//import com.linlazy.mmorpglintingyang.module.item.manager.backpack.domain.ItemDo;
//import com.linlazy.mmorpglintingyang.module.item.manager.dao.ItemDao;
//import com.linlazy.mmorpglintingyang.module.item.manager.entity.Item;
//import com.linlazy.mmorpglintingyang.server.common.GlobalConfigService;
//import com.linlazy.mmorpglintingyang.utils.ItemIdUtil;
//import com.linlazy.mmorpglintingyang.utils.SpringContextUtil;
//import lombok.Data;
//
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Set;
//import java.util.stream.Collectors;
//
///**
// * 玩家主背包
// * @author linlazy
// */
//@Data
//public class MainBackPack extends BaseBackPack {
//
//    private static ItemDao itemDao = SpringContextUtil.getApplicationContext().getBean(ItemDao.class);
//    private static GlobalConfigService globalConfigService =  SpringContextUtil.getApplicationContext().getBean(GlobalConfigService.class);
//
//    private static Map<Long, MainBackPack> actorIdBackPackMap = new HashMap<>();
//
//    /**
//     * 玩家ID
//     */
//    private long actorId;
//
//    /**
//     * 获取玩家主背包背包
//     * @param actorId 玩家ID
//     * @return  玩家主背包背包
//     */
//    static MainBackPack getMainBackPack(long actorId){
//        if(actorIdBackPackMap.get(actorId) == null){
//            MainBackPack mainBackPack = new MainBackPack();
//            Set<Item> itemSet = itemDao.getItemSet(actorId);
//            Set<Lattice> latticeSet = itemSet.stream()
//                    .map(ItemDo::new)
//                    .map(itemDo -> {
//                        int backPackIndex = ItemIdUtil.getBackPackIndex(itemDo.getItemId());
//                        return new Lattice(backPackIndex, itemDo);
//                    })
//                    .collect(Collectors.toSet());
//            mainBackPack.setBackPack(latticeSet);
//            mainBackPack.setActorId(actorId);
//            actorIdBackPackMap.put(actorId,mainBackPack);
//        }
//
//        return actorIdBackPackMap.get(actorId);
//    }
//    @Override
//    protected int backpackType() {
//        return BackPackType.MAIN;
//    }
//    @Override
//    protected void doArrangeBackpack(Set<Lattice> arrangeBackPack) {
//        backPack = arrangeBackPack;
//        //存单背包
//        itemDao.deleteActorItems(actorId);
//        this.backPack.stream()
//                .map(Lattice::getItemDo)
//                .map(ItemDo::convertItem)
//                .forEachOrdered(item -> itemDao.addItem(item));
//    }
//
//    @Override
//    protected Set<Lattice> newArrangeBackPack() {
//        return new HashSet<>(globalConfigService.getMainPackageMaxLatticeNum());
//    }
//    @Override
//    protected void addLattice(Lattice spaceBackPackLattice) {
//        itemDao.addItem(spaceBackPackLattice.getItemDo().convertItem());
//    }
//
//    @Override
//    protected void updateLattice(Lattice backPackLattice) {
//        itemDao.updateItem(backPackLattice.getItemDo().convertItem());
//    }
//    @Override
//    protected void deleteLattice(Lattice backPackLattice) {
//        itemDao.deleteItem(backPackLattice.getItemDo().convertItem());
//    }
//
//
//}
