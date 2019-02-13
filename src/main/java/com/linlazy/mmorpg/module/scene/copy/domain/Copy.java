package com.linlazy.mmorpg.module.scene.copy.domain;

import com.linlazy.mmorpg.event.type.CopyFailEvent;
import com.linlazy.mmorpg.file.config.SceneConfig;
import com.linlazy.mmorpg.file.service.SceneConfigService;
import com.linlazy.mmorpg.module.common.event.EventBusHolder;
import com.linlazy.mmorpg.module.common.reward.Reward;
import com.linlazy.mmorpg.module.player.domain.Player;
import com.linlazy.mmorpg.module.player.domain.PlayerTeamInfo;
import com.linlazy.mmorpg.module.scene.copy.push.CopyPushHelper;
import com.linlazy.mmorpg.module.scene.domain.Boss;
import com.linlazy.mmorpg.module.scene.domain.Scene;
import com.linlazy.mmorpg.module.scene.service.SceneService;
import com.linlazy.mmorpg.module.team.domain.Team;
import com.linlazy.mmorpg.server.threadpool.ScheduledThreadPool;
import com.linlazy.mmorpg.utils.SpringContextUtil;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 副本
 * @author linlazy
 */
@Data
public class Copy extends Scene {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 副本ID
     */
    private long copyId;

    /**
     * 当前bossID
     */
    private int currentBossIndex;

    /**
     * 奖励
     */
    List<Reward> rewardList;

    /**
     * 副本调度线程池
     */
    ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPool(20);
    ScheduledFuture<?> quitSchedule;

    /**
     * 初始化副本玩家信息
     * @return
     * @param team
     */
    public void initCopyPlayerInfo(Team team) {

        Map<Long, PlayerTeamInfo> playerTeamInfoMap = team.getPlayerTeamInfoMap();
        playerTeamInfoMap.values().stream()
                .forEach(playerTeamInfo -> {
                    playerMap.put(playerTeamInfo.getPlayer().getActorId(),playerTeamInfo.getPlayer());
                });
    }

    //=================================================================

    /***
     * 是否所有副本玩家都已死亡
     * @return
     */
    public boolean isAllActorDead(){
        return playerMap.values().stream()
                .allMatch(player ->player.getHp() == 0);
    }

    /***
     * 是否最终boss死亡
     * @return
     */
    public boolean isFinalBossDead(){
        return bossList.size()-1 == currentBossIndex;
    }


    /**
     * 启动退出副本调度
     */
    public void startQuitCopyScheduled() {
        SceneConfigService sceneConfigService = SpringContextUtil.getApplicationContext().getBean(SceneConfigService.class);

        SceneConfig sceneConfig = sceneConfigService.getSceneConfig(this.getSceneId());
        int times = sceneConfig.getOverTimeSeconds();
        //到达时间后挑战结束,退出副本触发事件
        quitSchedule = scheduledExecutorService.schedule(() -> {
            EventBusHolder.post(new CopyFailEvent(this));
            this.playerMap.values().stream()
                    .forEach(player -> {
                        CopyPushHelper.pushCopyFail(player.getActorId(),"超时，挑战失败");
                    });

            logger.debug("到达时间后，触发挑战失败事件");
        }, times, TimeUnit.SECONDS);
    }


    /**
     * 退出副本
     */
    public void quitQuit() {
        SceneConfigService sceneConfigService = SpringContextUtil.getApplicationContext().getBean(SceneConfigService.class);
        SceneService sceneService = SpringContextUtil.getApplicationContext().getBean(SceneService.class);
        SceneConfig sceneConfig = sceneConfigService.getSceneConfig(this.getSceneId());

        int targetSceneId = sceneConfig.getNeighborSet().get(0);

        //副本玩家移动到目标场景
        this.playerMap.values().stream()
                .filter(player -> player.getSceneId() == this.getSceneId())
                .forEach(player ->{
                    sceneService.moveTo(player.getActorId(),targetSceneId);
                });

        //取消调度，怪物自动刷新，小怪，BOSS定时攻击,超时退出副本
        bossList.get(currentBossIndex).quitSchedule();
        monsterMap.values().forEach(monster -> monster.quitSchedule());
        quitSchedule.cancel(true);
    }

    public void initCopyPlayerInfo(Player player) {
        playerMap.put(player.getActorId(),player);
    }




    public Boss nextBoss() {
        return bossList.get(++currentBossIndex);
    }

}
