package com.linlazy.mmorpg.entity;

import com.linlazy.mmorpg.server.db.annotation.Cloumn;
import com.linlazy.mmorpg.server.db.annotation.Table;
import com.linlazy.mmorpg.server.db.entity.Entity;
import com.linlazy.mmorpg.utils.DateUtils;
import lombok.Data;


/**
 * @author linlazy
 */
@Data
@Table("player")
public class PlayerEntity extends Entity {

    /**
     * 唯一标示
     */
    @Cloumn(pk = true)
    private long actorId;

    /**
     * 玩家所在场景
     */
    @Cloumn
    private int sceneId;

    /**
     * 玩家职业
     */
    @Cloumn
    private int profession;

    /**
     * 令牌
     */
    @Cloumn
    private String token;
    /**
     * 用户名
     */
    @Cloumn
    private String username;
    /**
     * 密码
     */
    @Cloumn
    private String password;

    /**
     * 金币
     */
    @Cloumn
    private long gold;

    /**
     * 等级
     */
    @Cloumn
    private int level;


    /**
     * 红
     */
    @Cloumn
    private int hp;

    /**
     * 血量上限
     */
    @Cloumn
    private int maxHp;

    /**
     * 红下一次自动回复时间
     */
    private long hpNextResumeTime;

    /**
     * 蓝
     */
    @Cloumn
    private int mp;
    /**
     * 蓝下一次自动回复时间
     */
    private long mpNextResumeTime = DateUtils.getNowMillis();

    public boolean resumeMP(long mpResumeIntervalMills) {
        // 未到回复时间
        if(DateUtils.getNowMillis() < mpNextResumeTime){
            return false;
        }

        //计算满足次数，每次回复一点
        int times = (int) ((DateUtils.getNowMillis() - mpNextResumeTime)/mpResumeIntervalMills +1);

        this.modifyMP(times);
        this.mpNextResumeTime += mpResumeIntervalMills * times;
        return true;
    }

    public int modifyMP(int mp) {
        synchronized (this){
            this.mp += mp;
            if(this.mp <0){
                this.mp = 0;
            }
            return this.mp;
        }
    }


}
