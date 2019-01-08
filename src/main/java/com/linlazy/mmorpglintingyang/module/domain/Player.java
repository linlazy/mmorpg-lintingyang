package com.linlazy.mmorpglintingyang.module.domain;

import com.linlazy.mmorpglintingyang.module.backpack.service.PlayerBackpackService;
import com.linlazy.mmorpglintingyang.module.equip.manager.domain.DressedEquip;
import lombok.Data;

import java.util.Objects;

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
     * 职业
     */
    private int profession;

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

    public Player(Long actorId) {
        this.actorId =actorId;
    }

    /**
     * 获取玩家背包
     * @return
     */
    public PlayerBackpack getPlayerBackpack(){
        return PlayerBackpackService.getPlayerBackpack(actorId);
    }
    /**
     * 获取玩家队伍
     * @return
     */
    public PlayerTeamInfo getPlayerTeamInfo() {
        return Team.getPlayerTeamInfo(actorId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return actorId == player.actorId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(actorId);
    }
}
