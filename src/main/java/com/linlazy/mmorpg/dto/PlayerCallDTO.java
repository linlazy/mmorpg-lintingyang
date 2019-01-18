package com.linlazy.mmorpg.dto;

import com.linlazy.mmorpg.domain.PlayerCall;
import lombok.Data;

/**
 * 召唤兽信息传递
 * @author linlazy
 */
@Data
public class PlayerCallDTO {

    /**
     * 名称
     */
    private String name;

    /**
     * 攻击力
     */
    private Integer attack;

    /**
     * 防御力
     */
    private Integer defense;

    /**
     * 血量
     */
    private Integer hp;

    /**
     * 等级
     */
    private Integer level;

    public PlayerCallDTO(PlayerCall playerCall) {
        this.name = playerCall.getName();
        this.hp = playerCall.getHp();
        this.attack = playerCall.computeAttack();
        this.defense = playerCall.computeDefense();
        this.level = playerCall.getLevel();
    }

    @Override
    public String toString() {
        return String.format("召唤兽名称【%s】 攻击力【%d】 防御力【%d】 血量：%d 等级：【%d】",name,attack,defense,hp,level);

    }
}
