package com.linlazy.mmorpg.module.shop.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.linlazy.mmorpg.dao.ShopDAO;
import com.linlazy.mmorpg.module.player.domain.Player;
import com.linlazy.mmorpg.module.player.domain.PlayerShop;
import com.linlazy.mmorpg.module.shop.domain.Shop;
import com.linlazy.mmorpg.entity.ShopEntity;
import com.linlazy.mmorpg.file.config.ShopConfig;
import com.linlazy.mmorpg.file.service.ShopConfigService;
import com.linlazy.mmorpg.module.common.reward.Reward;
import com.linlazy.mmorpg.module.common.reward.RewardService;
import com.linlazy.mmorpg.module.shop.count.ShopBuyCount;
import com.linlazy.mmorpg.module.shop.count.reset.BaseResetCount;
import com.linlazy.mmorpg.module.shop.money.BaseMoney;
import com.linlazy.mmorpg.server.common.Result;
import com.linlazy.mmorpg.module.player.service.PlayerService;
import com.linlazy.mmorpg.utils.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author linlazy
 */
@Component
public class ShopService {


    @Autowired
    private ShopConfigService shopConfigService;
    @Autowired
    private RewardService rewardService;
    @Autowired
    private ShopDAO shopDAO;
    @Autowired
    private PlayerService playerService;

    /**
     * 玩家商店缓存
     */
    public static LoadingCache<Long, PlayerShop> playerShopCache = CacheBuilder.newBuilder()
            .recordStats()
            .build(new CacheLoader<Long, PlayerShop>() {
                @Override
                public PlayerShop load(Long actorId) {
                    PlayerShop playerShop = new PlayerShop();

                    ShopDAO shopDAO = SpringContextUtil.getApplicationContext().getBean(ShopDAO.class);
                    List<ShopEntity> playerShopEntity = shopDAO.getPlayerShopEntity(actorId);
                    playerShopEntity.stream()
                            .forEach(shopEntity -> {
                                Shop shop = new Shop(shopEntity);
                                playerShop.getMap().put(shop.getGoodsId(),shop);
                            });

                    return playerShop;
                }
            });


    public Result<?> buy(long actorId, long goodsId) {

        if(isNotExist(goodsId)){
            return Result.valueOf("商品不存在");
        }


        PlayerShop playerShop = getPlayerShop(actorId);
        Shop shop = playerShop.getMap().get(goodsId);
        if(shop == null){
            shop = new Shop(goodsId);
            shop.setActorId(actorId);
            playerShop.getMap().put(shop.getGoodsId(),shop);
        }

        //购买次数
        Result<?> countEnough = ShopBuyCount.isEnough(shop);
        if(countEnough.isFail()){
            return Result.valueOf(countEnough.getCode());
        }

        //货币消耗
        BaseMoney baseMoney = BaseMoney.getBaseMoney(shop.getMoneyType());
        Result<?> moneyEnough = baseMoney.isEnough(shop);
        if(moneyEnough.isFail()){
            return Result.valueOf(moneyEnough.getCode());
        }
        //更新购买次数
        shop.setHasBuyCount(shop.getHasBuyCount() + 1);
        shopDAO.updateQueue(shop.convertEntity());

        //扣除货币
        baseMoney.consumeMoney(shop);

        //发放奖励
        List<Reward> rewardList = shop.getRewardList();
        rewardService.addRewardList(actorId,rewardList);

        return Result.success();
    }

    public PlayerShop getPlayerShop(long actorId) {
        try {
            return playerShopCache.get(actorId);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean isNotExist(long goodsId){
        return shopConfigService.getGoodsConfig(goodsId) == null;
    }

    public Result<?> shopInfo(long actorId) {

        Collection<ShopConfig> allShopConfig = shopConfigService.getAllShopConfig();
        Player player = playerService.getPlayer(actorId);
        PlayerShop playerShop = player.getPlayerShop();
        allShopConfig.forEach(shopConfig -> {
            Shop shop = playerShop.getMap().get(shopConfig.getGoodsId());
            if(shop == null){
                 shop = new Shop(shopConfig.getGoodsId());
                shop.setActorId(actorId);
                playerShop.getMap().put(shop.getGoodsId(),shop);
            }else {
                BaseResetCount baseResetCount = BaseResetCount.getBaseResetCount(shop.getResetType());
                baseResetCount.doReset(shop);
            }
        });
        return Result.success(playerShop.toString());
    }
}
