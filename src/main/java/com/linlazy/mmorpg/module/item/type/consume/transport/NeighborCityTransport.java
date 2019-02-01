package com.linlazy.mmorpg.module.item.type.consume.transport;

import com.linlazy.mmorpg.module.item.domain.Item;
import com.linlazy.mmorpg.module.player.domain.Player;
import com.linlazy.mmorpg.module.player.service.PlayerService;
import com.linlazy.mmorpg.server.common.Result;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 邻近城市地点传送
 * @author linlazy
 */
public class NeighborCityTransport extends BaseTransport{

    @Autowired
    private PlayerService playerService;


    @Override
    protected Integer transportType() {
        return TransportType.CITY_POSITION;
    }



    @Override
    public Result<?> doTransport(long actorId, Item transportItem) {
        Player player = playerService.getPlayer(actorId);
        //todo 相邻城市，需先构建地图
        return Result.success();
    }

}
