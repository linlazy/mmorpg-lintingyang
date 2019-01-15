package com.linlazy.mmorpg.module.backpack.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.linlazy.mmorpg.dao.ItemDAO;
import com.linlazy.mmorpg.domain.Item;
import com.linlazy.mmorpg.domain.ItemContext;
import com.linlazy.mmorpg.domain.Lattice;
import com.linlazy.mmorpg.domain.PlayerBackpack;
import com.linlazy.mmorpg.dto.LatticeDTO;
import com.linlazy.mmorpg.dto.PlayerBackPackDTO;
import com.linlazy.mmorpg.entity.ItemEntity;
import com.linlazy.mmorpg.server.common.GlobalConfigService;
import com.linlazy.mmorpg.server.common.Result;
import com.linlazy.mmorpg.utils.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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

                    ItemDAO itemDAO = SpringContextUtil.getApplicationContext().getBean(ItemDAO.class);
                    List<ItemEntity> itemEntitySet = itemDAO.getItemList(actorId);

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
    public  Result<?> getPlayerBackpackDTO(long actorId)  {


        PlayerBackPackDTO playerBackPackDTO =new PlayerBackPackDTO(actorId);

        PlayerBackpack playerBackpack = getPlayerBackpack(actorId);
        if( playerBackpack != null){
            List<LatticeDTO> latticeDTOList = Arrays.stream(playerBackpack.getBackPack())
                    .filter(Objects::nonNull)
                    .map(LatticeDTO::new).collect(Collectors.toList());
            playerBackPackDTO.setBackPackLatticeList(latticeDTOList);
        }
        return  Result.success(playerBackPackDTO);
    }

    /**
     * 获取玩家背包
     * @param actorId
     * @return
     */
    public  PlayerBackpack getPlayerBackpack(long actorId)  {
        try {
            return playerBackpackCache.get(actorId);
        } catch (ExecutionException e) {
            logger.error("{}",e);
        }
        return null;
    }

    /**
     *  玩家背包是否已满
     * @param actorId 玩家ID
     * @return
     */
    public Result<?> isNotFull(long actorId, List<ItemContext> itemContexts)  {
        PlayerBackpack playerBackpack = getPlayerBackpack(actorId);
        boolean full = playerBackpack.isFull(itemContexts);
        if(full){
            return Result.valueOf("背包已满");
        }
        return Result.success();
    }

    /**
     * 放进背包
     * @param actorId
     * @param itemContextList
     * @return
     */
    public Result<?> push(long actorId, List<ItemContext> itemContextList) {
        Result<?> notFull = isNotFull(actorId, itemContextList);
        if(notFull.isFail()){
            return Result.valueOf(notFull.getCode());
        }
        PlayerBackpack playerBackpack = getPlayerBackpack(actorId);
        playerBackpack.push(itemContextList);
        return Result.success();
    }
}
