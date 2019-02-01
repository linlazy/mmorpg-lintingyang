package com.linlazy.mmorpg.module.item.type.consume.transport;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.module.item.domain.Item;
import com.linlazy.mmorpg.module.scene.service.SceneService;
import com.linlazy.mmorpg.server.common.Result;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 固定地点传送
 * @author linlazy
 */
public class FixPositionTransport extends BaseTransport{

    @Autowired
    private SceneService sceneService;


    @Override
    protected Integer transportType() {
        return TransportType.FIX_POSITION;
    }



    @Override
    public Result<?> doTransport(long actorId, Item transportItem) {
        JSONObject ext = transportItem.getExt();
        sceneService.transportTo(actorId,ext.getIntValue("sceneId"));
        return Result.success();
    }

}
