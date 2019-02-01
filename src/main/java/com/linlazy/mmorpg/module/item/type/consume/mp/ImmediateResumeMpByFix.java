package com.linlazy.mmorpg.module.item.type.consume.mp;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.module.item.domain.Item;
import com.linlazy.mmorpg.module.player.domain.Player;
import com.linlazy.mmorpg.module.player.service.PlayerService;
import com.linlazy.mmorpg.server.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 立即回复HP，通过固定HP数值
 * @author linlazy
 */
@Component
public  class ImmediateResumeMpByFix extends BaseResumeMp {

    @Autowired
    private PlayerService playerService;

    @Override
    protected Integer resumeMpType() {
        return ResumeMpType.immediateResumeByFix;
    }

    @Override
    public Result<?> doResumeMp(long actorId, Item item) {
        JSONObject extJsonObject = item.getExtJsonObject();
        int resumeMP = extJsonObject.getIntValue("resumeMP");
        Player player = playerService.getPlayer(actorId);
        player.resumeMP(resumeMP);
        playerService.updatePlayer(player);
        return Result.success();
    }


}
