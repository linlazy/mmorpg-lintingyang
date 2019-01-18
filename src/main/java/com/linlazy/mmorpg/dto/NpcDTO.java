package com.linlazy.mmorpg.dto;

import com.linlazy.mmorpg.domain.Npc;
import lombok.Data;

/**
 * NPC信息DTO
 * @author linlazy
 */
@Data
public class NpcDTO {

    /**
     * NPC名称
     */
    private String npcName;

    /**
     * 怪物血量
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

    public NpcDTO(Npc npc) {
        npcName = npc.getName();
        hp = npc.getHp();
        attack = npc.computeAttack();
        defense = npc.computeDefense();
    }

    @Override
    public String toString() {
        return String.format("NPC名称【%s】 攻击力【%d】 防御力【%d】 血量：%d 等级：【%d】", npcName,attack,defense,hp,level);
    }
}
