package com.linlazy.mmorpg.module.copy.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.eventbus.Subscribe;
import com.linlazy.mmorpg.domain.*;
import com.linlazy.mmorpg.event.type.CopyFailEvent;
import com.linlazy.mmorpg.event.type.CopyPlayerDeadEvent;
import com.linlazy.mmorpg.module.common.event.ActorEvent;
import com.linlazy.mmorpg.module.common.event.EventBusHolder;
import com.linlazy.mmorpg.module.copy.manager.CopyManager;
import com.linlazy.mmorpg.module.scene.config.SceneConfigService;
import com.linlazy.mmorpg.module.team.service.TeamService;
import com.linlazy.mmorpg.server.common.GlobalConfigService;
import com.linlazy.mmorpg.server.common.Result;
import com.linlazy.mmorpg.service.BossService;
import com.linlazy.mmorpg.service.PlayerService;
import com.linlazy.mmorpg.service.SceneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author linlazy
 */
@Component
public class CopyService {

    private static Map<Long, Copy> copyIdCopyMap = new ConcurrentHashMap<>();

    private Map<Integer, ScheduledFuture> copyIdScheduleMap = new ConcurrentHashMap<>();

    /**
     * 当前最大副本编号
     */
    private AtomicLong maxCopyId = new AtomicLong();

    public Copy getCopy(long copyId){
        return copyIdCopyMap.get(copyId);
    }

    //=======================================================================================================================

    @Autowired
    private SceneConfigService sceneConfigService;
    @Autowired
    private SceneService sceneService;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private BossService bossService;


    //===================================================================================================

    @PostConstruct
    public void init(){
        EventBusHolder.register(this);
    }

    /**
     * 副本失败，清理副本
     * @param copyFailEvent
     */
    @Subscribe
    public void copyFail(CopyFailEvent copyFailEvent){
        Copy copy = copyFailEvent.getCopy();
        //取消调度
        ScheduledFuture scheduledFuture = copyIdScheduleMap.remove(copy.getCopyId());
        scheduledFuture.cancel(true);


        JSONObject copyConfig = sceneConfigService.getCopyConfig(copy.getSceneId());
        int targetSceneId = copyConfig.getJSONArray("neighborSet").getIntValue(0);

        //副本玩家移动到目标场景
        copy.getCopyPlayerInfo().getPlayerSet().stream()
                .filter(player -> player.getPlayerSceneInfo().getSceneId() == copy.getSceneId())
                .forEach(player ->{
                    sceneService.moveToScene(player.getActorId(),targetSceneId);
                });
        //退出副本
        copyIdCopyMap.remove(copy.getCopyId());
    }

    /**
     * 副本玩家死亡
     * @param playerDeadEvent
     */
    @Subscribe
    public void copyPlayerDead(CopyPlayerDeadEvent playerDeadEvent){
        PlayerCopyInfo playerCopyInfo = playerDeadEvent.getPlayer().getPlayerCopyInfo();

        Copy copy = getCopy(playerCopyInfo.getCopyId());
        if(copy.isAllActorDead()){
            EventBusHolder.post(new CopyFailEvent(copy));
        }
    }



   // =================================================================================


    @Autowired
    private GlobalConfigService globalConfigService;

    @Autowired
    private CopyManager copyManager;




    @Subscribe
    public void listenEvent(ActorEvent actorEvent){

        switch (actorEvent.getEventType()){

            case COPY_BOSS_DEAD:
                //副本BOSS死亡
                handleCopyBOSSDead(actorEvent);
                break;
            case QUIT_COPY:
                //退出副本
                handleQuitCopy(actorEvent);
                break;
            case COPY_SUCCESS:
                //挑战成功
                handleCopySuccess(actorEvent);
                break;
            case COPY_FAIL:
                //挑战失败
                handleCopyFail(actorEvent);
                break;
            default:
                break;
        }

    }

    private void handleCopyBOSSDead(ActorEvent actorEvent) {
        JSONObject jsonObject = (JSONObject)actorEvent.getData();
        int copyId = jsonObject.getIntValue("copyId");
        copyManager.handleCopyBOSSDead(copyId);
    }

    private void handleCopyFail(ActorEvent actorEvent) {
        JSONObject jsonObject = (JSONObject)actorEvent.getData();
        int copyId = jsonObject.getIntValue("copyId");
        copyManager.copyFail(copyId);
    }

    private void handleCopySuccess(ActorEvent actorEvent) {
        JSONObject jsonObject = (JSONObject)actorEvent.getData();
        int copyId = jsonObject.getIntValue("copyId");
        copyManager.copySuccess(copyId);
    }

    /**
     * 处理退出副本事件
     * @param actorEvent
     */
    private void handleQuitCopy(ActorEvent actorEvent) {
        JSONObject jsonObject = (JSONObject)actorEvent.getData();
        int copyId = jsonObject.getIntValue("copyId");
        copyManager.quitCopy(copyId);
    }

    //=======================================================================

    public Result<?> enterCopy(long actorId) {
        return null;
    }

    /**
     * 创建副本
     * @return
     */
    public Copy createCopy(long actorId){
        PlayerSceneInfo playerSceneInfo = playerService.getPlayer(actorId).getPlayerSceneInfo();


        Copy copy = new Copy();
        //初始化副本ID
        copy.setCopyId(maxCopyId.incrementAndGet());
        //初始化副本boss信息
        List<Boss> copyBoss = bossService.createCopyBoss(playerSceneInfo.getSceneId());
        copy.initCopyBossInfo(copyBoss);
        //初始化副本玩家信息
        Team team = teamService.getTeam(actorId);
        copy.initCopyPlayerInfo(team);

        return copy;
    }
}
