package com.linlazy.scene.config;


import com.google.common.collect.Sets;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 游戏场景配置服务类
 */
@Component
public class SceneConfigService {

//    private final static Set<SceneConfig>  sceneConfigSet= new HashSet<>();

    private final static Map<Integer,SceneConfig> sceneConfigMap= new HashMap<>();
    //需要根据ID 获取配置，set 改为map

    @PostConstruct
    public void init(){

        sceneConfigMap.put(1,new SceneConfig(1,"起始之地", Sets.newHashSet(2)));
        sceneConfigMap.put(2,new SceneConfig(2,"村子",Sets.newHashSet(1,3,4)));
        sceneConfigMap.put(3,new SceneConfig(3,"城堡",Sets.newHashSet(2)));
        sceneConfigMap.put(4,new SceneConfig(4,"森林",Sets.newHashSet(2)));
    }

    /**
     *  场景是否存在
     * @param targetSceneId 玩家前往场景ID
     * @return
     */
    public boolean hasScene(int targetSceneId) {
        for (SceneConfig sceneConfig: sceneConfigMap.values()){
            if(sceneConfig.getSceneId() == targetSceneId){
                return true;
            }
        }
        return false;
    }

    /**
     * 能否从当前场景移动到目标场景
     * @param currentSceneId 玩家所处场景ID
     * @param targetSceneId 玩家移动目标场景ID
     * @return
     */
    public boolean canMoveToTarget(int currentSceneId, int targetSceneId) {
        SceneConfig sceneConfig = sceneConfigMap.get(currentSceneId);
        return sceneConfig.getNeighborSet().contains(targetSceneId);
    }
}
