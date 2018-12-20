package com.linlazy.mmorpglintingyang.module.fight.skill.domain;

import com.google.common.collect.Sets;
import com.linlazy.mmorpglintingyang.module.scene.manager.dao.SceneDao;
import com.linlazy.mmorpglintingyang.module.scene.manager.domain.SceneMonster;
import com.linlazy.mmorpglintingyang.module.scene.manager.entity.Scene;
import com.linlazy.mmorpglintingyang.module.fight.skill.dao.SkillDao;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.stream.Collectors;

@Data
public class DressSkill {

    /**
     * 玩家ID
     */
    private long actorId;
    /**
     * 已穿戴技能
     */
    private Set<SkillDo> skillDoSet;

    private SceneDao sceneDao;

    @Autowired
    private SkillDao skillDao;

    public DressSkill(long actorId) {
        skillDoSet =skillDao.getSkillSet(actorId)
                .stream()
                .map(SkillDo::new)
                .collect(Collectors.toSet());
    }

    /**
     * 攻击怪物
     */
    public void attackMonsterWith(int skillId,long monsterId){
        SkillDo skillDo = skillDoSet.stream()
                .filter(skillDo1 -> skillDo1.getSkillId() == skillId)
                .findFirst().get();

        //计算总伤害
        int attack = skillDo.computeFinalAttack();

        Scene scene = sceneDao.getScene(actorId);
        new SceneMonster(scene.getSceneId()).attacked(Sets.newHashSet(monsterId),attack);
    }
}
