package com.linlazy.mmorpg.service;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.domain.Monster;
import com.linlazy.mmorpg.file.service.MonsterConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author linlazy
 */
@Component
public class MonsterService {

    @Autowired
    private MonsterConfigService monsterConfigService;

    /**
     * 获取场景怪物
     * @return
     * @param sceneId
     */
    public Set<Monster> getMonsterBySceneId(int sceneId){
        Set<Monster> monsterSet = new HashSet<>();

        List<JSONObject>  sceneMonsterConfigs= monsterConfigService.getMonsterBySceneId(sceneId);
        for(int i = 0; i < sceneMonsterConfigs.size(); i++){
            JSONObject sceneMonsterConfig = sceneMonsterConfigs.get(i);
            int monsterId = sceneMonsterConfig.getIntValue("monsterId");
            String name = sceneMonsterConfig.getString("name");
            int hp = sceneMonsterConfig.getIntValue("hp");
            Monster monster = new Monster();
            monster.setMonsterId(monsterId);
            monster.setName(name);
            monster.setSceneId(sceneId);
            monster.setHp(hp);

            monsterSet.add(monster);
        }

        return monsterSet;
    }


}
