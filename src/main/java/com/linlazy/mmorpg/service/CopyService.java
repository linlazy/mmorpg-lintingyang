package com.linlazy.mmorpg.service;

import com.google.common.eventbus.Subscribe;
import com.linlazy.mmorpg.domain.*;
import com.linlazy.mmorpg.event.type.*;
import com.linlazy.mmorpg.file.config.SceneConfig;
import com.linlazy.mmorpg.file.service.SceneConfigService;
import com.linlazy.mmorpg.module.common.event.EventBusHolder;
import com.linlazy.mmorpg.module.common.reward.Reward;
import com.linlazy.mmorpg.module.common.reward.RewardService;
import com.linlazy.mmorpg.module.team.service.TeamService;
import com.linlazy.mmorpg.push.CopyPushHelper;
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
    private MonsterService monsterService;
    @Autowired
    private SkillService skillService;
    @Autowired
    private RewardService rewardService;


    //===================================================================================================

    @PostConstruct
    public void init(){
        EventBusHolder.register(this);
    }


    @Subscribe
    public void copyRefreshMonster(CopyRefreshMonsterEvent copyRefreshMonsterEvent) {
        Copy copy = copyRefreshMonsterEvent.getCopy();

        copy.cancelMonsterAutoAttackSchedule();
        copy.clearMonster();
        //初始化副本小怪信息信息
        Map<Integer, Monster> monsterMap= monsterService.getMonsterBySceneId(copy.getSceneId());
        monsterMap.values().forEach(monster ->monster.setCopyId(copy.getCopyId()));
        copy.setMonsterMap(monsterMap);
        copy.getPlayerCopyInfoMap().values().forEach(playerCopyInfo ->
                CopyPushHelper.pushCopyMonsterRefresh(playerCopyInfo.getPlayer().getActorId(),"副本小怪刷新"));
        //开启小怪定时攻击调度
        copy.startMonsterAutoAttackScheduled();
    }

    @Subscribe
    public void enterCopy(CopyEnterEvent copyEnterEvent) {
        Player player =copyEnterEvent.getPlayer();
        //如果玩家不在副本中
        if(playerCopyIdMap.get(player.getActorId()) == null){
            //创建副本
            Copy copy = createCopy(player.getActorId());
            copy.setSceneId(player.getSceneId());
            //开启副本超时调度
            copy.startQuitCopyScheduled();
            //开启定时刷新小怪调度
            copy.startRefreshMonsterScheduled();
            //开启小怪定时攻击调度
            copy.startMonsterAutoAttackScheduled();
            //开启BOSS定时攻击调度
            copy.startBossAutoAttackScheduled();

        }

        player.setCopyId(playerCopyIdMap.get(player.getActorId()));
        CopyPushHelper.pushEnterSuccess(player.getActorId(),"进入副本成功");
    }

    /**
     * 副本失败，清理副本
     * @param copyFailEvent
     */
    @Subscribe
    public void copyFail(CopyFailEvent copyFailEvent){
        Copy copy = copyFailEvent.getCopy();
        copy.getPlayerCopyInfoMap().values()
                .stream()
                .forEach(playerCopyInfo -> playerCopyInfo.getPlayer().setHp(playerCopyInfo.getPlayer().getMaxHP()/2));
        clearCopy(copy);
    }



    /**
     * 副本玩家死亡
     * @param playerDeadEvent
     */
    @Subscribe
    public void copyPlayerDead(CopyPlayerDeadEvent playerDeadEvent){
        Player player = playerDeadEvent.getPlayer();

        Copy copy = getCopy(player.getCopyId());
        if(copy.isAllActorDead()){
            copy.getPlayerCopyInfoMap().values().stream()
                    .forEach(playerCopyInfo -> {
                        CopyPushHelper.pushCopyFail(playerCopyInfo.getPlayer().getActorId(),"挑战副本失败，全部玩家阵亡");
                    });
            copyFail(new CopyFailEvent(copy));
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


    /**
     * 创建副本
     * @return
     */
    public Copy createCopy(long actorId){
        Player player = playerService.getPlayer(actorId);

        Copy copy = new Copy();
        //初始化副本ID
        copy.setCopyId(maxCopyId.incrementAndGet());
        SceneConfig sceneConfig = sceneConfigService.getSceneConfig(player.getSceneId());
        copy.setSceneId(sceneConfig.getSceneId());
        copy.setSceneName(sceneConfig.getName());
        //初始化副本boss信息
        List<Boss> copyBoss = bossService.getBOSSBySceneId(player.getSceneId());
        copyBoss.forEach(
                boss -> boss.setCopyId(copy.getCopyId())
        );
        copy.setBossList(copyBoss);
        //初始化副本小怪信息信息
        Map<Integer, Monster> monsterMap= monsterService.getMonsterBySceneId(player.getSceneId());
        monsterMap.values().forEach(monster ->monster.setCopyId(copy.getCopyId()));
        copy.setMonsterMap(monsterMap);
        //初始化副本玩家信息


        if(player.isTeam()){
            Team team = teamService.getTeamByactorId(actorId);
            copy.initCopyPlayerInfo(team);
            team.getPlayerTeamInfoMap().values().forEach(playerTeamInfo -> {
                long actorId1 = playerTeamInfo.getPlayer().getActorId();
                playerCopyIdMap.put(actorId1,maxCopyId.get());
            });
        }else {
            copy.initCopyPlayerInfo(player);
            playerCopyIdMap.put(player.getActorId(),maxCopyId.get());
        }


        copyIdCopyMap.put(copy.getCopyId(),copy);
        return copy;
    }

}
