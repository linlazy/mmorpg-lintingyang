package com.linlazy.mmorpglintingyang.module.backpack.service;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.backpack.dto.BackPackDTO;
import com.linlazy.mmorpglintingyang.module.backpack.type.BackPack;
import com.linlazy.mmorpglintingyang.server.common.Result;
import org.springframework.stereotype.Component;

@Component
public class BackPackService {

    /**
     * 整理背包
     * @param actorId
     * @param backpackType
     * @return
     */
    public Result<BackPackDTO> arrange(long actorId, int backpackType, JSONObject jsonObject) {

        BackPack backPack = BackPack.getBackPack(backpackType, jsonObject);
        BackPackDTO backPackDTO = backPack.arrangeBackPack();
        return Result.success(backPackDTO);
    }
}
