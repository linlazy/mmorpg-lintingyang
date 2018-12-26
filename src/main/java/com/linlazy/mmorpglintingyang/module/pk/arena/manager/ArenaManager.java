package com.linlazy.mmorpglintingyang.module.pk.arena.manager;

import com.linlazy.mmorpglintingyang.module.pk.arena.domain.ArenaPlayerDo;
import com.linlazy.mmorpglintingyang.module.pk.arena.entity.Arena;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ArenaManager {

    @Autowired
    private ArenaDao arenaDao;

    /**
     * 处理竞技场玩家被击杀
     * @param arenaId
     * @param killId
     * @param killedId
     */
    public void handleArenaActorKilled(int arenaId, int killId, int killedId) {
        //击杀玩家得100分,击杀数加一
        Arena killArena = arenaDao.getArena(arenaId,killId);
        if(killArena == null){
            killArena =new Arena();
            killArena.setActorId(killId);
            killArena.setArenaId(arenaId);
            arenaDao.addArena(killArena);
        }
        ArenaPlayerDo killArenaPlayerDo = new ArenaPlayerDo(killArena);
        killArenaPlayerDo.modifyScore(100);
        killArenaPlayerDo.increaseKillNum();
        arenaDao.updateArena(killArenaPlayerDo.convertArena());

        //被击玩家杀扣50分,被击杀数加一
        Arena killedArena = arenaDao.getArena(arenaId,killedId);
        if(killedArena == null){
            killedArena =new Arena();
            killedArena.setActorId(killedId);
            killedArena.setArenaId(arenaId);
            arenaDao.addArena(killedArena);
        }
        ArenaPlayerDo killedArenaPlayerDo = new ArenaPlayerDo(killedArena);
        killedArenaPlayerDo.modifyScore(-50);
        killedArenaPlayerDo.increaseKilledNum();
        arenaDao.updateArena(killedArenaPlayerDo.convertArena());

    }

}
