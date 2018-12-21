package com.linlazy.mmorpglintingyang.module.backpack.service;

import com.linlazy.mmorpglintingyang.module.item.manager.ItemManager;
import com.linlazy.mmorpglintingyang.module.item.manager.backpack.response.BackPackInfo;
import com.linlazy.mmorpglintingyang.server.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BackPackService {

    @Autowired
    private ItemManager itemManager;

    /**
     * 整理背包
     * @param actorId
     * @return
     */
    public Result<BackPackInfo> arrange(long actorId) {
        BackPackInfo backPackInfo = itemManager.arrangeBackPack(actorId);
        return Result.success(backPackInfo);
    }
}
