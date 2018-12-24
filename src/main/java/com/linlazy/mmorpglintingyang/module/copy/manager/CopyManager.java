package com.linlazy.mmorpglintingyang.module.copy.manager;


import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.common.event.ActorEvent;
import com.linlazy.mmorpglintingyang.module.common.event.EventBusHolder;
import com.linlazy.mmorpglintingyang.module.common.event.EventType;
import com.linlazy.mmorpglintingyang.module.common.reward.Reward;
import com.linlazy.mmorpglintingyang.module.common.reward.RewardService;
import com.linlazy.mmorpglintingyang.module.copy.domain.CopyDo;
import com.linlazy.mmorpglintingyang.module.scene.config.SceneConfigService;
import com.linlazy.mmorpglintingyang.module.scene.manager.SceneManager;
import com.linlazy.mmorpglintingyang.module.team.domain.TeamDo;
import com.linlazy.mmorpglintingyang.module.team.manager.TeamManager;
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

@Component
public class CopyManager {

    ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    @Autowired
    private SceneConfigService sceneConfigService;

    @Autowired
    private ScheduledFuture<?> scheduledFuture;

    @Autowired
    private TeamManager teamManager;
    @Autowired
    private SceneManager sceneManager;
    @Autowired
    private RewardService rewardService;


    private static int maxCopyId = 0;

    private Map<Integer, CopyDo> copyIdCopyDoMap = new HashMap<>();

    private Map<Long,Integer> actorIdCopyMap = new HashMap<>();

    /**
     * 退出副本
     */
    public void quitCopy(int copyId){
        CopyDo copyDo = copyIdCopyDoMap.get(copyId);
        JSONObject copyConfig = sceneConfigService.getCopyConfig(copyDo.getSceneId());
        int targetSceneId = copyConfig.getJSONArray("sceneIds").getIntValue(0);
        Set<Long> actorIdSet = copyDo.getActorIdSet();
        actorIdSet.stream()
                .forEach(actorId -> {
                    sceneManager.moveToScene(actorId,targetSceneId);
                    actorIdCopyMap.remove(actorId);
                });
    }


    /**
     * 初始化副本
     * @param actorId
     */
    public CopyDo initCopy(long actorId,int sceneId) {
        if(actorIdCopyMap.get(actorId) == null){
            maxCopyId = maxCopyId + 1;
            CopyDo copyDo = new CopyDo();
            TeamDo actorTeamDo = teamManager.getActorTeamDo(actorId);
            Set<Long> teamIdSet = actorTeamDo.getTeamIdSet();
            copyDo.setActorIdSet(teamIdSet);
            copyDo.setCopyId(maxCopyId);
            copyDo.setSceneId(sceneId);

            copyIdCopyDoMap.put(maxCopyId,copyDo);

            actorIdCopyMap.put(actorId,maxCopyId);
            teamIdSet.stream().forEach(targetId ->{
                actorIdCopyMap.put(targetId,maxCopyId);
            });
        }

        Integer copyId = actorIdCopyMap.get(actorId);
        return copyIdCopyDoMap.get(copyId);
    }

    /**
     * 启动退出副本调度
     * @param copyDo
     */
    public void startQuitCopyScheduled(CopyDo copyDo) {

        JSONObject copyConfig = sceneConfigService.getCopyConfig(copyDo.getSceneId());
        int times = copyConfig.getIntValue("times");
        //到达时间后挑战结束,退出副本触发事件

        scheduledFuture = scheduledExecutorService.scheduleWithFixedDelay((Runnable) () -> {

            EventBusHolder.post(new ActorEvent<>(0, EventType.QUIT_COPY, copyDo.getCopyId()));

        }, 0L, times, TimeUnit.SECONDS);

    }

    public void copySuccess(int copyId) {
        CopyDo copyDo = copyIdCopyDoMap.get(copyId);
        //发放奖励
        List<Reward> rewardList = copyDo.getRewardList();
        Set<Long> actorIdSet = copyDo.getActorIdSet();
        for(long actorId:actorIdSet){
            rewardService.addRewardList(actorId,rewardList);
        }
        quitCopy(copyId);
        scheduledFuture.cancel(true);
    }
}
