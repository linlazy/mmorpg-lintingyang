package com.linlazy.mmorpg.dto;

import com.linlazy.mmorpg.constants.ProfessionType;
import com.linlazy.mmorpg.domain.Player;
import lombok.Data;

/**
 * 玩家信息
 * @author linlazy
 */
@Data
public class PlayerDTO {

    /**
     * 用户名
     */
    private String username;
    /**
     * 血量
     */
    private Integer hp;

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


    public PlayerDTO(Player player) {
        username = player.getName();
        hp = player.getHp();
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
    }


    @Override
    public String toString() {
        return String.format("玩家名称【%s】职业【%s】 攻击力【%d】 防御力【%d】 血量：%d 等级：【%d】",username,profession,attack,defense,hp,level);
    }
}
