package com.linlazy.mmorpglintingyang.module.backpack.type;

import com.linlazy.mmorpglintingyang.module.backpack.constants.BackPackType;
import com.linlazy.mmorpglintingyang.module.backpack.domain.BackpackLattice;
import com.linlazy.mmorpglintingyang.module.guild.dao.GuildWarehouseDao;
import com.linlazy.mmorpglintingyang.module.guild.entity.GuildWarehouse;
import com.linlazy.mmorpglintingyang.module.item.manager.backpack.domain.ItemDo;
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
 * 公会仓库背包
 */
@Data
public class GuildBackPack extends BackPack{

    private static Map<Long, GuildBackPack> guildIdBackPackMap = new HashMap<>();
    private static GuildWarehouseDao guildWarehouseDao = SpringContextUtil.getApplicationContext().getBean(GuildWarehouseDao.class);
    private static GlobalConfigService globalConfigService =SpringContextUtil.getApplicationContext().getBean(GlobalConfigService.class);

    /**
     * 公会ID
     */
    private long guildId;

    /**
     * 获取公会背包
     * @param guildId 公会ID
     * @return 公会背包
     */
     static GuildBackPack getGuildBackPack(long guildId){
        if(guildIdBackPackMap.get(guildId) == null){

            GuildBackPack guildBackPack = new GuildBackPack();

            Set<GuildWarehouse> guildWarehouse = guildWarehouseDao.getGuildWarehouse(guildId);
            Set<BackpackLattice> backpackLatticeSet = guildWarehouse.stream()
                    .map(GuildWarehouse::convertItemDo)
                    .map(itemDo -> {
                        int backPackIndex = ItemIdUtil.getBackPackIndex(itemDo.getItemId());
                        return new BackpackLattice(backPackIndex, itemDo);
                    }).collect(Collectors.toSet());

            guildBackPack.setBackPack(backpackLatticeSet);
            guildBackPack.setGuildId(guildId);
            guildIdBackPackMap.put(guildId,guildBackPack);
        }

        return guildIdBackPackMap.get(guildId);
    }

    @Override
    protected int backpackType() {
        return BackPackType.GUILD;
    }

    @Override
    protected void deleteLattice(BackpackLattice backPackLattice) {
        guildWarehouseDao.deleteGuildWarehouse(backPackLattice.getItemDo().convertGuildWarehouse());

    }

    @Override
    protected void addLattice(BackpackLattice spaceBackPackLattice) {
        guildWarehouseDao.addGuildWarehouse(spaceBackPackLattice.getItemDo().convertGuildWarehouse());
    }

    @Override
    protected void updateLattice(BackpackLattice backPackLattice) {
        guildWarehouseDao.updateGuildWarehouse(backPackLattice.getItemDo().convertGuildWarehouse());
    }

    @Override
    protected Set<BackpackLattice> newArrangeBackPack() {
        return new HashSet<>(globalConfigService.getGuildPackageMaxLatticeNum());
    }

    @Override
    protected void doArrangeBackpack(Set<BackpackLattice> arrangeBackPack) {
        backPack = arrangeBackPack;
        //存单背包
        guildWarehouseDao.deleteGuildWarehouses(guildId);
        this.backPack.stream()
                .map(BackpackLattice::getItemDo)
                .map(ItemDo::convertGuildWarehouse)
                .forEachOrdered(guildWarehouse -> guildWarehouseDao.addGuildWarehouse(guildWarehouse));
    }

}
