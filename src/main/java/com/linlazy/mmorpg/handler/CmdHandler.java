package com.linlazy.mmorpg.handler;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.module.copy.service.CopyService;
import com.linlazy.mmorpg.server.common.Result;
import com.linlazy.mmorpg.server.route.Cmd;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author linlazy
 */
public class CmdHandler {




    @Autowired
    private CopyService copyService;
    /**
     * 进入副本
     * @param jsonObject
     * @return
     */
    @Cmd("enterCopy")
    public Result<?> enterCopy(JSONObject jsonObject){
        long actorId = jsonObject.getLongValue("actorId");
        return copyService.enterCopy(actorId);
    }
}
