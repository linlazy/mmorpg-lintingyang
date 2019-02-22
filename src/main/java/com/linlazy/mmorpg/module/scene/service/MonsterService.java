package com.linlazy.mmorpg.module.scene.service;

import com.alibaba.fastjson.JSONObject;
import com.linlazy.mmorpg.file.config.MonsterConfig;
import com.linlazy.mmorpg.file.service.MonsterConfigService;
import com.linlazy.mmorpg.file.service.SceneConfigService;
import com.linlazy.mmorpg.module.common.reward.Reward;
import com.linlazy.mmorpg.module.scene.constants.SceneEntityType;
import com.linlazy.mmorpg.module.scene.domain.Monster;
import com.linlazy.mmorpg.module.skill.domain.Skill;
import com.linlazy.mmorpg.module.skill.service.SkillService;
import com.linlazy.mmorpg.utils.RewardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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
    @Autowired
    private SceneConfigService sceneConfigService;

    /**
     * 获取场景怪物
     * @return
     * @param sceneId
     */
    public  Map<Long,Monster> getMonsterBySceneId(int sceneId){
        Map<Long,Monster> monsterMap = new HashMap<>();

        List<JSONObject>  sceneMonsterConfigs= monsterConfigService.getMonsterBySceneId(sceneId);
        for(int i = 0; i < sceneMonsterConfigs.size(); i++){
            JSONObject sceneMonsterConfig = sceneMonsterConfigs.get(i);
            int monsterId = sceneMonsterConfig.getIntValue("monsterId");
            String name = sceneMonsterConfig.getString("name");
            int hp = sceneMonsterConfig.getIntValue("hp");
            int attack = sceneMonsterConfig.getIntValue("useSkill");
            String rewards = sceneMonsterConfig.getString("rewards");


            Monster monster = new Monster();

            if(!StringUtils.isEmpty(rewards)){
                List<Reward> rewardList = RewardUtils.parseRewards(rewards);
                monster.setReward(rewardList);
            }

            monster.setMonsterId(monsterId);
            monster.setName(name);
            monster.setType(sceneMonsterConfig.getIntValue("type"));
            monster.setSceneId(sceneId);
            monster.setHp(hp);
            monster.setAttack(attack);
            monster.setSceneEntityType(SceneEntityType.MONSTER);

            List<Skill> monsterSkillList = skillService.getMonsterSkillList(monster.getMonsterId());
            monster.setSkillList(monsterSkillList);

            monsterMap.put(monster.getId(),monster);
        }

        return monsterMap;
    }

    /**
     * 获取场景怪物
     * @return
     * @param monsterId
     */
    public  Monster getMonsterByMonsterId(int sceneId,long monsterId){

        MonsterConfig monsterConfig = monsterConfigService.getMonsterConfig(monsterId);

            Monster monster = new Monster();
            monster.setMonsterId(monsterConfig.getMonsterId());
            monster.setName(monsterConfig.getName());
            monster.setType(monsterConfig.getType());
            monster.setSceneId(sceneId);
            monster.setHp(monsterConfig.getHp());
            monster.setAttack(monsterConfig.getAttack());
            monster.setSceneEntityType(SceneEntityType.MONSTER);

            monster.setReward(monsterConfig.getRewardList());

            List<Skill> monsterSkillList = skillService.getMonsterSkillList(monster.getMonsterId());
            monster.setSkillList(monsterSkillList);

        return monster;
    }

}
