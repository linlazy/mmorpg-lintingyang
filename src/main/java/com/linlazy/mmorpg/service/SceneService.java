package com.linlazy.mmorpg.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import com.linlazy.mmorpg.domain.*;
import com.linlazy.mmorpg.dto.PlayerDTO;
import com.linlazy.mmorpg.dto.SceneDTO;
import com.linlazy.mmorpg.event.type.PlayerMoveSceneEvent;
import com.linlazy.mmorpg.event.type.SceneMonsterDeadEvent;
import com.linlazy.mmorpg.file.config.SceneConfig;
import com.linlazy.mmorpg.file.service.SceneConfigService;
import com.linlazy.mmorpg.module.common.event.ActorEvent;
import com.linlazy.mmorpg.module.common.event.EventBusHolder;
import com.linlazy.mmorpg.module.common.event.EventType;
import com.linlazy.mmorpg.push.ScenePushHelper;
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

    public Scene getScene(long actorId){
        Player player = playerService.getPlayer(actorId);


            if(globalConfigService.isCopy(player.getSceneId())){
                Copy copy = copyService.getCopy(player.getCopyId());
                return copy;
            }else {
                Scene scene = sceneMap.get(actorId);
                if(scene == null){
                    scene = new Scene();
                    scene.setSceneId(player.getSceneId());

                    SceneConfig sceneConfig = sceneConfigService.getSceneConfig(player.getSceneId());
                    scene.setSceneName(sceneConfig.getName());
                    scene.setNeighborSet(sceneConfig.getNeighborSet());

                    //初始化怪物
                    Set<Monster> monsterDoSet = monsterService.getMonsterBySceneId(player.getSceneId());
                    scene.setMonsterSet( monsterDoSet);

                    //初始化NPC
                    Set<Npc> npcDoSet = npcService.getNPCDoBySceneId(player.getSceneId());
                    scene.setNpcSet( npcDoSet);

                    //初始化玩家
                    Set<Player> sameScenePlayerSet = playerService.getSameScenePlayerSet(actorId);
                    scene.setPlayerSet(sameScenePlayerSet);

                    //初始化boss
                    Set<Boss> bossSet = bossService.getBOSSBySceneId(player.getSceneId());
                    scene.setBossSet(bossSet);

                    sceneMap.put(player.getSceneId(),scene);
                }
                return scene;
            }

    }

    /**
     * 移动到某个场景
     * @param targetSceneId
     */
    public Result<?> moveToScene(long actorId , int targetSceneId){
        Player player = playerService.getPlayer(actorId);
        player.setSceneId(targetSceneId);

        EventBusHolder.register(new PlayerMoveSceneEvent(player));
        return Result.success();
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
            Set<Player> playerSet = copy.getPlayerCopyInfoMap().values().stream()
                    .map(PlayerCopyInfo::getPlayer).collect(Collectors.toSet());
            List<Boss> copyBoss = copy.getCopyBoss();
            Collection<Monster> monsterSet = copy.getCopyMonsterInfoMap().values();
            sceneEntitySet.addAll(playerSet);
            sceneEntitySet.addAll(copyBoss);
            sceneEntitySet.addAll(monsterSet);

        }else {

            Scene scene = getScene(sceneEntity.getSceneId());
            Set<Player> playerSet = scene.getPlayerSet();
            Set<Monster> monsterSet = scene.getMonsterSet();
            Set<Boss> bossSet = scene.getBossSet();
            sceneEntitySet.addAll(playerSet);
            sceneEntitySet.addAll(monsterSet);
            sceneEntitySet.addAll(bossSet);
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
        Set<Player> playerSet = sceneMonsterDeadEvent.getScene().getPlayerSet();
        playerSet.stream()
            .forEach(player ->   ScenePushHelper.pushMonster(player.getActorId(), Lists.newArrayList(sceneMonsterDeadEvent.getMonster())));
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

        player.setSceneId(targetSceneId);
        return Result.success();
    }

    /**
     * 进入场景
     * @param actorId
     * @return
     */
    public Result<?> enter(long actorId) {

        Player player = playerService.getPlayer(actorId);
        Scene scene = getScene(actorId);
        scene.getPlayerSet().add(player);

        if(globalConfigService.isCopy(scene.getSceneId())){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("sceneId",scene.getSceneId());
            EventBusHolder.post(new ActorEvent<>(actorId, EventType.ENTER_COPY_SCENE));
        }
        return Result.success(new SceneDTO(scene));
    }

    /**
     * aoi指令
     * @param actorId
     * @param jsonObject
     * @return
     */
    public Result<?> aoi(long actorId, JSONObject jsonObject) {
        Player player = playerService.getPlayer(actorId);
        Scene scene = getScene(actorId);
        SceneDTO sceneDTO = new SceneDTO(scene);
        boolean closeOwn = jsonObject.getBooleanValue("closeOwn");
        if( closeOwn){
            Set<PlayerDTO> playerSet = scene.getPlayerSet().stream()
                    .filter(player1 -> player1.getActorId() != player.getActorId())
                    .map(PlayerDTO::new)
                    .collect(Collectors.toSet());
            sceneDTO.setPlayerDTOSet(playerSet);
        }
        return Result.success(sceneDTO);
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

}
