package com.linlazy.mmorpglintingyang.module.guild.domain;

import com.linlazy.mmorpglintingyang.module.guild.dao.GuildWarehouseDao;
import com.linlazy.mmorpglintingyang.module.guild.entity.GuildWarehouse;
import com.linlazy.mmorpglintingyang.module.item.manager.backpack.domain.ItemDo;
import com.linlazy.mmorpglintingyang.server.common.GlobalConfigService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class GuildBackPack extends BaseBackPack{

    private static Map<Long,GuildBackPack> guildIdBackPackMap = new HashMap<>();


    @Autowired
    private GuildWarehouseDao guildWarehouseDao;
    @Autowired
    private GlobalConfigService globalConfigService;


    /**
     * 仓库背包
     */
    private Set<GuildWarehouseDo> backPack = new HashSet<>();


    /**
     * 获取公会背包
     * @param guildId
     * @return
     */
    public  GuildBackPack getGuildBackPack(long guildId){
        Set<GuildWarehouse> guildWarehouse = guildWarehouseDao.getGuildWarehouse(guildId);
        backPack = guildWarehouse.stream()
                .map(GuildWarehouseDo::new)
                .collect(Collectors.toSet());
        return guildIdBackPackMap.get(guildId);
    }

    @Override
    protected int backpackType() {
        return 1;
    }

    @Override
    public void arrangeBackPack() {

//        Set<GuildWarehouseDo> arrangeBackPack = new HashSet<>(globalConfigService.getGuildPackageMaxLatticeNum());
//        //构建 道具，数量映射
//        Map<ItemDo,Integer>  itemDoTotalMap = getItemDoTotalMap();
//
//        //放进整理背包
//        int backPackLatticeIndex =0;
//        for(Map.Entry<ItemDo,Integer> entry: itemDoTotalMap.entrySet()){
//            ItemDo itemDo = entry.getKey();
//            //可折叠
//            if(itemDo.isSuperPosition()){
//                int itemTotal = entry.getValue();
//                backPackLatticeIndex = pushSuperPositionArrangeBackPack(backPackLatticeIndex,backPack,itemDo,itemTotal);
//            }else {
//                //不可折叠
//                BackPackLattice backPackLattice = new BackPackLattice(backPackLatticeIndex++, itemDo);
//                arrangeBackPack.add(backPackLattice);
//            }
//        }
    }

    private int pushSuperPositionArrangeBackPack(int backPackLatticeIndex, Set<GuildWarehouseDo> backPack, ItemDo itemDo, int itemTotal) {
        return 0;
    }

    private Map<ItemDo, Integer> getItemDoTotalMap() {
        Map<ItemDo,Integer>  itemDoTotalMap = new HashMap<>();
        for(GuildWarehouseDo guildWarehouseDo: backPack){
            ItemDo itemDo = guildWarehouseDo.getItemDo();
            itemDoTotalMap.putIfAbsent(itemDo, 0);
            Integer total = itemDoTotalMap.get(itemDo);
            total += itemDo.getCount();
            itemDoTotalMap.put(itemDo,total);
        }
        return itemDoTotalMap;
    }

    @Override
    public void pushBackPack(Set<ItemDo> itemDoSet) {

    }

    @Override
    public void popBackPack(Set<ItemDo> itemDoSet) {

    }

}
