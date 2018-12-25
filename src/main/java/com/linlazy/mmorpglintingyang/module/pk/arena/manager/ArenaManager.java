package com.linlazy.mmorpglintingyang.module.pk.arena.manager;

import com.linlazy.mmorpglintingyang.module.pk.arena.domain.ArenaDo;
import com.linlazy.mmorpglintingyang.module.pk.arena.domain.ArenaPlayerDo;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ArenaManager {

    private int maxArenaId = 0;


    private Map<Integer, ArenaDo> arenaIdArenaDoMap = new HashMap<>();

    /**
     * 处理竞技场玩家被击杀
     * @param arenaId
     * @param killId
     * @param killedId
     */
    public void handleArenaActorKilled(int arenaId, int killId, int killedId) {
        ArenaDo arenaDo = arenaIdArenaDoMap.get(arenaId);


        //击杀玩家得100分
        ArenaPlayerDo killArenaPlayerDo = arenaDo.getArenaPlayerDoSet().stream()
                .filter(arenaPlayerDo -> arenaPlayerDo.getActorId() == killId)
                .findFirst().get();
        killArenaPlayerDo.modifyScore(100);


        //被击杀扣50分
        ArenaPlayerDo killedArenaPlayerDo = arenaDo.getArenaPlayerDoSet().stream()
                .filter(arenaPlayerDo -> arenaPlayerDo.getActorId() == killedId)
                .findFirst().get();
        killedArenaPlayerDo.modifyScore(-50);

    }

    public ArenaDo getArenaDo(long arenaId) {
        return arenaIdArenaDoMap.get(arenaId);
    }
}
