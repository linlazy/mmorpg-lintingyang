package com.linlazy.mmorpg.domain;

import com.linlazy.mmorpg.entity.ArenaEntity;
import lombok.Data;

/**
 * 竞技场
 * @author linlazy
 */
@Data
public class Arena {

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



    public void modifyScore(int score) {
        this.score +=score;
        if( this.score < 0){
            this.score = 0;
        }
    }

    public void increaseKillNum() {
        killNum++;
    }

    public void increaseKilledNum() {
        killedNum++;
    }

    public ArenaEntity convertArenaEntity() {
        return null;
    }
}
