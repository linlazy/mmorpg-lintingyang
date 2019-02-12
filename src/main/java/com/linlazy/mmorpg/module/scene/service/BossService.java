package com.linlazy.mmorpg.module.scene.service;

import com.linlazy.mmorpg.file.config.SceneConfig;
import com.linlazy.mmorpg.file.service.SceneConfigService;
import com.linlazy.mmorpg.module.scene.constants.SceneEntityType;
import com.linlazy.mmorpg.module.scene.domain.Boss;
import com.linlazy.mmorpg.module.skill.domain.Skill;
import com.linlazy.mmorpg.file.config.BossConfig;
import com.linlazy.mmorpg.file.service.BossConfigService;
import com.linlazy.mmorpg.module.common.reward.Reward;
import com.linlazy.mmorpg.module.skill.service.SkillService;
import com.linlazy.mmorpg.utils.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author linlazy
 */
@Component
public class BossService {


    @Autowired
    private BossConfigService bossConfigService;
    @Autowired
    private SkillService skillService;
    @Autowired
    private SceneConfigService sceneConfigService;


    public List<Boss> getBOSSBySceneId(int sceneId){
        List<BossConfig> bossConfigList = bossConfigService.getBossConfigBySceneId(sceneId);
        return bossConfigList.stream()
                .map(bossConfig -> {
                    Boss boss = new Boss();
                    boss.setSceneId(sceneId);
                    boss.setBossId(bossConfig.getBossId());
                    boss.setHp(bossConfig.getHp());
                    boss.setAttack(bossConfig.getAttack());
                    boss.setName(bossConfig.getName());
                    boss.setSceneEntityType(SceneEntityType.BOSS);
                    boss.setType(bossConfig.getType());
                    Reward reward = RandomUtils.randomElement(bossConfig.getRewardList());
                    boss.setReward(reward);

                    List<Skill> bossSkillList = skillService.getBossSkillList(boss.getBossId());
                    boss.setSkillList(bossSkillList);

                    return boss;
                }).collect(Collectors.toList());
    }

    public List<Boss> getCopyBOSSBySceneId(int sceneId){
        SceneConfig sceneConfig = sceneConfigService.getSceneConfig(sceneId);
        List<Long> bossIdList = sceneConfig.getBossIdList();
        List<BossConfig> bossConfigList = bossIdList.stream().map(bossId -> bossConfigService.getBossConfig(bossId)).collect(Collectors.toList());
        return bossConfigList.stream()
                .map(bossConfig -> {
                    Boss boss = new Boss();
                    boss.setSceneId(sceneId);
                    boss.setBossId(bossConfig.getBossId());
                    boss.setHp(bossConfig.getHp());
                    boss.setAttack(bossConfig.getAttack());
                    boss.setName(bossConfig.getName());
                    boss.setSceneEntityType(SceneEntityType.BOSS);
                    boss.setType(bossConfig.getType());
                    Reward reward = RandomUtils.randomElement(bossConfig.getRewardList());
                    boss.setReward(reward);

                    List<Skill> bossSkillList = skillService.getBossSkillList(boss.getBossId());
                    boss.setSkillList(bossSkillList);

                    return boss;
                }).collect(Collectors.toList());
    }

}
