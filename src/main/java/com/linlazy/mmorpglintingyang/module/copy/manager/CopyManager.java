package com.linlazy.mmorpglintingyang.module.copy.manager;


import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.common.event.ActorEvent;
import com.linlazy.mmorpglintingyang.module.common.event.EventBusHolder;
import com.linlazy.mmorpglintingyang.module.common.event.EventType;
import com.linlazy.mmorpglintingyang.module.common.reward.Reward;
import com.linlazy.mmorpglintingyang.module.common.reward.RewardService;
import com.linlazy.mmorpglintingyang.module.copy.domain.CopyDo;
import com.linlazy.mmorpglintingyang.module.copy.domain.CopyPlayerDo;
import com.linlazy.mmorpglintingyang.module.scene.config.SceneConfigService;
import com.linlazy.mmorpglintingyang.module.scene.manager.SceneManager;
import com.linlazy.mmorpglintingyang.module.team.domain.TeamDo;
import com.linlazy.mmorpglintingyang.module.team.manager.TeamManager;
import com.linlazy.mmorpglintingyang.module.user.manager.UserManager;
import com.linlazy.mmorpglintingyang.module.user.manager.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class CopyManager {

    ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    @Autowired
    private SceneConfigService sceneConfigService;

    private ScheduledFuture<?> scheduledFuture;

    @Autowired
    private TeamManager teamManager;
    @Autowired
    private SceneManager sceneManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private RewardService rewardService;


    private static int maxCopyId = 0;

    private Map<Integer, CopyDo> copyIdCopyDoMap = new HashMap<>();
    private Map<Long,Integer> actorIdCopyIdMap = new HashMap<>();

    /**
     * 退出副本
     */
    public void quitCopy(int copyId){
        CopyDo copyDo = copyIdCopyDoMap.get(copyId);
        copyDo.quitCopy();
        copyIdCopyDoMap.remove(copyId);
    }


    /**
     * 创建副本
     * @param actorId
     */
    public CopyDo createCopy(long actorId,int sceneId) {
        maxCopyId = maxCopyId + 1;
        CopyDo copyDo = new CopyDo();
        //初始化副本ID
        copyDo.setCopyId(maxCopyId);
        //初始化副本玩家
        TeamDo actorTeamDo = teamManager.getActorTeamDo(actorId);
        Set<Long> teamIdSet = actorTeamDo.getTeamIdSet();
        Set<CopyPlayerDo> copyPlayerDoSet = teamIdSet.stream()
                .map(playerId -> {
                    User user = userManager.getUser(actorId);
                    actorIdCopyIdMap.put(actorId,copyDo.getCopyId());
                    return new CopyPlayerDo(user);
                }).collect(Collectors.toSet());
        copyDo.setCopyPlayerDoSet(copyPlayerDoSet);
        copyDo.setSceneId(sceneId);
        copyIdCopyDoMap.put(maxCopyId,copyDo);

        //启动调度
        return startQuitCopyScheduled(copyDo);
    }

    /**
     * 启动退出副本调度
     * @param copyDo
     */
    private CopyDo startQuitCopyScheduled(CopyDo copyDo) {

        JSONObject copyConfig = sceneConfigService.getCopyConfig(copyDo.getSceneId());
        int times = copyConfig.getIntValue("times");
        //到达时间后挑战结束,退出副本触发事件

        scheduledFuture = scheduledExecutorService.scheduleWithFixedDelay((Runnable) () -> {

            EventBusHolder.post(new ActorEvent<>(0, EventType.QUIT_COPY, copyDo.getCopyId()));

        }, 0L, times, TimeUnit.SECONDS);

        return copyDo;
    }

    public void copySuccess(int copyId) {
        CopyDo copyDo = copyIdCopyDoMap.get(copyId);
        //发放奖励
        List<Reward> rewardList = copyDo.getRewardList();
         copyDo.getCopyPlayerDoSet().stream()
         .forEach(copyPlayerDo -> {
             rewardService.addRewardList(copyPlayerDo.getActorId(),rewardList);
         });

         EventBusHolder.post(new ActorEvent<>(0,EventType.QUIT_COPY,copyId));
    }


    /**
     * 玩家不在副本中
     * @param actorId
     * @return
     */
    public boolean notCopyFor(long actorId) {
        return actorIdCopyIdMap.get(actorId) != null;
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

    public void quitCopy(Long actorId) {
        actorIdCopyIdMap.remove(actorId);
    }

    public CopyDo getCopyDo(int copyId) {
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
