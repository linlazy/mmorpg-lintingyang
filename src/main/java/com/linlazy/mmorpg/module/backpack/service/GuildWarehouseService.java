package com.linlazy.mmorpg.module.backpack.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.linlazy.mmorpg.dao.GuildWarehouseDAO;
import com.linlazy.mmorpg.domain.GuildWarehouse;
import com.linlazy.mmorpg.domain.Item;
import com.linlazy.mmorpg.domain.Lattice;
import com.linlazy.mmorpg.entity.GuildWarehouseEntity;
import com.linlazy.mmorpg.utils.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 公会仓库服务类
 * @author linlazy
 */
@Component
public class GuildWarehouseService {

    private static Logger logger = LoggerFactory.getLogger(PlayerBackpackService.class);

    /**
     * 公会仓库缓存
     */
    private  static LoadingCache<Long, GuildWarehouse> guildWarehouseCache = CacheBuilder.newBuilder()
            .maximumSize(10)
            .expireAfterAccess(60, TimeUnit.SECONDS)
            .recordStats()
            .build(new CacheLoader<Long, GuildWarehouse>() {
                @Override
                public GuildWarehouse load(Long guildId) {

                    GuildWarehouse guildWarehouse = new GuildWarehouse(guildId);

                    GuildWarehouseDAO guildWarehouseDAO = SpringContextUtil.getApplicationContext().getBean(GuildWarehouseDAO.class);
                    List<GuildWarehouseEntity> guildWarehouseEntitySet = guildWarehouseDAO.getGuildWarehouseEntity(guildId);

                    Lattice[] latticeArr = new Lattice[globalConfigService.getMainPackageMaxLatticeNum()];

                    List<Lattice> latticeList = guildWarehouseEntitySet.stream()
                            .map(Item::new)
                            .map(Lattice::new)
                            .collect(Collectors.toList());
                    guildWarehouse.setLatticeList(latticeList);

                    return guildWarehouse;
                }
            });

    /**
     * 获取公会仓库
     * @param guildId
     * @return
     */
    public static GuildWarehouse getGuildWarehouse(long guildId){

        GuildWarehouse guildWarehouse = null;
        try {
             guildWarehouse = guildWarehouseCache.get(guildId);
        } catch (ExecutionException e) {
            logger.error("{}",e);
        }


        return guildWarehouse;
    }
}
