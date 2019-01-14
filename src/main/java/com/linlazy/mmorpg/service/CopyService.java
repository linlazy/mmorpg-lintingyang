package com.linlazy.mmorpg.service;

import com.google.common.eventbus.Subscribe;
import com.linlazy.mmorpg.domain.*;
import com.linlazy.mmorpg.event.type.CopyBossDeadEvent;
import com.linlazy.mmorpg.event.type.CopyFailEvent;
import com.linlazy.mmorpg.event.type.CopyPlayerDeadEvent;
import com.linlazy.mmorpg.event.type.CopySuccessEvent;
import com.linlazy.mmorpg.module.common.event.EventBusHolder;
import com.linlazy.mmorpg.module.common.reward.Reward;
import com.linlazy.mmorpg.module.common.reward.RewardService;
import com.linlazy.mmorpg.file.service.SceneConfigService;
import com.linlazy.mmorpg.module.team.service.TeamService;
import com.linlazy.mmorpg.server.common.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 副本服务类
 * @author linlazy
 */
@Component
public class CopyService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static Map<Long, Copy> copyIdCopyMap = new ConcurrentHashMap<>();

    /**
     * 玩家副本信息
     */
    private Map<Long,Long> playerCopyIdMap=  new ConcurrentHashMap<>();

    /**
     * 当前最大副本编号
     */
    private AtomicLong maxCopyId = new AtomicLong();

    public Copy getCopyByActorId(long actorId){
        Long copyId = playerCopyIdMap.get(actorId);
        return copyIdCopyMap.get(copyId);
    }

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
    @Autowired
    private SkillService skillService;
    @Autowired
    private RewardService rewardService;


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
        clearCopy(copy);
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

    /**
     * 副本BOSS死亡
     * @param copyBossDeadEvent
     */
    @Subscribe
    public void copyBossDead(CopyBossDeadEvent copyBossDeadEvent){
        Copy copy = copyBossDeadEvent.getCopy();
        if(copy.isFinalBossDead()){
            EventBusHolder.post(new CopySuccessEvent(copy));
        }
    }

    /**
     * 副本挑战成功
     * @param copySuccessEvent
     */
    @Subscribe
    public void copySuccess(CopySuccessEvent copySuccessEvent) {
        Copy copy = copySuccessEvent.getCopy();
        //发放奖励
        List<Reward> rewardList = copy.getRewardList();
        copy.getPlayerCopyInfoMap().values().stream()
                .forEach(playerCopyInfo -> {
                    rewardService.addRewardList(playerCopyInfo.getPlayer().getActorId(),rewardList);
                });

        clearCopy(copy);
    }

    /**
     * 清理副本
     * @param copy
     */
    private void clearCopy(Copy copy) {
        //取消副本调度
        copy.quitQuit();
        //退出副本
        copyIdCopyMap.remove(copy.getCopyId());
        copy.getPlayerCopyInfoMap().values().stream()
                .forEach(playerCopyInfo -> {
                    playerCopyIdMap.remove(playerCopyInfo.getPlayer().getActorId());
                });

    }

    //=======================================================================

    public Result<?> enterCopy(long actorId) {
        Player player = playerService.getPlayer(actorId);
        //如果玩家不在副本中
        if(playerCopyIdMap.get(actorId) == null){
            //创建副本
            Copy copy = createCopy(actorId);
            //开启副本超时调度
            copy.startQuitCopyScheduled();
            //开启定时刷新小怪调度
            copy.startRefreshMonsterScheduled();
            //开启小怪定时攻击调度
            copy.startMonsterAutoAttackScheduled();
            //开启BOSS定时攻击调度
            copy.startBossAutoAttackScheduled();

        }
        return Result.success(String.format("进入副本成功"));
    }

    /**
     * 创建副本
     * @return
     */
    public Copy createCopy(long actorId){
        Player player = playerService.getPlayer(actorId);

        Copy copy = new Copy();
        //初始化副本ID
        copy.setCopyId(maxCopyId.incrementAndGet());
        //初始化副本boss信息
        List<Boss> copyBoss = bossService.createCopyBoss(player.getSceneId());
        copy.setCopyBoss(copyBoss);
        //初始化副本玩家信息
        Team team = teamService.getTeam(actorId);
        copy.initCopyPlayerInfo(team);

        team.getPlayerTeamInfoMap().values().forEach(playerTeamInfo -> {
            long actorId1 = playerTeamInfo.getPlayer().getActorId();
            playerCopyIdMap.put(actorId1,maxCopyId.get());
        });



        return copy;
    }

}
