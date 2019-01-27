package com.linlazy.mmorpg.module.skill.strategy.taunt;

import com.linlazy.mmorpg.module.scene.constants.SceneEntityType;
import com.linlazy.mmorpg.module.player.domain.Player;
import com.linlazy.mmorpg.module.playercall.domain.PlayerCall;
import com.linlazy.mmorpg.module.scene.domain.SceneEntity;
import com.linlazy.mmorpg.module.skill.domain.Skill;
import com.linlazy.mmorpg.module.player.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 玩家召唤兽嘲讽技能
 * @author linlazy
 */
@Component
public class PlayerCallTauntSkill extends TauntSkill {

    @Autowired
    private PlayerService playerService;

    @Override
    protected Integer sceneEntityType() {
        return SceneEntityType.PLAYER_CALL;
    }

    @Override
    public Set<SceneEntity> selectTauntedSceneEntitySet(SceneEntity sceneEntity, Skill skill, Set<SceneEntity> allSceneEntity) {
        PlayerCall playerCall = (PlayerCall) sceneEntity;
        Player player = playerService.getPlayer(playerCall.getSourceId());
        TauntSkill tauntSkill = TauntSkill.getTauntSkill(SceneEntityType.PLAYER);
        Set<SceneEntity> sceneEntities = tauntSkill.selectTauntedSceneEntitySet(player, skill, allSceneEntity);

        return sceneEntities;
    }
}
