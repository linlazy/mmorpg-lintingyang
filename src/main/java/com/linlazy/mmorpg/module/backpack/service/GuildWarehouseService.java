package com.linlazy.mmorpg.module.backpack.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.linlazy.mmorpg.dao.GuildWarehouseDAO;
import com.linlazy.mmorpg.module.guild.domain.GuildWarehouse;
import com.linlazy.mmorpg.module.item.domain.Item;
import com.linlazy.mmorpg.module.item.domain.ItemContext;
import com.linlazy.mmorpg.module.backpack.domain.Lattice;
import com.linlazy.mmorpg.entity.GuildWarehouseEntity;
import com.linlazy.mmorpg.server.common.GlobalConfigService;
import com.linlazy.mmorpg.server.common.Result;
import com.linlazy.mmorpg.utils.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 公会仓库服务类
 * @author linlazy
 */
@Component
public class GuildWarehouseService {

    private static Logger logger = LoggerFactory.getLogger(PlayerBackpackService.class);

    @Autowired
    private PlayerBackpackService playerBackpackService;

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
                    GlobalConfigService globalConfigService = SpringContextUtil.getApplicationContext().getBean(GlobalConfigService.class);
                    List<GuildWarehouseEntity> guildWarehouseEntitySet = guildWarehouseDAO.getGuildWarehouseEntity(guildId);

                    Lattice[] latticeArr = new Lattice[globalConfigService.getGuildPackageMaxLatticeNum()];

                    guildWarehouseEntitySet.stream()
                            .map(Item::new)
                            .map(Lattice::new)
                            .forEach(
                                    lattice -> {
                                        latticeArr[lattice.getIndex()] = lattice;
                                    }
                            );
                    guildWarehouse.setLatticeArr(latticeArr);

                    return guildWarehouse;
                }
            });

    /**
     * 获取公会仓库
     * @param guildId
     * @return
     */
    public GuildWarehouse getGuildWarehouse(long guildId){

        GuildWarehouse guildWarehouse = null;
        try {
             guildWarehouse = guildWarehouseCache.get(guildId);
        } catch (ExecutionException e) {
            logger.error("{}",e);
        }

        return guildWarehouse;
    }

    /**
     *  公会道具是否足够
     * @param guildId 公会ID
     * @param itemList 取出的道具列表
     * @return
     */
    public Result<?> isEnough(long guildId, List<ItemContext> itemList){
        GuildWarehouse guildWarehouse = getGuildWarehouse(guildId);
        try{
            guildWarehouse.getReadWriteLock().readLock().lock();
            boolean notEnough = guildWarehouse.isNotEnough(itemList);
            if(notEnough){
                return Result.valueOf("公会道具不足");
            }
        }finally {
            guildWarehouse.getReadWriteLock().readLock().unlock();
        }
        return Result.success();
    }

    /**
     *  玩家从公会仓库中取出道具
     * @param guildId 公会ID
     * @param actorId 玩家ID
     * @param itemList 道具列表
     * @return 取出结果
     */
    public Result<?> pop(long guildId,long actorId,List<ItemContext> itemList){

        Result<?> enough = isEnough(guildId, itemList);
        if(enough.isFail()){
            return Result.valueOf(enough.getCode());
        }

        Result<?> full = playerBackpackService.isNotFull(actorId, itemList);
        if(full.isFail()){
            return Result.valueOf(full.getCode());
        }

        GuildWarehouse guildWarehouse = getGuildWarehouse(guildId);
        try{
            guildWarehouse.getReadWriteLock().writeLock().lock();
            guildWarehouse.pop(itemList);
        }finally {
            guildWarehouse.getReadWriteLock().writeLock().unlock();
        }

        return playerBackpackService.push(actorId,itemList);
    }

    public Result<?> push(Long guildId, long actorId, ArrayList<ItemContext> itemContexts) {

        Result<?> enough = playerBackpackService.isEnough(actorId, itemContexts);
        if(enough.isFail()){
            return Result.valueOf(enough.getCode());
        }

        Result<?> notFull = isNotFull(guildId, itemContexts);
        if(notFull.isFail()){
            return Result.valueOf(notFull.getCode());
        }

        GuildWarehouse guildWarehouse = getGuildWarehouse(guildId);
        try{
            guildWarehouse.getReadWriteLock().writeLock().lock();
            guildWarehouse.push(itemContexts);
        }finally {
            guildWarehouse.getReadWriteLock().writeLock().unlock();
        }

        return playerBackpackService.pop(actorId,itemContexts);
    }

    private Result<?> isNotFull(Long guildId, ArrayList<ItemContext> itemContexts) {
        GuildWarehouse guildWarehouse = getGuildWarehouse(guildId);
        try{
            guildWarehouse.getReadWriteLock().readLock().lock();
            boolean full = guildWarehouse.isFull(itemContexts);
            if(full){
                return Result.valueOf("公会仓库已满");
            }
        }finally {
            guildWarehouse.getReadWriteLock().readLock().unlock();
        }
        return Result.success();
    }
}
