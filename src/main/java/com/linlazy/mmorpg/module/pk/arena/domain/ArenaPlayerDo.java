package com.linlazy.mmorpg.module.pk.arena.domain;

import com.linlazy.mmorpg.module.pk.arena.entity.Arena;
import com.linlazy.mmorpg.module.pk.arena.manager.ArenaManager;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 竞技场玩家
 * @author linlazy
 */
@Data
public class ArenaPlayerDo {

    /**
     * 竞技场ID
     */
    private long arenaId;

    /**
     * 玩家ID
     */
    private long actorId;

    /**
     * 玩家积分
     */
    private int score;

    /**
     * 击杀人数
     */
    private int killNum;

    /**
     * 被击杀次数
     */
    private int killedNum;


    @Autowired
    private ArenaManager arenaManager;

    public ArenaPlayerDo(Arena arena) {
        this.arenaId = arena.getArenaId();
        this.actorId = arena.getActorId();
        this.killNum = arena.getKillNum();
        this.killedNum = arena.getKilledNum();
        this.score = arena.getScore();
    }







    public void modifyScore(int score){
        this.score +=score;
        if( this.score < 0){
            this.score = 0;
        }
    }

    public Arena convertArena() {
        Arena arena = new Arena();
        arena.setArenaId(arenaId);
        arena.setActorId(actorId);
        arena.setKilledNum(killedNum);
        arena.setKillNum(killNum);
        arena.setScore(score);
        return arena;
    }

    public void increaseKillNum() {
        killNum++;
    }

    public void increaseKilledNum() {
        killedNum++;
    }
}
