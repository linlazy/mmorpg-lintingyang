package com.linlazy.mmorpglintingyang.module.scene.manager;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.common.event.ActorEvent;
import com.linlazy.mmorpglintingyang.module.common.event.EventBusHolder;
import com.linlazy.mmorpglintingyang.module.common.event.EventType;
import com.linlazy.mmorpglintingyang.module.copy.domain.CopyDo;
import com.linlazy.mmorpglintingyang.module.copy.manager.CopyManager;
import com.linlazy.mmorpglintingyang.module.scene.domain.MonsterDo;
import com.linlazy.mmorpglintingyang.module.scene.domain.NpcDo;
import com.linlazy.mmorpglintingyang.module.scene.domain.SceneDo;
import com.linlazy.mmorpglintingyang.module.scene.domain.SceneEntityDo;
import com.linlazy.mmorpglintingyang.module.user.manager.UserManager;
import com.linlazy.mmorpglintingyang.module.user.manager.entity.User;
import com.linlazy.mmorpglintingyang.server.common.GlobalConfigService;
import com.linlazy.mmorpglintingyang.server.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author linlazy
 */
@Component
public class SceneManager {

    @Autowired
    private UserManager userManager;
    @Autowired
    private MonsterManager monsterManager;
    @Autowired
    private NpcManager npcManager;

    @Autowired
    private CopyManager copyManager;
    @Autowired
    private GlobalConfigService globalConfigService;

    private Map<Integer,SceneDo> map = new HashMap<>();

    /**
     * 移动到某个场景
     * @param targetSceneId
     */
   public Result<?> moveToScene(long actorId ,int targetSceneId){

       //原场景中移除玩家
       User user = userManager.getUser(actorId);
       SceneDo sceneDo = getSceneDo(actorId);
       sceneDo.getActorIdSet().remove(actorId);


       user.setSceneId(targetSceneId);
       userManager.updateUser(user);
       //新场景中增加玩家
       SceneDo targetSceneDo = getSceneDo(actorId);
       targetSceneDo.getActorIdSet().add(actorId);
       return Result.success();
   }

    /**
     * 进入场景
     * @param actorId
     * @return
     */
    public Result<?> enter(long actorId) {
        SceneDo sceneDo = getSceneDo(actorId);
        sceneDo.getActorIdSet().add(actorId);

        if(globalConfigService.isCopy(sceneDo.getSceneId())){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("sceneId",sceneDo.getSceneId());
            EventBusHolder.post(new ActorEvent<>(actorId, EventType.ENTER_COPY_SCENE));
        }
        return Result.success(sceneDo);
    }

    public SceneDo getSceneDo(long actorId) {
        User user = userManager.getUser(actorId);
        int sceneId = user.getSceneId();
        if(globalConfigService.isCopy(sceneId)){
            CopyDo copyDo = copyManager.getCopyDo(actorId,sceneId);
            return copyDo.convertSceneDo();
        }else {
            SceneDo sceneDo = map.get(sceneId);
            if(sceneDo == null){
                sceneDo = new SceneDo();

                sceneDo.setSceneId(sceneId);

                Set<SceneEntityDo> sceneEntityDoSet = sceneDo.getSceneEntityDoSet();
                //初始化怪物
                Set<MonsterDo> monsterDoSet = monsterManager.getMonsterBySceneId(sceneId);
                sceneEntityDoSet.addAll( monsterDoSet.stream()
                        .map(SceneEntityDo::new)
                        .collect(Collectors.toSet()));

                //初始化NPC
                Set<NpcDo> npcDoSet = npcManager.getNPCDoBySceneId(sceneId);
                sceneEntityDoSet.addAll( npcDoSet.stream()
                        .map(SceneEntityDo::new)
                        .collect(Collectors.toSet()));

                //初始化玩家
                Set<Long> actorIdSet = sceneDo.getActorIdSet();
                actorIdSet.add(user.getActorId());
                map.put(sceneId,sceneDo);
            }
            return sceneDo;
        }

    }
}
