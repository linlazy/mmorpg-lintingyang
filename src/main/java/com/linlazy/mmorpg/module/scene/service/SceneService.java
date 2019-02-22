package com.linlazy.mmorpg.module.scene.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.eventbus.Subscribe;
import com.linlazy.mmorpg.event.type.*;
import com.linlazy.mmorpg.file.config.SceneConfig;
import com.linlazy.mmorpg.file.service.SceneConfigService;
import com.linlazy.mmorpg.module.common.event.EventBusHolder;
import com.linlazy.mmorpg.module.player.domain.Player;
import com.linlazy.mmorpg.module.player.dto.PlayerDTO;
import com.linlazy.mmorpg.module.player.service.PlayerService;
import com.linlazy.mmorpg.module.playercall.domain.PlayerCall;
import com.linlazy.mmorpg.module.playercall.push.PlayerCallPushHelper;
import com.linlazy.mmorpg.module.scene.copy.domain.Copy;
import com.linlazy.mmorpg.module.scene.copy.service.CopyService;
import com.linlazy.mmorpg.module.scene.domain.*;
import com.linlazy.mmorpg.module.scene.dto.SceneDTO;
import com.linlazy.mmorpg.module.scene.push.ScenePushHelper;
import com.linlazy.mmorpg.server.common.GlobalConfigService;
import com.linlazy.mmorpg.server.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 场景服务类
 * @author linlazy
 */
@Component
public class SceneService {




    /**
     * 普通场景
     */
    private Map<Integer, Scene> sceneMap = new ConcurrentHashMap<>();

    @Autowired
    private PlayerService playerService;
    @Autowired
    private CopyService copyService;
    @Autowired
    private SceneConfigService sceneConfigService;
    @Autowired
    private NpcService npcService;
    @Autowired
    private MonsterService monsterService;
    @Autowired
    private BossService bossService;
    @Autowired
    private GlobalConfigService globalConfigService;





    public Scene getSceneBySceneEntity(SceneEntity sceneEntity){


            if(globalConfigService.isCopy(sceneEntity.getSceneId())){
                Copy copy = copyService.getCopy(sceneEntity.getCopyId());
                return copy;
            }else {
                Scene scene = sceneMap.get(sceneEntity.getSceneId());
                if(scene == null){
                    scene = new Scene();
                    scene.setSceneId(sceneEntity.getSceneId());

                    SceneConfig sceneConfig = sceneConfigService.getSceneConfig(sceneEntity.getSceneId());
                    scene.setSceneName(sceneConfig.getName());
                    scene.setNeighborSet(sceneConfig.getNeighborSet());

                    //初始化怪物
                    Map<Long, Monster> monsterMap= monsterService.getMonsterBySceneId(sceneEntity.getSceneId());
                    scene.setMonsterMap( monsterMap);

                    //初始化NPC
                    Set<Npc> npcDoSet = npcService.getNPCDoBySceneId(sceneEntity.getSceneId());
                    scene.setNpcSet( npcDoSet);

                    //初始化玩家
                    Map<Long, Player> sameScenePlayerMap = playerService.getSameScenePlayerMap(sceneEntity.getSceneId());
                    scene.setPlayerMap(sameScenePlayerMap);

                    //初始化boss
                    scene.setBossList(bossService.getBOSSBySceneId(sceneEntity.getSceneId()));
                    scene.getBossList().forEach(boss -> EventBusHolder.register(boss));


                    sceneMap.put(sceneEntity.getSceneId(),scene);
                    scene.startRefreshBossScheduled();
                }
                return scene;
            }

    }



    public boolean isCopyScene(int sceneId) {
        return sceneConfigService.isCopyScene(sceneId);
    }

    public Copy getCopy(long copyId) {
        return copyService.getCopy(copyId);
    }

