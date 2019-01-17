package com.linlazy.mmorpg.defense.player;

import com.linlazy.mmorpg.domain.Player;
import com.linlazy.mmorpg.file.config.LevelFightConfig;
import com.linlazy.mmorpg.file.service.LevelFightConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 玩家等级防御力
 * @author linlazy
 */
@Component
public class PlayerLevelDefense extends PlayerDefense {

    @Autowired
    private LevelFightConfigService levelFightConfigService;

    @Override
    protected int defenseType() {
        return 1;
    }

    @Override
    protected int computeDefense(Player player) {

        LevelFightConfig levelFightConfig = levelFightConfigService.getLevelFightConfig(player.getProfession());
        return player.getLevel() * levelFightConfig.getDefense();
    }
}
