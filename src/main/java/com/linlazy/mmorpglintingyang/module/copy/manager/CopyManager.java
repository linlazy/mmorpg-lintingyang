package com.linlazy.mmorpglintingyang.module.copy.manager;


import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.common.event.ActorEvent;
import com.linlazy.mmorpglintingyang.module.common.event.EventBusHolder;
import com.linlazy.mmorpglintingyang.module.common.event.EventType;
import com.linlazy.mmorpglintingyang.module.common.reward.Reward;
import com.linlazy.mmorpglintingyang.module.common.reward.RewardService;
import com.linlazy.mmorpglintingyang.module.config.BossConfigService;
import com.linlazy.mmorpglintingyang.module.copy.domain.CopyDo;
import com.linlazy.mmorpglintingyang.module.scene.config.SceneConfigService;
import com.linlazy.mmorpglintingyang.module.scene.constants.SceneEntityType;
import com.linlazy.mmorpglintingyang.module.scene.domain.SceneBossDo;
import com.linlazy.mmorpglintingyang.module.scene.domain.SceneEntityDo;
import com.linlazy.mmorpglintingyang.module.scene.domain.ScenePlayerDo;
import com.linlazy.mmorpglintingyang.module.scene.manager.SceneManager;
import com.linlazy.mmorpglintingyang.module.team.domain.TeamDo;
import com.linlazy.mmorpglintingyang.module.team.manager.TeamManager;
import com.linlazy.mmorpglintingyang.module.user.manager.UserManager;
import com.linlazy.mmorpglintingyang.module.user.manager.entity.User;
import com.linlazy.mmorpglintingyang.utils.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class CopyManager {

    ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(20);

    @Autowired
    private SceneConfigService sceneConfigService;

    @Autowired
    private BossConfigService bossConfigService;

    @Autowired
    private TeamManager teamManager;
    @Autowired
    private SceneManager sceneManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private RewardService rewardService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static AtomicInteger maxCopyId = new AtomicInteger(0);

    private Map<Integer, CopyDo> copyIdCopyDoMap = new ConcurrentHashMap<>();
    private Map<Long,Integer> actorIdCopyIdMap = new ConcurrentHashMap<>();

    private Map<Integer,ScheduledFuture> copyIdScheduleMap = new HashMap<>();

    /**
     * 退出副本
     */
    public void quitCopy(int copyId){
        ScheduledFuture scheduledFuture = copyIdScheduleMap.remove(copyId);
        scheduledFuture.cancel(true);
        CopyDo copyDo = copyIdCopyDoMap.get(copyId);
        copyDo.quitCopy();
        copyIdCopyDoMap.remove(copyId);
    }


    /**
     * 创建副本
     * @param actorId
     */
    public CopyDo createCopy(long actorId,int sceneId) {
        int  newCopyId = maxCopyId.incrementAndGet();
        CopyDo copyDo = new CopyDo();
        //初始化副本ID
        copyDo.setCopyId(newCopyId);
        //初始化副本玩家
        TeamDo actorTeamDo = teamManager.getActorTeamDo(actorId);
        Set<Long> teamIdSet = actorTeamDo.getTeamIdSet();
        copyDo.setCopyPlayerIdSet(teamIdSet);
        copyDo.setSceneId(sceneId);

        //初始化副本怪物
        Set<SceneEntityDo> sceneEntityDoSet = new HashSet<>();

        List<JSONObject> bossConfigs = bossConfigService.getBossBySceneId(sceneId);
        for(int i = 0 ; i < bossConfigs.size(); i ++){
            JSONObject bossConfig = bossConfigs.get(i);

            SceneBossDo bossDo = new SceneBossDo();
            bossDo.setSceneId(sceneId);
            bossDo.setBossId(bossConfig.getIntValue("bossId"));
            bossDo.setName(bossConfig.getString("name"));
            bossDo.setHp(bossConfig.getIntValue("hp"));
            bossDo.setCopyId(newCopyId);

            sceneEntityDoSet.add(new SceneEntityDo(bossDo));
        }
        copyDo.setSceneEntitySet(sceneEntityDoSet);

        copyIdCopyDoMap.put(newCopyId,copyDo);
        Set<Long> copyPlayerIdSet = copyDo.getCopyPlayerIdSet();
        copyPlayerIdSet.stream()
                .forEach(teamId ->{
                    actorIdCopyIdMap.put(teamId,newCopyId);
                });

        //启动Boss自动攻击调度
        startBossAutoAttackScheduled(copyDo);
        //启动调度`
        return startQuitCopyScheduled(copyDo);
    }

    private void startBossAutoAttackScheduled(CopyDo copyDo) {
        ScheduledFuture<?> scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(() -> {
            //随机选择一位在场景中的玩家
            Set<Long> copyPlayerIdSet = copyDo.getCopyPlayerIdSet();
            Long targetId = RandomUtils.randomElement(copyPlayerIdSet);
            User user = userManager.getUser(targetId);
            SceneEntityDo sceneEntityDo = new SceneEntityDo(new ScenePlayerDo(user));
            sceneEntityDo.setCopyId(copyDo.getCopyId());
            // boss attack entityId entityType
            copyDo.getSceneEntitySet().stream()
                    .filter(sceneEntityDo1 -> sceneEntityDo1.getSceneEntityType() == SceneEntityType.Boss)
                    .forEach(
                            sceneEntityDo1 -> {
                                JSONObject bossConfig = bossConfigService.getBossConfig((int) sceneEntityDo1.getSceneEntityId());
                                int attack = bossConfig.getIntValue("attack");
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("entityId", sceneEntityDo.getSceneEntityId());
                                jsonObject.put("entityType", sceneEntityDo.getSceneEntityType());
                                sceneEntityDo.attacked(attack, jsonObject);
                            }
                    );
        }, 0L, 5L, TimeUnit.SECONDS);

        copyIdScheduleMap.put(copyDo.getCopyId(),scheduledFuture);

    }

    /**
     * 启动退出副本调度
     * @param copyDo
     */
    private CopyDo startQuitCopyScheduled(CopyDo copyDo) {

        JSONObject copyConfig = sceneConfigService.getCopyConfig(copyDo.getSceneId());
        int times = copyConfig.getIntValue("times");
        //到达时间后挑战结束,退出副本触发事件
         scheduledExecutorService.schedule(() -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("copyId",copyDo.getCopyId());
            EventBusHolder.post(new ActorEvent<>(0, EventType.QUIT_COPY,jsonObject));
             logger.debug("到达时间后挑战结束,退出副本触发事件");
        }, times, TimeUnit.SECONDS);

        return copyDo;
    }

    public void copySuccess(int copyId) {
        CopyDo copyDo = copyIdCopyDoMap.get(copyId);
        //发放奖励
        List<Reward> rewardList = copyDo.getRewardList();
         copyDo.getCopyPlayerIdSet().stream()
         .forEach(actorId -> {
             rewardService.addRewardList(actorId,rewardList);
         });

         EventBusHolder.post(new ActorEvent<>(0,EventType.QUIT_COPY,copyId));
    }


    /**
     * 玩家不在副本中
     * @param actorId
     * @return
     */
    public boolean notCopyFor(long actorId) {
        return actorIdCopyIdMap.get(actorId) == null;
    }

    /**
     * 处理副本玩家死亡事件
     * @param copyId
     */
    public void handleCopyActorDead(int copyId) {
        CopyDo copyDo = copyIdCopyDoMap.get(copyId);
        //所有玩家死亡，挑战副本失败
        if(copyDo.isAllActorDead()){
            EventBusHolder.post(new ActorEvent(0,EventType.COPY_FAIL));
        }
    }

    /**
     * 玩家退出副本
     * @param actorId
     */
    public void quitCopy(long actorId) {
        int copyId = actorIdCopyIdMap.get(actorId);
        CopyDo copyDo = getCopyDo(copyId);
        copyDo.getCopyPlayerIdSet().remove(actorId);
        actorIdCopyIdMap.remove(actorId);
    }

    public CopyDo getCopyDo(int copyId) {
        return copyIdCopyDoMap.get(copyId);
    }

    public CopyDo getCopyDo(long actorId,int sceneId) {
        if(this.notCopyFor(actorId)){
            return createCopy(actorId,sceneId);
        }
        int copyId = actorIdCopyIdMap.get(actorId);
        return copyIdCopyDoMap.get(copyId);
    }

    public void handleCopyBOSSDead(int copyId) {
        CopyDo copyDo = copyIdCopyDoMap.get(copyId);
        //所有BOSS，挑战副本成功
        if(copyDo.isAllBOSSDead()){
            EventBusHolder.post(new ActorEvent(0,EventType.COPY_SUCCESS));
        }
    }

    public void copyFail(int copyId) {
        EventBusHolder.post(new ActorEvent<>(0,EventType.QUIT_COPY,copyId));
    }
}