    public Set<SceneEntity> getAllSceneEntity(SceneEntity sceneEntity){
        //同场景（同副本）所有实体
        Set<SceneEntity> sceneEntitySet = new HashSet<>();
        if(isCopyScene(sceneEntity.getSceneId())){

            Copy copy =getCopy(sceneEntity.getCopyId());
            Set<Player> playerSet = copy.getPlayerMap().values().stream()
                    .filter(player -> player.getSceneId() == sceneEntity.getSceneId())
                    .collect(Collectors.toSet());
            Boss copyBoss = copy.getBossList().get(copy.getCurrentBossIndex());
            Collection<Monster> monsterSet = copy.getMonsterMap().values();
            Collection<PlayerCall> playerCallSet = copy.getPlayerCallMap().values();

            sceneEntitySet.addAll(playerSet);
            sceneEntitySet.add(copyBoss);
            sceneEntitySet.addAll(monsterSet);
            sceneEntitySet.addAll(playerCallSet);
        }else {

            Scene scene = getSceneBySceneEntity(sceneEntity);
            Collection<Player> playerSet = scene.getPlayerMap().values().stream()
                    .collect(Collectors.toSet());
            Collection<Monster> monsterSet = scene.getMonsterMap().values().stream()
                    .collect(Collectors.toSet());
            List<Boss> bossSet = scene.getBossList().stream()
                    .collect(Collectors.toList());
            Collection<PlayerCall> playerCallSet = scene.getPlayerCallMap().values();



            sceneEntitySet.addAll(playerSet);
            sceneEntitySet.addAll(monsterSet);
            sceneEntitySet.addAll(bossSet);
            sceneEntitySet.addAll(playerCallSet);
        }


        return sceneEntitySet;
    }

    @PostConstruct
    public void init(){
        EventBusHolder.register(this);
    }

    /**
     * 处理场景怪物死亡事件
     * @param sceneMonsterDeadEvent
     */
    @Subscribe
    private void handlerSceneMonsterDead(SceneMonsterDeadEvent sceneMonsterDeadEvent) {
        Monster monster = sceneMonsterDeadEvent.getMonster();
        Scene scene = sceneMonsterDeadEvent.getScene();

        Map<Long, Player> playerMap = sceneMonsterDeadEvent.getScene().getPlayerMap();
        playerMap.values().stream()
                .forEach(player -> ScenePushHelper.pushMonsterDead(player.getActorId(), String.format("小怪【%s】已死亡",sceneMonsterDeadEvent.getMonster().getName())));

        scene.refreshDeadMonsterSchedule(monster);

    }

    /**
     * 处理场景BOSS死亡事件
     * @param bossDeadEvent
     */
    @Subscribe
    private void handlerSceneBossDead(BossDeadEvent bossDeadEvent) {
        Scene scene = getSceneBySceneEntity(bossDeadEvent.getBoss());
        Map<Long, Player> playerMap =scene.getPlayerMap();
        playerMap.values().stream()
                .forEach(player -> ScenePushHelper.pushMonsterDead(player.getActorId(), String.format("BOSS【%s】已死亡",bossDeadEvent.getBoss().getName())));
    }


    /**
     * 处理玩家场景移动事件
     * @param sceneMoveEvent
     */
    @Subscribe
    private void handleSceneMove(SceneMoveEvent sceneMoveEvent) {
        Player player = sceneMoveEvent.getPlayer();


        Scene sceneBySceneEntity = getSceneBySceneEntity(player);
        sceneBySceneEntity.getPlayerMap().put(player.getActorId(),player);
        sceneBySceneEntity.getPlayerMap().forEach(
                (k,player1)->{
                    ScenePushHelper.pushEnterScene(player1.getActorId(),String.format("玩家【%s】进入了场景",player.getName()));
                });

        PlayerCall playerCall = player.getPlayerCall();
        if(playerCall != null){
            Scene oldScene = getSceneBySceneEntity(playerCall);
            oldScene.getPlayerCallMap().remove(playerCall.getId());

            playerCall.setSceneId(player.getSceneId());
            Scene newScene = getSceneBySceneEntity(playerCall);
            newScene.getPlayerCallMap().put(playerCall.getId(),playerCall);
            playerCall.quitAutoAttack();
            PlayerCallPushHelper.moveScene(player.getActorId(),String.format("你的召唤兽从场景【%s】跟随移动到场景【%s】",oldScene.getSceneName(),newScene.getSceneName()));
        }
    }


