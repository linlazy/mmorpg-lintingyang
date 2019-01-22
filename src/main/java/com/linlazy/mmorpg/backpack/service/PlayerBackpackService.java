package com.linlazy.mmorpg.backpack.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.linlazy.mmorpg.constants.ItemType;
import com.linlazy.mmorpg.dao.ItemDAO;
import com.linlazy.mmorpg.domain.*;
import com.linlazy.mmorpg.dto.LatticeDTO;
import com.linlazy.mmorpg.dto.PlayerBackPackDTO;
import com.linlazy.mmorpg.entity.ItemEntity;
import com.linlazy.mmorpg.server.common.GlobalConfigService;
import com.linlazy.mmorpg.server.common.Result;
import com.linlazy.mmorpg.service.PlayerService;
import com.linlazy.mmorpg.utils.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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


    @Autowired
    private PlayerService playerService;

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
                            .map(itemEntity->{
                                Item item= null;
                                if(itemEntity.getItemType() == ItemType.EQUIP){
                                    item = new Equip(itemEntity);
                                }else{
                                    item = new Item(itemEntity);
                                }
                                return item;
                            })
                            .filter(item -> {
                                if(item.getItemType() == ItemType.EQUIP){
                                    Equip equip = (Equip) item;
                                    if(!equip.isDress()){
                                        return true;
                                    }
                                }
                                return false;
                            })
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
    public Result<?> isNotFull(long actorId, List<ItemContext> items)  {
        PlayerBackpack playerBackpack = getPlayerBackpack(actorId);
        boolean full = playerBackpack.isFull(items);
        if(full){
            return Result.valueOf("背包已满");
        }
        return Result.success();
    }

    /**
     *  玩家背包是否足够物品
     * @param actorId 玩家ID
     * @return
     */
    public Result<?> isEnough(long actorId, List<ItemContext> items)  {
        PlayerBackpack playerBackpack = getPlayerBackpack(actorId);
        boolean notEnough = playerBackpack.isNotEnough(items);
        if(notEnough){
            return Result.valueOf("背包物品不足");
        }
        return Result.success();
    }

    /**
     * 放进背包
     * @param actorId
     * @param itemList
     * @return
     */
    public Result<?> push(long actorId, List<ItemContext> itemList) {
        Result<?> notFull = isNotFull(actorId, itemList);
        if(notFull.isFail()){
            return Result.valueOf(notFull.getCode());
        }

        PlayerBackpack playerBackpack = getPlayerBackpack(actorId);
        playerBackpack.push(itemList);

        return Result.success();
    }

    /**
     * 丢弃背包物品
     * @param actorId
     * @param itemList
     * @return
     */
    public Result<?> pop(long actorId, List<ItemContext> itemList) {
        Result<?> enough = isEnough(actorId, itemList);
        if(enough.isFail()){
            return Result.valueOf(enough.getCode());
        }
        PlayerBackpack playerBackpack = getPlayerBackpack(actorId);
        playerBackpack.pop(itemList);
        return Result.success();
    }
}
