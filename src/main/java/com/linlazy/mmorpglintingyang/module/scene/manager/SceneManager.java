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

        return Result.success(sceneDo);
    }

    public SceneDo getSceneDo(long actorId) {
        User user = userManager.getUser(actorId);
        SceneDo sceneDo = map.get(user.getSceneId());
        if(sceneDo == null){
            sceneDo = new SceneDo();

            sceneDo.setSceneId(user.getSceneId());

            Set<SceneEntityDo> sceneEntityDoSet = sceneDo.getSceneEntityDoSet();
            //初始化怪物
            Set<MonsterDo> monsterDoSet = monsterManager.getMonsterBySceneId(user.getSceneId());
            sceneEntityDoSet.addAll( monsterDoSet.stream()
                    .map(SceneEntityDo::new)
                    .collect(Collectors.toSet()));

            //初始化NPC
            Set<NpcDo> npcDoSet = npcManager.getNPCDoBySceneId(user.getSceneId());
            sceneEntityDoSet.addAll( npcDoSet.stream()
                    .map(SceneEntityDo::new)
                    .collect(Collectors.toSet()));

            //初始化玩家
            Set<Long> actorIdSet = sceneDo.getActorIdSet();
            actorIdSet.add(user.getActorId());
            map.put(user.getSceneId(),sceneDo);
        }

        return sceneDo;
    }
}
