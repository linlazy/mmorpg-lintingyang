package com.linlazy.mmorpg.template.skill.strategy.recovery;

import com.linlazy.mmorpg.constants.SceneEntityType;
import com.linlazy.mmorpg.domain.Player;
import com.linlazy.mmorpg.domain.PlayerCall;
import com.linlazy.mmorpg.domain.SceneEntity;
import com.linlazy.mmorpg.domain.Skill;
import com.linlazy.mmorpg.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 玩家召唤兽恢复技能
 * @author linlazy
 */
@Component
public class PlayerCallRecoverySkill extends RecoverySkill {

    @Autowired
    private PlayerService playerService;

    @Override
    protected Integer sceneEntityType() {
        return SceneEntityType.PLAYER_CALL;
    }

    /**
     * 恢复对象为召唤者自身及其组队玩家
     * @param sceneEntity 使用技能对象
     * @param skill 使用技能
     * @param allSceneEntity 同场景对象
     * @return
     */
    @Override
    public Set<SceneEntity> selectRecoverySceneEntitySet(SceneEntity sceneEntity, Skill skill, Set<SceneEntity> allSceneEntity) {
        PlayerCall playerCall = (PlayerCall) sceneEntity;
        Player player = playerService.getPlayer(playerCall.getSourceId());
        RecoverySkill recoverySkill = RecoverySkill.getRecoverySkill(SceneEntityType.PLAYER);
        Set<SceneEntity> sceneEntities = recoverySkill.selectRecoverySceneEntitySet(player, skill, allSceneEntity);

        return sceneEntities;
    }
}
