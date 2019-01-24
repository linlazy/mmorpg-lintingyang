package com.linlazy.mmorpg.module.shop.money;

import com.linlazy.mmorpg.domain.Player;
import com.linlazy.mmorpg.domain.Shop;
import com.linlazy.mmorpg.server.common.Result;
import com.linlazy.mmorpg.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author linlazy
 */
@Component
public class Gold extends BaseMoney{

    @Autowired
    private PlayerService playerService;

    @Override
    protected Integer moneyType() {
        return MoneyType.GOLD;
    }

    @Override
    public Result<?> isEnough(Shop shop) {

        Player player = playerService.getPlayer(shop.getActorId());

        if(player.getGold() < shop.getConsumeMoneyCount()){
            return Result.valueOf("金币不足");
        }

        return Result.success();
    }

    @Override
    public Result<?> consumeMoney(Shop shop) {
        Player player = playerService.getPlayer(shop.getActorId());
        player.consumeGold(shop.getConsumeMoneyCount());
        playerService.updatePlayer(player);
        return Result.success();
    }
}
