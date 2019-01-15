package com.linlazy.mmorpg.dto;

import com.linlazy.mmorpg.domain.Monster;
import lombok.Data;

/**
 * 怪物信息DTO
 * @author linlazy
 */
@Data
public class MonsterDTO {

    /**
     * 怪物名称
     */
    private String monsterName;

    /**
     * 怪物血量
     */
    private Integer hp;

    public MonsterDTO(Monster monster) {
        monsterName = monster.getName();
        hp = monster.getHp();
    }


}
