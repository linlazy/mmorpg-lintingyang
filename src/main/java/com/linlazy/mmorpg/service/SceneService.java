package com.linlazy.mmorpg.service;

import com.linlazy.mmorpg.domain.*;
import com.linlazy.mmorpg.event.type.PlayerMoveSceneEvent;
import com.linlazy.mmorpg.module.common.event.EventBusHolder;
import com.linlazy.mmorpg.module.scene.config.SceneConfigService;
import com.linlazy.mmorpg.module.scene.domain.SceneEntity;
import com.linlazy.mmorpg.server.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    public Scene getScene(int sceneId){
        Scene scene = sceneMap.get(sceneId);
        if(scene == null){

        }
        return scene;
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
}
