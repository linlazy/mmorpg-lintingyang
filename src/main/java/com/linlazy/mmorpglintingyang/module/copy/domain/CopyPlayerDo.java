package com.linlazy.mmorpglintingyang.module.copy.domain;


import com.linlazy.mmorpglintingyang.module.common.event.ActorEvent;
import com.linlazy.mmorpglintingyang.module.common.event.EventBusHolder;
import com.linlazy.mmorpglintingyang.module.common.event.EventType;
import com.linlazy.mmorpglintingyang.module.copy.manager.CopyManager;
import com.linlazy.mmorpglintingyang.module.user.manager.entity.User;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 副本玩家
 */
@Data
public class CopyPlayerDo {

    /**
     * 玩家ID
     */
    private long actorId;

    /**
     * 当前血量
     */
    private int hp;

    /**
     * 玩家攻击力
     */
    private int attack;

    /**
     * 副本ID
     */
    private int copyId;

    @Autowired
    private CopyManager copyManager;

    public CopyPlayerDo(User user) {
        this.actorId = user.getActorId();
        this.hp = user.getHp();
    }

    public User convertUser(){
        User user = new User();
        user.setActorId(actorId);
        user.setHp(hp);
        return user;
    }

    /**
     * 玩家受到攻击
     * @param attack
     */
    public void attackedFromBoss(int attack) {
        //攻击逻辑
        if(this.hp == 0){
            EventBusHolder.post(new ActorEvent(actorId, EventType.COPY_ACTOR_DEAD));
        }
    }

    /**
     * 玩家攻击BOSS
     */
    public void attackBOSS(int bossId) {
        CopyDo copyDo = copyManager.getCopyDo(this.copyId);
        copyDo.getCopyBossDoSet().stream()
                .filter(bossDo -> bossDo.getBossId() == bossId)
                .findFirst().get().attacked(this.attack);
    }
}
