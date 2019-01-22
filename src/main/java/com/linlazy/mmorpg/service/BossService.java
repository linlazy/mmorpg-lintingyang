package com.linlazy.mmorpg.service;

import com.linlazy.mmorpg.constants.SceneEntityType;
import com.linlazy.mmorpg.domain.Boss;
import com.linlazy.mmorpg.domain.Skill;
import com.linlazy.mmorpg.file.config.BossConfig;
import com.linlazy.mmorpg.file.service.BossConfigService;
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

                    List<Skill> bossSkillList = skillService.getBossSkillList(boss.getBossId());
                    boss.setSkillList(bossSkillList);

                    return boss;
                }).collect(Collectors.toList());
    }

}
