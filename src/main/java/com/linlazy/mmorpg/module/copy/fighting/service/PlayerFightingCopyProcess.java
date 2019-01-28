package com.linlazy.mmorpg.module.copy.fighting.service;

import lombok.Data;

/**
 * @author linlazy
 */
@Data
public class PlayerFightingCopyProcess {

    /**
     * 是否使用技能
     */
    private boolean isUseSkill;

    /**
     * 是否穿戴装备
     */
    private boolean isDressedEquip;

    /**
     * 是否组队
     */
    private boolean isTeam;

    /**
     * 与牧师组队
     */
    private boolean isTeamWithMinister;
}
