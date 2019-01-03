package com.linlazy.mmorpglintingyang.module.scene.manager;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpglintingyang.module.scene.config.MonsterConfigService;
import com.linlazy.mmorpglintingyang.module.scene.domain.MonsterDo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author linlazy
 */
@Component
public class MonsterManager {

    @Autowired
    private MonsterConfigService monsterConfigService;

    /**
     * 获取场景怪物
     * @return
     * @param sceneId
     */
    public Set<MonsterDo> getMonsterBySceneId(int sceneId){
        Set<MonsterDo> monsterDoSet = new HashSet<>();

        List<JSONObject>  sceneMonsterConfigs= monsterConfigService.getMonsterBySceneId(sceneId);
        for(int i = 0; i < sceneMonsterConfigs.size(); i++){
            JSONObject sceneMonsterConfig = sceneMonsterConfigs.get(i);
            int monsterId = sceneMonsterConfig.getIntValue("monsterId");
            String name = sceneMonsterConfig.getString("name");
            int hp = sceneMonsterConfig.getIntValue("hp");
            MonsterDo monsterDo = new MonsterDo();
            monsterDo.setMonsterId(monsterId);
            monsterDo.setName(name);
            monsterDo.setSceneId(sceneId);
            monsterDo.setHp(hp);

            monsterDoSet.add(monsterDo);
        }

        return monsterDoSet;
    }


}
