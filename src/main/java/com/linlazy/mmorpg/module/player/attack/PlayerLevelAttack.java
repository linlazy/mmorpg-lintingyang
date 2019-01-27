package com.linlazy.mmorpg.module.player.attack;

import com.linlazy.mmorpg.module.player.domain.Player;
import com.linlazy.mmorpg.file.config.LevelFightConfig;
import com.linlazy.mmorpg.file.service.LevelFightConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author linlazy
 */
@Component
public class PlayerLevelAttack extends PlayerAttack{

    @Autowired
    private LevelFightConfigService levelFightConfigService;

    @Override
    protected int attackType() {
        return 1;
    }

    @Override
    protected int computeAttack(Player player) {
        LevelFightConfig levelFightConfig = levelFightConfigService.getLevelFightConfig(player.getProfession());
        return player.getLevel() * levelFightConfig.getAttack();
    }
}
