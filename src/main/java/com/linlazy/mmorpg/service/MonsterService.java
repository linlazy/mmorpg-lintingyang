package com.linlazy.mmorpg.service;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.constants.SceneEntityType;
import com.linlazy.mmorpg.domain.Monster;
import com.linlazy.mmorpg.domain.Skill;
import com.linlazy.mmorpg.file.service.MonsterConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author linlazy
 */
@Component
public class MonsterService {

    @Autowired
    private MonsterConfigService monsterConfigService;
    @Autowired
    private SkillService skillService;

    /**
     * 获取场景怪物
     * @return
     * @param sceneId
     */
    public  Map<Integer,Monster> getMonsterBySceneId(int sceneId){
        Map<Integer,Monster> monsterMap = new HashMap<>();

        List<JSONObject>  sceneMonsterConfigs= monsterConfigService.getMonsterBySceneId(sceneId);
        for(int i = 0; i < sceneMonsterConfigs.size(); i++){
            JSONObject sceneMonsterConfig = sceneMonsterConfigs.get(i);
            int monsterId = sceneMonsterConfig.getIntValue("monsterId");
            String name = sceneMonsterConfig.getString("name");
            int hp = sceneMonsterConfig.getIntValue("hp");
            int attack = sceneMonsterConfig.getIntValue("useSkill");
            Monster monster = new Monster();
            monster.setMonsterId(monsterId);
            monster.setName(name);
            monster.setSceneId(sceneId);
            monster.setHp(hp);
            monster.setAttack(attack);
            monster.setSceneEntityType(SceneEntityType.MONSTER);

            List<Skill> monsterSkillList = skillService.getMonsterSkillList(monster.getMonsterId());
            monster.setSkillList(monsterSkillList);

            monsterMap.put(monster.getMonsterId(),monster);
        }

        return monsterMap;
    }


}
