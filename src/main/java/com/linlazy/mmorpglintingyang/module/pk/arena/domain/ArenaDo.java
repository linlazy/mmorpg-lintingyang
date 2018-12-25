package com.linlazy.mmorpglintingyang.module.pk.arena.domain;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class ArenaDo {

    /**
     * 玩家ID
     */
    private long actorId;

    private Set<ArenaPlayerDo> arenaPlayerDoSet = new HashSet<>();
}
