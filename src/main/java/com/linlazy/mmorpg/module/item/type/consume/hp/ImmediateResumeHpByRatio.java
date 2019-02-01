package com.linlazy.mmorpg.module.item.type.consume.hp;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.module.item.domain.Item;
import com.linlazy.mmorpg.module.player.domain.Player;
import com.linlazy.mmorpg.module.player.service.PlayerService;
import com.linlazy.mmorpg.server.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.linlazy.mmorpg.module.item.type.consume.hp.ResumeHpType.immediateResumeByRatio;

/**
 * 立即回复HP，通过比例
 * @author linlazy
 */
@Component
public  class ImmediateResumeHpByRatio extends BaseResumeHp{

    @Autowired
    private PlayerService playerService;

    @Override
    protected Integer resumeHpType() {
        return immediateResumeByRatio;
    }

    @Override
    public Result<?> doResumeHp(long actorId, Item item) {
        JSONObject extJsonObject = item.getExtJsonObject();
        int resumeHpRatio = extJsonObject.getIntValue("resumeHpRatio");
        Player player = playerService.getPlayer(actorId);
        int resumeHp = player.getMaxHP() * resumeHpRatio/100;
        player.resumeHP(resumeHp);
        playerService.updatePlayer(player);
        return Result.success();
    }


}
