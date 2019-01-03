package com.linlazy.mmorpglintingyang.module.backpack.service;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.backpack.dto.BackPackDTO;
import com.linlazy.mmorpglintingyang.module.backpack.type.BaseBackPack;
import com.linlazy.mmorpglintingyang.server.common.Result;
import org.springframework.stereotype.Component;

/**
 * @author linlazy
 */
@Component
public class BackPackService {

    /**
     * 整理背包
     * @param actorId
     * @param backpackType
     * @return
     */
    public Result<BackPackDTO> arrange(long actorId, int backpackType, JSONObject jsonObject) {

        BaseBackPack baseBackPack = BaseBackPack.getBackPack(backpackType, jsonObject);
        BackPackDTO backPackDTO = baseBackPack.arrangeBackPack();
        return Result.success(backPackDTO);
    }
}
