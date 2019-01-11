package com.linlazy.mmorpg.module.backpack.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.linlazy.mmorpg.module.backpack.dto.LatticeDTO;
import com.linlazy.mmorpg.module.backpack.dto.PlayerBackPackDTO;
import com.linlazy.mmorpg.dao.ItemDAO;
import com.linlazy.mmorpg.module.domain.Item;
import com.linlazy.mmorpg.module.domain.Lattice;
import com.linlazy.mmorpg.module.domain.PlayerBackpack;
import com.linlazy.mmorpg.entity.ItemEntity;
import com.linlazy.mmorpg.server.common.GlobalConfigService;
import com.linlazy.mmorpg.utils.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 玩家背包服务类，供外部调用
 * @author linlazy
 */
@Component
public class PlayerBackpackService {

    private static Logger logger = LoggerFactory.getLogger(PlayerBackpackService.class);

    /**
     * 玩家背包缓存
     */
    public static LoadingCache<Long, PlayerBackpack> playerBackpackCache = CacheBuilder.newBuilder()
            .maximumSize(10)
            .expireAfterAccess(60, TimeUnit.SECONDS)
            .recordStats()
            .build(new CacheLoader<Long, PlayerBackpack>() {
                @Override
                public PlayerBackpack load(Long actorId) {

                    PlayerBackpack playerBackpack = new PlayerBackpack(actorId);
                    GlobalConfigService globalConfigService =  SpringContextUtil.getApplicationContext().getBean(GlobalConfigService.class);
                    Lattice[] latticeArr = new Lattice[globalConfigService.getMainPackageMaxLatticeNum()];

                    Set<ItemEntity> itemEntitySet = ItemDAO.getItem(actorId);
                    itemEntitySet.stream()
                            .map(Item::new)
                            .map(Lattice::new)
                            .forEach(lattice -> {
                                latticeArr[lattice.getIndex()] = lattice;
                            });

                    playerBackpack.setLatticeArr(latticeArr);

                    return playerBackpack;
                }
            });

    /**
     * 客户端获取玩家背包
     * @param actorId
     * @return
     */
    public static PlayerBackPackDTO getPlayerBackpackDTO(long actorId)  {
        PlayerBackPackDTO playerBackPackDTO =new PlayerBackPackDTO(actorId);

        PlayerBackpack playerBackpack = getPlayerBackpack(actorId);
        if( playerBackpack != null){
            List<LatticeDTO> latticeDTOList = Arrays.stream(playerBackpack.getBackPack()).map(LatticeDTO::new).collect(Collectors.toList());
            playerBackPackDTO.setBackPackLatticeList(latticeDTOList);
        }
        return playerBackPackDTO;
    }

    /**
     * 获取玩家背包
     * @param actorId
     * @return
     */
    public static PlayerBackpack getPlayerBackpack(long actorId)  {
        try {
            return playerBackpackCache.get(actorId);
        } catch (ExecutionException e) {
            logger.error("{}",e);
        }
        return null;
    }
}
