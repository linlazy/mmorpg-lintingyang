//package com.linlazy.mmorpglintingyang.module.backpack.service;
//
//import com.google.common.cache.CacheBuilder;
//import com.google.common.cache.CacheLoader;
//import com.google.common.cache.LoadingCache;
//import com.linlazy.mmorpglintingyang.module.backpack.dto.GuildWarehouseDTO;
//import com.linlazy.mmorpglintingyang.module.backpack.dto.LatticeDTO;
//import com.linlazy.mmorpglintingyang.module.dao.GuildDAO;
//import com.linlazy.mmorpglintingyang.module.domain.GuildWarehouse;
//import com.linlazy.mmorpglintingyang.module.domain.Item;
//import com.linlazy.mmorpglintingyang.module.domain.Lattice;
//import com.linlazy.mmorpglintingyang.module.entity.GuildWarehouseEntity;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.Set;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.TimeUnit;
//import java.util.stream.Collectors;
//
///**
// * 公会仓库服务类
// * @author linlazy
// */
//@Component
//public class GuildWarehouseService {
//
//    private static Logger logger = LoggerFactory.getLogger(PlayerBackpackService.class);
//
//    /**
//     * 公会仓库缓存
//     */
//    private  static LoadingCache<Long, GuildWarehouse> guildWarehouseCache = CacheBuilder.newBuilder()
//            .maximumSize(10)
//            .expireAfterAccess(60, TimeUnit.SECONDS)
//            .recordStats()
//            .build(new CacheLoader<Long, GuildWarehouse>() {
//                @Override
//                public GuildWarehouse load(Long guildId) {
//
//                    GuildWarehouse guildWarehouse = new GuildWarehouse(guildId);
//
//                    Set<GuildWarehouseEntity> guildWarehouseEntitySet = GuildDAO.getGuildWarehouseEntity(guildId);
//                    List<Lattice> latticeList = guildWarehouseEntitySet.stream()
//                            .map(Item::new)
//                            .map(Lattice::new)
//                            .collect(Collectors.toList());
//                    guildWarehouse.setLatticeList(latticeList);
//
//                    return guildWarehouse;
//                }
//            });
//
//    /**
//     * 获取公会仓库
//     * @param guildId
//     * @return
//     */
//    public static GuildWarehouseDTO getGuildWarehouse(long guildId){
//        GuildWarehouseDTO guildWarehouseDTO = new GuildWarehouseDTO(guildId);
//
//        GuildWarehouse guildWarehouse = null;
//        try {
//             guildWarehouse = guildWarehouseCache.get(guildId);
//        } catch (ExecutionException e) {
//            logger.error("{}",e);
//        }
//
//        List<LatticeDTO> latticeDTOList = guildWarehouse.getBackPack().stream().map(LatticeDTO::new).collect(Collectors.toList());
//        guildWarehouseDTO.setBackPackLatticeList(latticeDTOList);
//
//        return guildWarehouseDTO;
//    }
//}
