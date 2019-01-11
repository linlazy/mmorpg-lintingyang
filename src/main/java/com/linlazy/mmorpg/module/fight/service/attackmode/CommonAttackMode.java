package com.linlazy.mmorpg.module.fight.service.attackmode;

import com.linlazy.mmorpg.module.fight.validator.FightValidtor;
import com.linlazy.mmorpg.module.scene.manager.SceneManager;
import com.linlazy.mmorpg.module.user.manager.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author linlazy
 */
@Component
public class CommonAttackMode extends BaseAttackMode {

    @Autowired
    private FightValidtor fightValidtor;
    @Autowired
    private UserManager userManager;
    @Autowired
    private SceneManager sceneManager;

    @Override
    protected int attackMode() {
        return AttackModeType.COMMON;
    }

}
