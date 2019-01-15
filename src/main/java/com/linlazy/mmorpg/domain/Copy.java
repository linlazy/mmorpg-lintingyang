package com.linlazy.mmorpg.domain;

import com.linlazy.mmorpg.event.type.CopyFailEvent;
import com.linlazy.mmorpg.event.type.CopyRefreshMonsterEvent;
import com.linlazy.mmorpg.file.config.SceneConfig;
import com.linlazy.mmorpg.file.service.SceneConfigService;
import com.linlazy.mmorpg.module.common.event.EventBusHolder;
import com.linlazy.mmorpg.module.common.reward.Reward;
import com.linlazy.mmorpg.service.SceneService;
import com.linlazy.mmorpg.service.SkillService;
import com.linlazy.mmorpg.utils.SpringContextUtil;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 副本
 * @author linlazy
 */
@Data
public class Copy extends Scene{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 副本ID
     */
    private long copyId;

    /**
     * 当前bossID
     */
    private int currentBossId;

    /**
     * 副本Boss信息
     */
    private List<Boss> copyBoss= new CopyOnWriteArrayList<>();


    /**
     * 副本小怪信息
     */
    private Map<Long,Monster> copyMonsterInfoMap = new ConcurrentHashMap<>();

    /**
     * 副本玩家信息
     */
    private Map<Long,PlayerCopyInfo> playerCopyInfoMap = new ConcurrentHashMap<>();

    /**
     * 小怪自动攻击句柄
     */
    private static Map<Long, ScheduledFuture> monsterIdAutoAttackScheduleMap = new ConcurrentHashMap<>();

    /**
     * boss自动攻击句柄
     */
    private static Map<Long, ScheduledFuture> bossIdAutoAttackScheduleMap = new ConcurrentHashMap<>();

    /**
     * 副本自动小怪刷新调度
     */
    private static Map<Long, ScheduledFuture> copyIdMonsterRefreshScheduleMap = new ConcurrentHashMap<>();

    /**
     * 副本调度线程池
     */
    ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(20);


    /**
     * 初始化副本玩家信息
     * @return
     * @param team
     */
    public void initCopyPlayerInfo(Team team) {

        Map<Long, PlayerTeamInfo> playerTeamInfoMap = team.getPlayerTeamInfoMap();
        playerTeamInfoMap.values().stream()
                .forEach(playerTeamInfo -> {
                    playerCopyInfoMap.put(playerTeamInfo.getPlayer().getActorId(),new PlayerCopyInfo(copyId,playerTeamInfo.getPlayer()));
                });
    }

    //=================================================================

    /***
     * 是否所有副本玩家都已死亡
     * @return
     */
    public boolean isAllActorDead(){
        return playerCopyInfoMap.values().stream()
                .allMatch(playerCopyInfo -> playerCopyInfo.getPlayer().getHp() == 0);
    }

    /***
     * 是否最终boss死亡
     * @return
     */
    public boolean isFinalBossDead(){
        return copyBoss.get(copyBoss.size()-1).getId() == currentBossId;
    }

    public List<Reward> getRewardList() {
        return null;
    }

    /**
     * 怪物定时攻击调度
     */
    public void startMonsterAutoAttackScheduled() {

        //获取怪物
        Collection<Monster> monsters =copyMonsterInfoMap.values();

        //随机选择怪物攻击技能
        SkillService skillService = SpringContextUtil.getApplicationContext().getBean(SkillService.class);
        monsters.stream()
                .forEach(monster -> {

                    ScheduledFuture<?> scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(() -> {
                        Skill skill = monster.getMonsterSkillInfo().randomSkill();

                        skillService.attack(monster,skill);
                    }, 0L, 5L, TimeUnit.SECONDS);
                    monsterIdAutoAttackScheduleMap.put(copyId,scheduledFuture);
                });
    }

    /**
     * 小怪定时刷新调度
     */
    public void startRefreshMonsterScheduled() {
        //到达时间后挑战结束,退出副本触发事件
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            EventBusHolder.post(new CopyRefreshMonsterEvent(this));
            logger.debug("到达时间后，触发小怪刷新事件");
        }, 0L,300, TimeUnit.SECONDS);
    }

    /**
     * 启动退出副本调度
     */
    public void startQuitCopyScheduled() {
        SceneConfigService sceneConfigService = SpringContextUtil.getApplicationContext().getBean(SceneConfigService.class);

        SceneConfig sceneConfig = sceneConfigService.getSceneConfig(this.getSceneId());
        int times = sceneConfig.getOverTimeSeconds();
        //到达时间后挑战结束,退出副本触发事件
        ScheduledFuture<?> schedule = scheduledExecutorService.schedule(() -> {
            EventBusHolder.post(new CopyFailEvent(this));
            logger.debug("到达时间后，触发挑战失败事件");
        }, times, TimeUnit.SECONDS);
    }

    /**
     * BOSS定时攻击调度
     */
    public void startBossAutoAttackScheduled() {
        //获取BOSS
        Boss boss = this.getCopyBoss().get(this.getCurrentBossId());

        SkillService skillService = SpringContextUtil.getApplicationContext().getBean(SkillService.class);
        ScheduledFuture<?> scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(() -> {
            //随机选择boss技能攻击
            Skill skill = boss.getBossSkillInfo().randomSkill();
            skillService.attack(boss,skill);
        }, 0L, 5L, TimeUnit.SECONDS);

        bossIdAutoAttackScheduleMap.put(boss.getId(),scheduledFuture);
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
        this.getPlayerCopyInfoMap().values().stream()
                .filter(playerCopyInfo -> playerCopyInfo.getPlayer().getSceneId() == this.getSceneId())
                .forEach(playerCopyInfo ->{
                    sceneService.moveToScene(playerCopyInfo.getPlayer().getActorId(),targetSceneId);
                });

        //取消调度，怪物自动刷新，小怪，BOSS定时攻击
        bossIdAutoAttackScheduleMap.values()
                .forEach(scheduledFuture -> scheduledFuture.cancel(true));
        monsterIdAutoAttackScheduleMap.values()
                .forEach(scheduledFuture -> scheduledFuture.cancel(true));
        copyIdMonsterRefreshScheduleMap.values()
                .forEach(scheduledFuture -> scheduledFuture.cancel(true));

    }
}
