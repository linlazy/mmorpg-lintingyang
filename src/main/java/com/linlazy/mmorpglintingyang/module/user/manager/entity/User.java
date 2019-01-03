package com.linlazy.mmorpglintingyang.module.user.manager.entity;

import com.linlazy.mmorpglintingyang.utils.DateUtils;
import lombok.Data;

/**
 * @author linlazy
 */
@Data
public class User {

    /**
     * 唯一标示
     */
    private long actorId;

    /**
     * 玩家所在场景
     */
    private int sceneId;

    /**
     * 令牌
     */
    private String token;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;

    /**
     * 金币
     */
    private long gold;

    /**
     * 等级
     */
    private int level;

    /**
     * 状态
     */
    private int status;

    /**
     * 物理攻击下限
     */
    private long physicalAttackDown;
    /**
     * 物理攻击上限
     */
    private long physicalAttackUp;

    /**
     * 魔法攻击下限
     */
    private long magicAttackDown;
    /**
     * 魔法攻击上限
     */
    private long magicAttackUp;

    /**
     * 红
     */
    private int hp;

    public synchronized int getHp() {
        return hp;
    }

    public synchronized void setHp(int hp) {
        this.hp = hp;
    }

    /**
     * 红下一次自动回复时间
     */
    private long hpNextResumeTime;

    /**
     * 蓝
     */
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

    public int modifyHP(int hp) {
        synchronized (this){
            this.hp += hp;
            if(this.hp < 0){
                this.hp = 0;
            }
            return this.hp;
        }
    }

    public void modifyPhysicalAttack(int count) {

    }

    public void modifyMagicAttack(int count) {

    }

    public void modifyPhysicalDefense(int count) {

    }

    public void modifyMagicDefense(int count) {

    }

    public int modifyGold(int count) {
        return 0;
    }
}