    /**
     * 移动到某个场景
     * @param targetSceneId
     */
    public Result<?> moveTo(long actorId,int targetSceneId){

        // 1 target场景是否存在
        if( !hasScene(targetSceneId)){
            return Result.valueOf("场景不存在");
        }

        // 2 能否移动到target场景
        if(!canMoveToScene(actorId,targetSceneId)){
            return Result.valueOf("无法移动到目标场景");
        }

        Player player = playerService.getPlayer(actorId);
        Scene sceneBySceneEntity = getSceneBySceneEntity(player);
        sceneBySceneEntity.getPlayerMap().remove(player.getActorId());
        player.setSceneId(targetSceneId);
        playerService.updatePlayer(player);

        if(isCopyScene(targetSceneId)){
            EventBusHolder.post(new CopyMoveEvent(player));
        }
        EventBusHolder.post(new SceneMoveEvent(player));

        return Result.success();
    }

    /**
     * 进入游戏
     * @param actorId
     * @return
     */
    public Result<?> enter(long actorId) {

        Player player = playerService.getPlayer(actorId);
        Scene scene = getSceneBySceneEntity(player);
        scene.playerEnterScene(player);

        return Result.success(new SceneDTO(scene).toString());
    }

    /**
     * aoi指令
     * @param actorId
     * @param jsonObject
     * @return
     */
    public Result<?> aoi(long actorId, JSONObject jsonObject) {
        Player player = playerService.getPlayer(actorId);
        Scene scene = getSceneBySceneEntity(player);
        SceneDTO sceneDTO = new SceneDTO(scene);
        boolean closeOwn = jsonObject.getBooleanValue("closeOwn");
        if( closeOwn){
            Set<PlayerDTO> playerSet = scene.getPlayerMap().values().stream()
                    .filter(player1 -> player1.getActorId() != player.getActorId())
                    .map(PlayerDTO::new)
                    .collect(Collectors.toSet());
            sceneDTO.setPlayerDTOSet(playerSet);
        }
        return Result.success(sceneDTO.toString());
    }

    /**
     * 对话NPC
     * @param actorId
     * @param npcId
     * @param jsonObject
     * @return
     */
    public Result<?> talk(long actorId, int npcId, JSONObject jsonObject) {
        // 1 当前场景是否存在
        if( !hasNPC(actorId,npcId)){
            return Result.valueOf("当前场景不存在此npc");
        }

        return Result.success("和npc对话");
    }
    public boolean hasScene(int sceneId){
        return sceneConfigService.hasScene(sceneId);
    }

    public boolean canMoveToScene(long actorId,int sceneId){
        Player player = playerService.getPlayer(actorId);
        return sceneConfigService.canMoveToTarget(player.getSceneId(),sceneId);
    }

    public boolean hasNPC(long actorId, int npcId) {
        Player player = playerService.getPlayer(actorId);
        int sceneId = player.getSceneId();
        return npcService.hasNPC(sceneId,npcId);
    }

    /**
     * 传送到某个场景
     * @param actorId 玩家ID
     * @param targetSceneId 目标场景
     */
    public void transportTo(long actorId,int targetSceneId){
        Player player = playerService.getPlayer(actorId);
        player.setSceneId(targetSceneId);

        EventBusHolder.post(new SceneMoveEvent(player));

        if(isCopyScene(targetSceneId)){
            EventBusHolder.post(new CopyMoveEvent(player));
        }
    }

}
