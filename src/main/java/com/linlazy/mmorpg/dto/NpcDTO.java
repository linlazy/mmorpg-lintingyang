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
     * 怪物名称
     */
    private String monsterName;

    /**
     * 怪物血量
     */
    private Integer hp;

    public NpcDTO(Npc npc) {
        monsterName = npc.getName();
        hp = npc.getHp();
    }

    @Override
    public String toString() {
        return "NpcDTO{" +
                "monsterName='" + monsterName + '\'' +
                ", hp=" + hp +
                '}';
    }
}
