package com.linlazy.mmorpglintingyang.module.skill.manager;

import com.linlazy.mmorpglintingyang.module.skill.manager.entity.model.SkillInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;
@RunWith(SpringRunner.class)
@SpringBootTest
public class SkillManagerTest {

    @Autowired
    private SkillManager skillManager;

    @Test
    public void getActorSkillInfoSet() {
        Set<SkillInfo> actorSkillInfoSet = skillManager.getActorSkillInfoSet(3);
    }
}