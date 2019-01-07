package com.linlazy.mmorpglintingyang.module.domain;

import com.linlazy.mmorpglintingyang.module.equip.manager.domain.DressedEquip;
import lombok.Data;

/**
 * 玩家领域类
 * @author linlazy
 */
@Data
public class Player {

    /**
     * 玩家
     */
    private long actorId;
    /**
     * 血量
     */
    private int hp;

    /**
     * 蓝
     */
    private int mp;

    /**
     * 金币
     */
    private int gold;

    /**
     * 主背包
     */
    private PlayerBackpack playerBackpack;

    /**
     * 穿戴装备
     */
    private DressedEquip dressedEquip;

    /**
     *  玩家公会信息
     */
    private PlayerGuildInfo playerGuildInfo;

    /**
     *  玩家竞技场信息
     */
    private PlayerArenaInfo playerArenaInfo;

    /**
     * 玩家技能信息
     */
    private PlayerSkillInfo playerSkillInfo;

    /**
     * 玩家任务信息
     */
    private PlayerTaskInfo playerTaskInfo;

    /**
     * 玩家队伍信息
     */
    private PlayerTeamInfo playerTeamInfo;

    /**
     * 玩家邮件信息
     */
    private PlayerEmailInfo playerEmailInfo;

    /**
     * 玩家聊天信息
     */
    private PlayerChatInfo playerChatInfo;
}
