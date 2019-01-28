package com.linlazy.mmorpg.module.copy.fighting.domain;

import com.linlazy.mmorpg.module.copy.fighting.service.PlayerFightingCopyProcess;
import com.linlazy.mmorpg.module.scene.copy.domain.Copy;
import lombok.Data;

/**
 * 斗罗大陆副本
 * @author linlazy
 */
@Data
public class FightingCopy extends Copy {

    /**
     * 副本层级
     */
    private int copyLevel;


    /**
     * 当前层级的玩家进度
     */
   PlayerFightingCopyProcess playerFightingCopyProcess = new PlayerFightingCopyProcess();

}
