package com.linlazy.mmorpglintingyang.module.copy.domain;

import com.linlazy.mmorpglintingyang.module.common.reward.Reward;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Data
public class CopyDo {

    /**
     * 副本ID标识
     */
    private int copyId;


    /**
     * 场景ID
     */
    private int sceneId;

    /**
     * 是否结束
     */
    private boolean isEND;


    /**
     * 副本玩家
     */
    private Set<Long> actorIdSet = new HashSet<>();


    /**
     * 获取通过副本奖励
     * @return
     */
    public List<Reward> getRewardList(){
        return null;
    }

    //进入副本
    // 启动定时退出副本调度

    //玩家攻击事件
    //boss 扣除hp 死亡 ，挑战成功

    //boss 攻击事件
    // 玩家全部死亡 挑战失败，取消定时退出副本调度，清除副本
    //时间到，挑战失败，触发退出副本调度，清除副本

    //挑战成功
    //发放奖励
    // 玩家退出副本
    // 取消定时退出副本调度
    // 清除副本

}
