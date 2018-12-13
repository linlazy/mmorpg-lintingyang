package com.linlazy.mmorpglintingyang.module.user.manager.entity;

import com.linlazy.mmorpglintingyang.utils.DateUtils;
import lombok.Data;

@Data
public class User {

    /**
     * 唯一标示
     */
    private long actorId;

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
     * 状态
     */
    private int status;
    /**
     * 红
     */
    private int hp;
    /**
     * 红下一次自动回复时间
     */
    private long HPNextResumeTime;

    /**
     * 蓝
     */
    private int mp;
    /**
     * 蓝下一次自动回复时间
     */
    private long MPNextResumeTime = DateUtils.getNowMillis();

    public boolean resumeMP(long mpResumeIntervalMills) {
        // 未到回复时间
        if(DateUtils.getNowMillis() < MPNextResumeTime){
            return false;
        }

        //计算满足次数，每次回复一点
        int times = (int) ((DateUtils.getNowMillis() -MPNextResumeTime)/mpResumeIntervalMills +1);

        this.modifyMP(times);
        this.MPNextResumeTime += mpResumeIntervalMills * times;
        return true;
    }

    private void modifyMP(int mp) {
        synchronized (this){
            this.mp += mp;
            if(this.mp <0){
                this.mp = 0;
            }
        }
    }
}
