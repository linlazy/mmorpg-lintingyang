package com.linlazy.mmorpg.service;

import com.linlazy.mmorpg.domain.Player;
import com.linlazy.mmorpg.domain.PlayerSceneInfo;
import com.linlazy.mmorpg.domain.Scene;
import com.linlazy.mmorpg.event.type.PlayerMoveSceneEvent;
import com.linlazy.mmorpg.module.common.event.EventBusHolder;
import com.linlazy.mmorpg.server.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 场景服务类
 * @author linlazy
 */
@Component
public class SceneService {


    private Map<Integer, Scene> sceneMap = new ConcurrentHashMap<>();

    @Autowired
    private PlayerService playerService;

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
        PlayerSceneInfo playerSceneInfo = player.getPlayerSceneInfo();
        playerSceneInfo.setSceneId(targetSceneId);

        EventBusHolder.register(new PlayerMoveSceneEvent(player));
        return Result.success();
    }
}
