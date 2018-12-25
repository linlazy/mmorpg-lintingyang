package com.linlazy.mmorpglintingyang.module.copy.domain;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.common.reward.Reward;
import com.linlazy.mmorpglintingyang.module.copy.manager.CopyManager;
import com.linlazy.mmorpglintingyang.module.scene.config.SceneConfigService;
import com.linlazy.mmorpglintingyang.module.scene.manager.SceneManager;
import com.linlazy.mmorpglintingyang.utils.RewardUtils;
import com.linlazy.mmorpglintingyang.utils.SpringContextUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Data
@Component
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
     * 副本玩家
     */
    private Set<CopyPlayerDo> copyPlayerDoSet = new HashSet<>();

    /**
     * 副本boss集合
     */
    private Set<CopyBossDo> copyBossDoSet = new HashSet<>();


    @Autowired
    private CopyManager copyManager;

    /**
     * 获取通过副本奖励
     * @return
     */
    public List<Reward> getRewardList(){
        SceneConfigService sceneConfigService = SpringContextUtil.getApplicationContext().getBean(SceneConfigService.class);
        JSONObject copyConfig = sceneConfigService.getCopyConfig(this.sceneId);
        String rewards = copyConfig.getString("rewards");
        return RewardUtils.parseRewards(rewards);
    }

    /**
     * 玩家全部死亡
     * @return
     */
    public boolean isAllActorDead(){
        return copyPlayerDoSet.stream()
                .allMatch(copyPlayerDo -> copyPlayerDo.getHp() == 0);
    }

    /**
     * BOSS全部死亡
     * @return
     */
    public boolean isAllBOSSDead(){
        return copyBossDoSet.stream()
                .allMatch(copyBossDo -> copyBossDo.getHp() == 0);
    }

    //创建副本
    // 启动定时退出副本调度

    //玩家攻击事件

    //boss 攻击事件


    // 玩家全部死亡
    // 挑战失败
    // 玩家退出副本 (取消定时退出副本调度 清除副本)

    // BOSS全部死亡
    //挑战成功
    //发放奖励
    // 玩家退出副本 (取消定时退出副本调度 清除副本)

    /**
     * 退出副本
     */
    public void quitCopy(){

        //获取退出后目标场景
        SceneConfigService sceneConfigService = SpringContextUtil.getApplicationContext().getBean(SceneConfigService.class);
        JSONObject copyConfig = sceneConfigService.getCopyConfig(this.sceneId);
        int targetSceneId = copyConfig.getJSONArray("sceneIds").getIntValue(0);

        //副本玩家移动到目标场景
        SceneManager sceneManager = SpringContextUtil.getApplicationContext().getBean(SceneManager.class);
        this.copyPlayerDoSet.stream()
                .map(CopyPlayerDo::getActorId)
                .forEach(actorId ->{
                    sceneManager.moveToScene(actorId,targetSceneId);
                    copyManager.quitCopy(actorId);
                });
    }
}
