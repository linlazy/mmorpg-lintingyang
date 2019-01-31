package com.linlazy.mmorpg.module.item.effect.hp;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.module.item.domain.Item;
import com.linlazy.mmorpg.module.player.domain.Player;
import com.linlazy.mmorpg.module.player.service.PlayerService;
import com.linlazy.mmorpg.server.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.linlazy.mmorpg.module.item.effect.hp.ResumeHpType.immediateResumeByFix;

/**
 * 立即回复HP，通过固定HP数值
 * @author linlazy
 */
@Component
public  class ImmediateResumeHpByFix extends BaseResumeHp{

    @Autowired
    private PlayerService playerService;

    @Override
    protected Integer resumeHpType() {
        return immediateResumeByFix;
    }

    @Override
    public Result<?> doResumeHp(long actorId,Item item) {
        JSONObject extJsonObject = item.getExtJsonObject();
        int resumeHp = extJsonObject.getIntValue("resumeHp");
        Player player = playerService.getPlayer(actorId);
        player.resumeHP(resumeHp);
        playerService.updatePlayer(player);
        return Result.success();
    }


}
