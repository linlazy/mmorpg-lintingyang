package com.linlazy.mmorpg.module.player.dto;

import com.linlazy.mmorpg.module.player.constants.ProfessionType;
import com.linlazy.mmorpg.module.player.domain.Player;
import lombok.Data;

/**
 * 玩家信息
 * @author linlazy
 */
@Data
public class PlayerDTO {

    /**
     * 玩家ID
     */
    private Long actorId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 血量
     */
    private Integer hp;

    /**
     * 血量上限
     */
    private Integer maxHp;

    /**
     * 蓝量
     */
    private Integer mp;

    /**
     * 金币
     */
    private Long gold;

    /**
     * 等级
     */
    private Integer level;

    /**
     * 攻击力
     */
    private Integer attack;

    /**
     * 防御力
     */
    private Integer defense;

    private String profession;

    /**
     * 经验
     */
    private Long exp;

    /**
     * 经验上限
     */
    private Long maxExp;


    public PlayerDTO(Player player) {
        actorId = player.getActorId();
        username = player.getName();
        hp = player.getHp();
        mp = player.getMp();
        gold = player.getGold();

        level = player.getLevel();
        attack = player.computeAttack();
        defense = player.computeDefense();
        switch (player.getProfession()){
            case ProfessionType
                    .warrior:
                profession = "战士";
            break;
            case ProfessionType
                    .minister:
                profession = "牧师";
                break;
            case ProfessionType
                    .master:
                profession = "法师";
                break;
            case ProfessionType
                    .summoner:
                profession = "召唤师";
                break;
            default:
        }

        exp = player.getExp();

        maxExp = player.getMaxExp();

        maxHp = player.getMaxHP();

    }


    @Override
    public String toString() {
        return String.format("玩家ID【%d】 玩家名称【%s】职业【%s】 攻击力【%d】 防御力【%d】 血量【%d】血量上限【%d】 蓝量【%d】 金币【%d】 等级：【%d】 经验【%d】经验上限【%d】",actorId,username,profession,attack,defense,hp,maxHp,mp,gold,level,exp,maxExp);
    }
}
