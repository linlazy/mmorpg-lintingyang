package com.linlazy.mmorpglintingyang.module.scene.manager;

import com.linlazy.mmorpglintingyang.module.scene.domain.MonsterDo;
import com.linlazy.mmorpglintingyang.module.scene.domain.NpcDo;
import com.linlazy.mmorpglintingyang.module.scene.domain.SceneDo;
import com.linlazy.mmorpglintingyang.module.scene.domain.SceneEntityDo;
import com.linlazy.mmorpglintingyang.module.user.manager.UserManager;
import com.linlazy.mmorpglintingyang.module.user.manager.entity.User;
import com.linlazy.mmorpglintingyang.server.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SceneManager {

    @Autowired
    private UserManager userManager;
    @Autowired
    private MonsterManager monsterManager;
    @Autowired
    private NPCManager npcManager;

    private Map<Integer,SceneDo> map = new HashMap<>();

    /**
     * 移动到某个场景
     * @param targetSceneId
     */
   public Result<?> moveToScene(long actorId ,int targetSceneId){

       //原场景中移除玩家
       User user = userManager.getUser(actorId);
       SceneDo sceneDo = getSceneDoByActorId(actorId);
       sceneDo.getActorIdSet().remove(actorId);

       //新场景中增加玩家
       SceneDo targetSceneDo = getSceneDoBySceneId(targetSceneId);
       targetSceneDo.getActorIdSet().add(actorId);

       user.setSceneId(targetSceneId);
       userManager.updateUser(user);
       return Result.success();
   }

    /**
     * 进入场景
     * @param actorId
     * @return
     */
    public Result<?> enter(long actorId) {
        SceneDo sceneDo = getSceneDoByActorId(actorId);
        sceneDo.getActorIdSet().add(actorId);

        return Result.success(sceneDo);
    }


    public SceneDo getSceneDoByActorId(long actorId) {
        User user = userManager.getUser(actorId);
        SceneDo sceneDo  = getSceneDoBySceneId(user.getSceneId());
        return sceneDo;
    }

    private SceneDo getSceneDoBySceneId(int sceneId) {
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

            map.put(sceneId,sceneDo);
        }

        return sceneDo;
    }
}
