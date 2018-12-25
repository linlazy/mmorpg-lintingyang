package com.linlazy.mmorpglintingyang.module.pk.arena.domain;

import com.linlazy.mmorpglintingyang.module.common.event.ActorEvent;
import com.linlazy.mmorpglintingyang.module.common.event.EventBusHolder;
import com.linlazy.mmorpglintingyang.module.common.event.EventType;
import com.linlazy.mmorpglintingyang.module.pk.arena.entity.Arena;
import com.linlazy.mmorpglintingyang.module.pk.arena.manager.ArenaManager;
import com.linlazy.mmorpglintingyang.module.user.manager.entity.User;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 竞技场玩家
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
     * 玩家血量
     */
    private int hp;

    /**
     * 击杀人数
     */
    private int killNum;

    /**
     * 被击杀次数
     */
    private int killedNum;

    /**
     * 攻击力
     */
    private int attack;

    /**
     * 防御力
     */
    private int defense;

    @Autowired
    private ArenaManager arenaManager;

    public ArenaPlayerDo(User user, Arena arena) {
        this.arenaId = arena.getArenaId();
        this.actorId = arena.getActorId();
        this.hp = user.getHp();
        this.killNum = arena.getKillNum();
        this.killedNum = arena.getKilledNum();

        int levelAttack = user.getLevel() * 20;
        int equipAttack = 0;
        int skillAttack = 0;
        this.attack = levelAttack + equipAttack + skillAttack ;
    }


    /**
     * 受到玩家攻击
     * @param killId
     * @param attack
     */
    public void attackedFrom(long killId, int attack){

        int deceaseHp = attack -this.defense;
        if(deceaseHp < 0){
            deceaseHp = 0;
        }
        this.modifyHp(-deceaseHp);
        if(hp == 0){
            killedNum++;
            EventBusHolder.post(new ActorEvent(actorId,EventType.ARENA_ACTOR_KILLED,killId));
        }
    }


    /**
     * 攻击玩家
     * @param killedId
     */
    public void attack(long killedId){
        ArenaDo arenaDo = arenaManager.getArenaDo(arenaId);
        ArenaPlayerDo killedPlayerDo = arenaDo.getArenaPlayerDoSet().stream()
                .filter(arenaPlayerDo -> arenaPlayerDo.getActorId() == killedId)
                .findFirst().get();
        killedPlayerDo.attackedFrom(this.actorId,attack);
    }

    public void modifyScore(int score){
        this.score +=score;
        if( this.score < 0){
            this.score = 0;
        }
    }


    private void modifyHp(int hp){
        this.hp +=hp;
        if(this.hp < 0){
            this.hp = 0;
        }
    }
}
